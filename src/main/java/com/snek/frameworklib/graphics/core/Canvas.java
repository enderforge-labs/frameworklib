package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.server.level.ServerLevel;
















/**
 * The base class for canvases.
 * <p>
 * A canvas is the main container for graphic elements.
 * Each menu screen should have its own canvas.
 * <p>
 * This is sealed as {@link HudCanvas} and {@link UiCanvas} are the only possible types of canvases.
 * Specialized types must inherit from either of them.
 */
public abstract sealed class Canvas extends Div permits UiCanvas, HudCanvas {


    // Core data
    protected final @NotNull Context context;
    protected int lastRotation = 0;

    // Colors
    public static final @NotNull Vector3i TOOLBAR_FG_COLOR = new Vector3i(Txt.COLOR_WHITE);
    public static final          int      TOOLBAR_FG_ALPHA = 255;

    // Layout
    public static final int   SPAWN_SIZE_TIME    = 8;
    public static final float SQUARE_BUTTON_SIZE = 0.12f;

    public static final float BOTTOM_ROW_SPACING      = 0.04f;
    public static final float BOTTOM_ROW_SHIFT        = SQUARE_BUTTON_SIZE + BOTTOM_ROW_SPACING;
    public static final float BOTTOM_ROW_CONTENT_SIZE = 0.6f;
    public static final float TOOLBAR_FG_WIDTH        = 0.15f;

    // Animation
    public static final int CANVAS_ROTATION_TIME = 8;




    // Inherited elements
    protected final @NotNull Elm bg;
    protected final @NotNull Elm back;
    protected final @NotNull Elm top;
    protected final @NotNull Elm bottom;

    // Getters
    public @NotNull Elm     getBg      () { return bg;           }
    public @NotNull Elm     getBack    () { return back;         }
    public @NotNull Elm     getTop     () { return top;          }
    public @NotNull Elm     getBottom  () { return bottom;       }
    public @NotNull int     getRotation() { return lastRotation; }
    public @NotNull Context getContext () { return context;      }

    // Height cache
    private final float newHeightBg;
    private final float newHeightBack;
    private final float newHeightTop;
    private final float newHeightBottom;

    // Position cache
    private final float newPosBg;
    private final float newPosBack;
    private final float newPosTop;
    private final float newPosBottom;






