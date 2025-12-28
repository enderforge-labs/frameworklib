package com.snek.frameworklib.graphics.core.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.InterpolatedData;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.TransitionStep;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.containers.IndexedArrayDeque;
import com.snek.frameworklib.data_types.displays.CustomDisplay;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Easing;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.scheduler.Scheduler;
import com.snek.frameworklib.utils.scheduler.TaskHandler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Display.BillboardConstraints;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
















/**
 * The base class of every visible graphic element.
 * <p>
 * This implements automatic entity management support for animations on top of the {@link Div} base class.
 */
public abstract class Elm extends Div {
    public static final @NotNull String ENTITY_CUSTOM_NAME = FrameworkLib.LIB_ID + ".ui.displayentity";
    public static final int QUEUE_LINGER_TICKS = 4;
    // ^ Additional update ticks the element stays in the update queue for after all of its steps have been processed.


    // Animation handling
    //FIXME use a custom linked list or something. adding and removing from elmUpdateQueue should be O(1)
    private   static final @NotNull List<Elm> elmUpdateQueue = new ArrayList<>();   // The list of instances with pending transition steps
    protected        final @NotNull IndexedArrayDeque<InterpolatedData> futureDataQueue = new IndexedArrayDeque<>(); // The list of transition steps to apply to this instance in the next ticks. 1 for each update tick
    private boolean isQueued = false;                                               // Whether this instance is queued for updates. Updated manually
    private int queueLingerTicks = 0;


    // Forced imperceptible changes applied to the entity's interpolated data.
    //! Minecraft's display entity rendering system repeats ticks if the new value doesn't differ from the old one.
    //! EPSILON is either added or subtracted from the target value intermittently in order to minimize error build ups and prevent visual tick duplicates.
    //! 0.000001 is the minimum value difference Minecraft can recognize as a change.
    public static final float EPSILON = 0.0000011f;
    protected int epsilonPolarity = 1;


    // Despawn task handler
    private @Nullable TaskHandler despawnFinalizerTaskHandler = null;


    // In-world data
    protected final @NotNull ServerLevel   level;     // The level this Elm will be spawned in
    private   final @NotNull CustomDisplay entity;    // The display entity held by this element
    private         @NotNull ElmStyle      style;     // The style of the element
    private boolean isTransformNormalized = true;




    /**
     * Retrieves the display held by this element.
     * @return The custom display.
     */
    public @NotNull CustomDisplay getEntity() {
        assert Require.nonNull(entity, "entity");
        return entity;
    }

    /**
     * Retrieves the custom display held by this element as the specified subclass.
     * @param type The sublass to cast the custom display to.
     * @return The custom display casted to the specified class.
     */
    public <T> @NotNull T getEntity(final @NotNull Class<T> type) {
        assert Require.nonNull(entity, "entity");
        assert Require.instanceOf(entity, type, "entity");
        return type.cast(entity);
    }


    /**
     * Retrieves the style used by this element.
     * @return The style.
     */
    public @NotNull ElmStyle getStyle() {
        assert Require.nonNull(style, "style");
        return style;
    }

    /**
     * Retrieves the style used by this element as the specified subclass.
     * @param type The sublass to cast the style to.
     * @return The style casted to the specified class.
     */
    public <T> @NotNull T getStyle(final @NotNull Class<T> type) {
        assert Require.nonNull(style, "style");
        assert Require.instanceOf(style, type, "style");
        return type.cast(style);
    }

    /**
     * Changes the style used by this element.
     * <p>
     * {@link #flushStyle()} must be called in order to update the entities.
     * @param style The new style value.
     */
    public void setStyle(final @NotNull ElmStyle style) {
        assert Require.nonNull(style, "style");
        this.style = style;
    }








    /**
     * Creates a new Elm using an existing CustomDisplay and a custom style.
     * @param level The level in which to place the element.
     * @param entity The display entity.
     * @param style The custom style.
     */
    protected Elm(final @NotNull ServerLevel level, final @NotNull CustomDisplay entity, final @NotNull ElmStyle style) {
        super();
        assert Require.nonNull(level, "level");
        assert Require.nonNull(entity, "entity");
        assert Require.nonNull(style, "style");

        this.level  = level;
        this.entity = entity;
        this.style  = style;
        this.style.resetAll();
    }




