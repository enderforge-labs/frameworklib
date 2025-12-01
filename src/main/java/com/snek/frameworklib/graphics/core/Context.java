package com.snek.frameworklib.graphics.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.interfaces.Scrollable;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.input.HoverReceiver;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
















/**
 * The base class for contexts.
 * <p>
 * A context is the main container for any graphic interface.
 * It contains canvases, which contain elements.
 * <p>
 * This is sealed as HudContext and UiContext are the only possible types of contexts.
 * Specialized types must inherit from either of them.
 */
public abstract sealed class Context permits HudContext, UiContext {

    // Active Context list
    private static final Map<Player, LinkedList<Context>> activeContexts = new HashMap<>();
    public static Map<Player, LinkedList<Context>> getActiveContexts() { return activeContexts; }

    // Hud data
    protected final Player player;
    protected @Nullable InteractionBlocker interactionBlocker = null;
    protected @Nullable Canvas activeCanvas = null;
    protected boolean spawned = false;

    // Getters
    public @NotNull  Player             getPlayer                () { return player; }
    public @Nullable Canvas             getActiveCanvas          () { return activeCanvas; }
    public @Nullable InteractionBlocker getInteractionBlocker    () { return interactionBlocker; }
    public abstract float getInteractionBlockerSize();
    public abstract @NotNull Vector3d getSpawnPos();

    // Optimization structures
    private @Nullable Elm targetedElm = null;
    public @Nullable Elm getTargetedElm() { return targetedElm; }








    /**
     * Creates a new context for the specified player.
     * @param player The owner of the context.
     */
    protected Context(final @NotNull Player player) {
        this.player = player;
    }


    /**
     * Spawns the context at the provided coordinates.
     * @param pos The coordinates to spawn the context at.
     *     This defines the the center of the context's hitbox.
     */
    public void spawn(final @NotNull Vector3d pos) {
        if(!spawned) {
            spawned = true;
            final float size = getInteractionBlockerSize();
            if(activeCanvas != null) activeCanvas.spawn(pos);
            interactionBlocker = new InteractionBlocker(player.level(), size, size);
            interactionBlocker.spawn(pos);

            // Update context list
            activeContexts.putIfAbsent(player, new LinkedList<>());
            final @NotNull LinkedList<Context> contexts = activeContexts.get(player);
            contexts.add(this);
        }
    }




    /**
     * Despawns the context and all of its graphic elements.
     */
    public void despawn(final boolean animate) {
        if(spawned) {
            spawned = false;
            if(activeCanvas != null) activeCanvas.despawn(animate);
            interactionBlocker.despawn();
            interactionBlocker = null;

            // Update context list
            final @Nullable LinkedList<Context> contexts = activeContexts.get(player);
            contexts.remove(this);
            if(contexts.isEmpty()) activeContexts.remove(player);

            // Force-update targeted context for the player
            HoverReceiver.updateTargetedContext(player);
        }
    }




    /**
     * Updates the active canvas.
     */
    public void update() {
        if(activeCanvas != null) activeCanvas.update();
    }




    public abstract void changeCanvas(final @NotNull Canvas canvas);
    protected final void finalizeCanvasChange(final @NotNull Canvas newCanvas, final @NotNull Vector3d canvasSpawnPos) {
        final int lastRotation = activeCanvas != null ? activeCanvas.getRotation() : 0;


        // Set new active canvas and spawn canvas into the world
        activeCanvas = newCanvas;
        newCanvas.spawn(canvasSpawnPos);


        // Adjust rotation of child elements if needed
        //! newCanvas.lastRotation is updated when the canvas is created. In Canvas.Canvas
        if(lastRotation != 0) {
            final Animation animation = Canvas.calcCanvasRotationAnimation(0, lastRotation);
            for(final Div c : newCanvas.getBg().getChildren()) {
                c.applyAnimationNowRecursive(animation);
            }
        }
    }







    /**
     * Closes all the contexts of the specified player.
     * <p>
     * Does nothing if the player doesn't have any context open.
     * @param player The player.
     */
    public static void closeContexts(final @NotNull Player player) {
        final LinkedList<Context> contexts = getActiveContexts().get(player);
        if(contexts != null) for(final Context context : contexts) {
            context.despawn(true);
        }
    }




    /**
     * Updates all active contexts of every player.
     */
    public static void updateActiveContexts() {
        for(final LinkedList<Context> contexts : getActiveContexts().values()) {
            for(final Context context : contexts) {
                context.update();
            }
        }
    }




    /**
     * Forwards a click event to this context.
     * @param player The player.
     * @param action The type of click.
     * @return True if the context consumed the click, false otherwise.
     */
    public boolean forwardClick(final @NotNull Player player, final @NotNull ClickAction action) {
        if(activeCanvas == null) return false;

        final boolean r = activeCanvas.forwardClick(player, action);
        if(r && activeCanvas instanceof HudCanvas hud) {
            hud.resetInactivityTimer();
        }
        return r;
    }




    /**
     * Forwards a scroll event to this context.
     * @param player The player.
     * @param action The amount of scroll.
     */
    public void forwardScroll(final @NotNull Player player, final float scrollAmount) {
        if(activeCanvas == null) return;

        if(targetedElm != null && targetedElm instanceof Scrollable s) {
            s.onScroll(player, scrollAmount);
        }
    }




    /**
     * Forwards a hover event to this context.
     * @param player The player.
     */
    public void forwardHover(final @NotNull Player player) {
        final Canvas canvas = activeCanvas;
        if(canvas == null) return;

        // If a targeted element is present, update its hover state
        if(targetedElm != null) {
            targetedElm.updateHoverState(player);

            // If it's no longer being hovered on, check if a new element is being hovered
            if(!targetedElm.isHovered()) {
                targetedElm = canvas.findTargetedElement(player);

                // If said element exists, send an update to it
                if(targetedElm != null) {
                    targetedElm.updateHoverState(player);
                }
            }
        }

        // If a targeted element is not present, check if a new element is being hovered
        else {
            targetedElm = canvas.findTargetedElement(player);

            // If said element exists, send an update to it
            if(targetedElm != null) targetedElm.updateHoverState(player);
        }
    }




    /**
     * Finds the top-most context from the player's point of view.
     * <p>
     * More specifically, this is the context that's above all other contexts when projected onto the player's view.
     * This only includes contexts that are directly under the crossair.
     * <p>
     * This method correctly handles intersecting contexts at any orientation.
     * <p>
     * Notice: The Z-Index of the individual elements contained by the context is not taken into account.
     * Abnormally high Z-Layer values can result in noticibly incorrect context detection.
     * @param player The player.
     * @return The top-most context, or null if the player isn't directly looking at any context.
     */
    public static @Nullable Context findTopMostContext(final Player player) {

        // Get all contexts
        LinkedList<Context> contexts = getActiveContexts().get(player);
        if(contexts == null) return null;

        // Find top-most context
        @Nullable Context topMost = null;
        double bestDistance = Double.MAX_VALUE;
        for(final @Nullable Context c : contexts) {
            if(c.activeCanvas == null) continue;
            final double distance = c.activeCanvas.getBg().getIntersectionLength(player);
            if(distance > 0 && distance < bestDistance) {
                bestDistance = distance;
                topMost = c;
            }
        }

        // Return found context
        return topMost;
    }
}


//FIXME instantly despawn the context when players disconnect or die or are dying or go in spectator mode or change dimension
//FIXME instantly despawn the context when players disconnect or die or are dying or go in spectator mode or change dimension