    /**
     * Creates a new Canvas.
     * @param context The context to assign this canvas to.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     * @param bgStyle The style of the default background element.
     * @param bgStyle The style of the default back panel element.
     */
    protected Canvas(
        final @NotNull Context context,
        final float height, final float heightTop, final float heightBottom,
        final @Nullable PanelElmStyle bgStyle, final @Nullable PanelElmStyle backStyle
    ) {
        this.context = context;
        canvas = this;
        setSize(new Vector2f(1f, 1f));

        // Calculate new heights
        newHeightBg     = height - heightTop - heightBottom;
        newHeightBack   = height;
        newHeightTop    = heightTop;
        newHeightBottom = heightBottom;

        // Calculate new positions
        newPosBg     = 1 - height + heightBottom;
        newPosBack   = 1 - height;
        newPosTop    = 1 - heightTop;
        newPosBottom = 1 - height;

        // Cache data for element transfer/creation
        final @Nullable Canvas prevCanvas = context.getActiveCanvas();
        final @NotNull ServerLevel world = (ServerLevel)context.getPlayer().level();




        // If the elements don't exist yet
        if(prevCanvas == null) {
            lastRotation = calcRot();


            // Create the elements
            bg     = (Elm)addChild(new PanelElm(world, bgStyle   == null ? new PanelElmStyle() : bgStyle));
            back   = (Elm)addChild(new PanelElm(world, backStyle == null ? new PanelElmStyle() : backStyle));
            top    = (Elm)addChild(new CanvasBorder(world));
            bottom = (Elm)addChild(new CanvasBorder(world));


            // Set their size, position and alignments
            bg    .setSize(new Vector2f(1f, 1f));
            bg    .setAlignmentX(AlignmentX.CENTER);
            bg    .setAlignmentY(AlignmentY.BOTTOM);
            back  .setSize(new Vector2f(1f, 1f));
            back  .setAlignmentX(AlignmentX.CENTER);
            back  .setAlignmentY(AlignmentY.BOTTOM);
            top   .setSize(new Vector2f(1f, 1f));
            top   .setAlignmentX(AlignmentX.CENTER);
            top   .setAlignmentY(AlignmentY.BOTTOM);
            bottom.setSize(new Vector2f(1f, 1f));
            bottom.setAlignmentX(AlignmentX.CENTER);
            bottom.setAlignmentY(AlignmentY.TOP);


            // Set visual dimensions (and rotation for the back side)
            final Transform transformBg     = new Transform().scaleY(newHeightBg    ).moveY(newPosBg    );
            final Transform transformBack   = new Transform().scaleY(newHeightBack  ).moveY(newPosBack  ).rotY((float)Math.PI).moveX(1f);
            final Transform transformTop    = new Transform().scaleY(newHeightTop   ).moveY(newPosTop   );
            final Transform transformBottom = new Transform().scaleY(newHeightBottom).moveY(newPosBottom);
            bg    .applyAnimationNow(new Transition().additiveTransform(transformBg));
            back  .applyAnimationNow(new Transition().additiveTransform(transformBack));
            top   .applyAnimationNow(new Transition().additiveTransform(transformTop));
            bottom.applyAnimationNow(new Transition().additiveTransform(transformBottom));


            // lastRotation = prevCanvas.getRotation(); //TODO REMOVE
            // //! updateRot is called in the spawn method //TODO remove comment idk
            // //! This is to avoid initialization order issues //TODO remove comment idk
        }




        // If the elements already exist
        else {
            lastRotation = prevCanvas.getRotation();


            // Instantly despawn and remove previous children of the background element
            for(final Div c : prevCanvas.getBg().getChildren()) c.despawn(false);
            prevCanvas.getBg().clearChildren();


            // Inherit the elements
            bg     = (Elm)addChild(prevCanvas.getBg());
            back   = (Elm)addChild(prevCanvas.getBack());
            top    = (Elm)addChild(prevCanvas.getTop());
            bottom = (Elm)addChild(prevCanvas.getBottom());


            // Animate them to match the specified visual dimensions
            final Transform transformBg     = new Transform().scaleY(newHeightBg     / prevCanvas.newHeightBg    ).moveY(newPosBg     - prevCanvas.newPosBg    );
            final Transform transformBack   = new Transform().scaleY(newHeightBack   / prevCanvas.newHeightBack  ).moveY(newPosBack   - prevCanvas.newPosBack  );
            final Transform transformTop    = new Transform().scaleY(newHeightTop    / prevCanvas.newHeightTop   ).moveY(newPosTop    - prevCanvas.newPosTop   );
            final Transform transformBottom = new Transform().scaleY(newHeightBottom / prevCanvas.newHeightBottom).moveY(newPosBottom - prevCanvas.newPosBottom);
            bg    .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBg));
            back  .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBack));
            top   .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformTop));
            bottom.applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBottom));


            // // Rotate child elements to match the previous canvas's rotation (if neeeded) //TODO Remove
            // //! New elements are rotated in Context.finalizeCanvasChange //TODO remove comment idk
            // lastRotation = prevCanvas.getRotation(); //TODO Remove
        }
    }




    /**
     * Updates the canvas.
     */
    public abstract void update();




    /**
     * Updates the rotation of the canvas.
     * @param instant Whether the rotation should be instantaneous or animated.
     */
    protected void updateRot(final boolean instant) {
        final int newRot = calcRot();
        if(lastRotation != newRot) {
            final Animation animation = calcCanvasRotationAnimation(lastRotation, newRot);
            if(instant) applyAnimationNowRecursive(animation); else applyAnimationRecursive(animation); //TODO replace with a single applyAnimationRecursive
            lastRotation = newRot;
        }
    }
    public abstract int calcRot();




    /**
     * Denormalizes the transform of the element.
     * <p>
     * This means applying the translations and rotations needed to align it with the canvas's current position and orientation.
     * @param elm The element to modify.
     */
    public void denormalizeTransform(final @NotNull Div elm) {
        if(canvas instanceof HudCanvas hud) {
            elm.applyAnimationNow(new Transition().additiveTransform(new Transform().move(hud.__calcVisualShiftLocal())));
        }
        elm.applyAnimationNow(Canvas.calcCanvasRotationAnimation(0, canvas.getRotation()));
    }


    /**
     * Normalizes the transform of the element.
     * <p>
     * This means applying the translations and rotations needed to go from the canvas's current position and orientation to the default ones.
     * <p>
     * This method only applies the transform to the provided element. Not its children.
     * @param elm The element to modify.
     */
    public void normalizeTransform(final @NotNull Div elm) {
        if(canvas instanceof HudCanvas hud) {
            elm.applyAnimationNow(new Transition().additiveTransform(new Transform().move(hud.__calcVisualShiftLocal().mul(-1))));
        }
        elm.applyAnimationNow(Canvas.calcCanvasRotationAnimation(canvas.getRotation(), 0));
    }


    /**
     * Calculates the animations required to face from a specified direction to another one.
     * @param from The starting direction. 0 to 7.
     * @param to The new direction to face. 0 to 7.
     * @return The canvas animation.
     */
    public static @NotNull Animation calcCanvasRotationAnimation(final int from, final int to) {
        final float rotation = (float)(-Math.toRadians(to * 45f - from * 45f));
        return new Animation(
            new Transition(CANVAS_ROTATION_TIME, Easings.cubicOut)
            .additiveTransform(new Transform().rotGlobalY(rotation))
        );
    }


    /**
     * Calculates the animations required to face from a specified direction to another one, without rotating the item's model.
     * @param from The starting direction. 0 to 7.
     * @param to The new direction to face. 0 to 7.
     * @return The item display animation.
     */
    public static @NotNull Animation calcItemDisplayRotationAnimation(final int from, final int to) {
        final float rotation = (float)(-Math.toRadians(to * 45f - from * 45f));
        return new Animation(
            new Transition(CANVAS_ROTATION_TIME, Easings.cubicOut)
            .additiveTransform(new Transform().rotGlobalY(rotation).rotY(- rotation))
        );
    }








    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {

        // If the background is already spawned, only spawn its children
        if(bg.isSpawned()) for(final Div c : bg.getChildren()) {
            isSpawned = true;
            c.spawn(pos, animate);
        }

        // If not, spawn everything
        else {
            super.spawn(pos, animate);
        }
    }
}