    /**
     * Flushes changeable style values to the entity.
     * <p>
     * This does not start an interpolation.
     */
    public void flushStyle() {
        assert Require.nonNull(style, "style");
        epsilonPolarity *= -1;

        // Apply transform
        { final Flagged<Transform> f = style.getFlaggedTransform();
        if(f.isFlagged()) {
            entity.setTransformation(__calcTransform().moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
            f.unflag();
        }}

        // Apply view range
        { final Flagged<Float> f = style.getFlaggedViewRange();
        if(f.isFlagged()) {
            entity.setViewRange(f.get());
            f.unflag();
        }}

        // Apply billboard mode
        { final Flagged<BillboardConstraints> f = style.getFlaggedBillboardMode();
        if(f.isFlagged()) {
            entity.setBillboardMode(f.get());
            f.unflag();
        }}
    }




    @Override
    public void updateAbsPosSelf() {
        final Vector2f oldPos = new Vector2f(getAbsPos());
        super.updateAbsPosSelf();
        if(!getAbsPos().equals(oldPos)) {
            style.editTransform();
            flushStyle();
        }
        //! This check's sole purpose is to prevent unneeded transform updates and comparisons
    }


    @Override
    public void updateZIndexSelf() {
        final int oldZIndex = getZIndex();
        super.updateZIndexSelf();
        if(getZIndex() != oldZIndex) {
            style.editTransform();
            flushStyle();
        }
        //! This check's sole purpose is to prevent unneeded transform updates and comparisons
    }


    @Override
    public int getLayerCount() {
        return 1;
    }


    //TODO this needs caching. Use a Flagged value to cache the transform and update it when any of its variables change
    //TODO this needs caching. Use a Flagged value to cache the transform and update it when any of its variables change
    //TODO all subclasses too
    /**
     * Calculates the final transform to apply to the entity.
     * <p>
     * This should take into account the element's position, alignment options, Z-index and visual transform.
     * @return The transform.
     */
    public @NotNull Transform __calcTransform() {
        assert Require.nonNull(style, "style");
        assert Require.nonNull(style.getTransform(), "style transform");
        return style.getTransform().copy().move(getAbsPos().x, getAbsPos().y, getZIndex() * Configs.getUi().z_layer_spacing.getValue());
    }







    /**
     * Instantly calculates animation steps and adds this element to the update queue.
     * <p>
     * Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    @Override
    public void applyAnimation(final @NotNull Animation animation) {
        super.applyAnimation(animation);

        // Add element to the update queue and update the queued state
        if(!isQueued) {
            elmUpdateQueue.add(this);
            isQueued = true;
            queueLingerTicks = QUEUE_LINGER_TICKS;
        }

        // Apply each transition one at a time
        int shift = 0;
        for(final Transition transition : animation.getTransitions()) {
            shift += __applyAnimationTransition(transition, shift);
        }
    }




    @Override
    public void applyAnimationNow(final @NotNull Animation animation) {
        super.applyAnimationNow(animation);

        // Apply each transition one at a time
        for(final Transition transition : animation.getTransitions()) {
            __applyAnimationTransitionNow(transition);
        }
    }


    /**
     * Helper function.
     * <p>
     * Instantly calculates the result of a single transition and applies it to the element.
     * @param t The transition to apply.
     */
    protected void __applyAnimationTransitionNow(final @NotNull Transition t) {
        assert Require.nonNull(t, "transition");

        // Calculate step and apply it instantly
        final TransitionStep step = t.createStep(1);
        final InterpolatedData data = __generateInterpolatedData();
        data.apply(step);
        __applyTransitionStep(data);
        flushStyle();

        // Update existing future data if present. Instantly start the interpolation otherwise
        if(futureDataQueue.isEmpty()) {
            entity.setInterpolationDuration(0);
            entity.setStartInterpolation();
        }
        else {
            for(final InterpolatedData d : futureDataQueue) {
                d.apply(step);
            }
        }
    }




    /**
     * Helper function.
     * <p>
     * Instantly calculates the steps of a single transition and adds them to this element's future data.
     * @param transition The transition to apply.
     * @param shift the amount of future data to skip before applying this transition.
     * @return The amount of future data this transition affected.
     */
    protected int __applyAnimationTransition(final @NotNull Transition transition, final int shift) {
        assert Require.nonNull(transition, "transition");
        assert Require.nonNegative(shift, "shift");


        // Calculate transition as a list of steps
        final List<TransitionStep> animationSteps = new ArrayList<>();
        final int time = transition.getDuration();            // The duration of this transition
        final Easing e = transition.getEasing();
        final Integer refreshTime = Configs.getPerf().animation_refresh_time.getValue();
        for(int i = 0; i == 0 || i < time; i += refreshTime) {
            final float factor = (float)e.compute(Math.min(1d, (double)(i + refreshTime) / time));
            animationSteps.add(transition.createStep(factor));
        }


        // Create the necessary amount of future data before applying the steps
        futureDataQueue.getOrAdd(
            shift + animationSteps.size() - 1,
            () -> {
                return futureDataQueue.isEmpty() ?
                __generateInterpolatedData() :
                __generateInterpolatedData(futureDataQueue.size() - 1);
            }
        );


        // Update existing future data
        int j = 0;
        for(; j < animationSteps.size(); ++j) {
            futureDataQueue.get(j + shift).apply(animationSteps.get(j));
        }

        // If the amount of future data is larger than the amount of steps, apply the last step to the remaining data
        final TransitionStep lastStep = animationSteps.get(animationSteps.size() - 1);
        for(; j + shift < futureDataQueue.size(); ++j) {
            futureDataQueue.get(j + shift).apply(lastStep);
        }


        // Return transition width
        return animationSteps.size();
    }




    /**
     * Helper function.
     * <p>
     * Applies a single future data to the element.
     * @param d The future data value.
     */
    protected void __applyTransitionStep(final @NotNull InterpolatedData d) {
        assert Require.nonNull(d, "interpolated data");
        if(d.hasTransform()) { style.setTransform(d.getTransform()); }
    }


    /**
     * Helper function.
     * <p>
     * Generates a base future data from the current values of the element.
     * @return The generated future data.
     */
    protected @NotNull InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            style.getTransform().copy(),
            null,
            null,
            null
        );
    }
    /**
     * Helper function.
     * <p>
     * Generates a base future data from the values stored in an element of the future data queue.
     * @param index The index of the element to read values from.
     * @return The generated future data.
     */
    protected @NotNull InterpolatedData __generateInterpolatedData(final int index) {
        return new InterpolatedData(
            futureDataQueue.get(index).getTransform().copy(),
            null,
            null,
            null
        );
    }




    /**
     * Retrieves a copy of the last transform queued in the future data queue.
     * @return The last transform in the future data queue, or the current transform if the queue is empty.
     */
    public @NotNull Transform genLastTransform() {
        return futureDataQueue.isEmpty() ? getStyle().getTransform().copy() : futureDataQueue.peekLast().getTransform().copy();
    }








    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        if(!isSpawned) {

            // Force despawn finalization if the element is currently waiting for the despawn animation to end
            if(despawnFinalizerTaskHandler != null) {
                despawnFinalizerTaskHandler.compute();
            }


            // Denormalize transform
            if(canvas != null && isTransformNormalized) {
                canvas.denormalizeTransform(this);
                isTransformNormalized = false;
            }


            // Handle primer and spawn animations
            final Animation primerAnimation = style.getPrimerAnimation();
            if(primerAnimation != null) {
                applyAnimationNow(primerAnimation);
            }
            final Animation spawnAnimation = style.getSpawnAnimation();
            if(spawnAnimation != null) {
                if(animate) applyAnimation(spawnAnimation);
                else applyAnimationNow(spawnAnimation);
            }


            // Renew the entity if needed, then prepare the entity, flush style data and spawn it
            entity.renewEntity(level);
            prepareEntityForSpawn(pos);
            getStyle().flagAll();
            flushStyle();
            entity.spawn(level, pos);


            // Set the tracking custom name and call Div's spawn method
            //! Name must be set after spawning as entities that load in with the tracking name are purged
            entity.setCustomNameVisible(false);
            entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
            super.spawn(pos, animate);
        }
    }


    /**
     * A method called right after the old entity is replaced (if one exists), but before the new one is flushed and spawned.
     * <p>
     * This allows subclasses to set custom properties and NBTs before the element becomes visible on the client,
     * which is especially useful for setting constant properties that only need to be initialized before spawn.
     * <p>
     * Overriding this method differs from adding logic to {@link #spawn(Vector3d, boolean)} because it correctly targets the new entity during respawns.
     * Adding logic to {@link #spawn(Vector3d, boolean)} alone would incorrectly target the old entity instead.
     * @param pos The position this element is going to be spawned at.
     */
    protected abstract void prepareEntityForSpawn(final @NotNull Vector3d pos);








    @Override
    public void despawn(final boolean animate) {
        if(isSpawned) {

            // Call superclass spawn
            super.despawn(animate);
            assert Require.nonNull(style, "style");


            // Handle animations
            final Animation despawnAnimation = style.getDespawnAnimation();
            if(despawnAnimation != null) {
                if(animate) {

                    // Delay despawn finalization to allow the despawn animation to complete
                    applyAnimation(despawnAnimation);
                    despawnFinalizerTaskHandler = Scheduler.schedule(despawnAnimation.getTotalDuration(), this::finalizeDespawn);
                }
                else {
                    applyAnimationNow(despawnAnimation);
                    finalizeDespawn();
                }
            }


            // Skip animations if they are null
            else {
                finalizeDespawn();
            }
        }
    }




    /**
     * A helper method that finalizes the despawning process.
     * <p>
     * It resets the tracking name, normalizes the transform and removes the entities from the level.
     */
    public void finalizeDespawn() {
        assert Require.nonNull(style, "style");
        assert Require.nonNull(entity, "entity");


        // Start inverse primer animation
        final Animation inversePrimerAnimation = style.getInversePrimerAnimation();
        if(inversePrimerAnimation != null) {
            applyAnimationNow(inversePrimerAnimation);
        }


        // Remove tracking custom name. This lets the entity respawn freely without getting purged.
        entity.setCustomName(new Txt("removed").get());
        if(canvas != null && !isTransformNormalized) {
            canvas.normalizeTransform(this);
            isTransformNormalized = true;
        }


        // Clear remaining animation steps and queue state
        if(!futureDataQueue.isEmpty()) {
            __applyTransitionStep(futureDataQueue.getLast());
            futureDataQueue.clear();
        }
        elmUpdateQueue.remove(this);
        isQueued = false;


        // Despawn the entity
        entity.despawn();
    }








    /**
     * Processes the first step of the scheduled transitions of this Elm.
     * @return false if the element has been removed from the update queue, true otherwise.
     */
    protected boolean stepTransition() {
        assert Require.nonNull(entity, "entity");

        // Apply step and update the entity
        if(!futureDataQueue.isEmpty()) {
            __applyTransitionStep(futureDataQueue.removeFirst());
        }
        flushStyle();
        entity.setInterpolationDuration(Configs.getPerf().animation_refresh_time.getValue());
        entity.setStartInterpolation();


        // Remove the element from the update queue if no steps are left and linger ticks have ran out
        if(futureDataQueue.isEmpty()) {
            if(queueLingerTicks > 0) {
                --queueLingerTicks;
            }
            else {
                elmUpdateQueue.remove(this);
                isQueued = false;
            }
            return false;
        }
        return true;
    }




    /**
     * Processes the first step of the scheduled transitions of all the queued elements.
     * <p>
     * Must be called at the end of the tick every {@code Configs.getPerf().animation_refresh_time} ticks.
     */
    public static void processUpdateQueue() {
        for(int i = 0; i < elmUpdateQueue.size();) {
            if(elmUpdateQueue.get(i).stepTransition()) ++i;
        }
    }







    @Override
    public @Nullable Vector2f checkIntersection(final @NotNull Player player, final boolean calculateIntersectionCoords) {
        assert Require.nonNull(player, "player");
        if(style.getBillboardMode() != BillboardConstraints.FIXED) return null;
        return super.checkIntersection(player, calculateIntersectionCoords);
    }




    @Override
    public double getIntersectionLength(final @NotNull Player player) {
        assert Require.nonNull(player, "player");
        if(style.getBillboardMode() != BillboardConstraints.FIXED) return Double.MAX_VALUE;
        return super.getIntersectionLength(player);
    }








    /**
     * Checks for stray displays and purges them.
     * <p>
     * Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(final @NotNull Entity entity) {
        assert Require.nonNull(entity, "entity");
        if(entity instanceof Display) {
            if(
                entity.level() != null &&
                entity.hasCustomName() &&
                entity.getCustomName().getString().equals(ENTITY_CUSTOM_NAME)
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }
}