package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.graphics.core.elements.CanvasTitle;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;















/**
 * The base class for canvases.
 * <p>
 * A canvas is the main container for graphic elements.
 * It provides a background element, a top and bottom border, a back panel and support for simple toolbars.
 * <p>
 * Each menu screen should have its own canvas.
 * <p>
 * This class is sealed as {@link HudCanvas} and {@link UiCanvas} are the only possible types of canvases.
 * Specialized types must inherit from either of them.
 */
public abstract sealed class Canvas extends Div permits UiCanvas, HudCanvas {

    // Default layout
    public static final float TITLE_H                = 0.1f;
    public static final float TITLE_W                = 0.9f;
    public static final float TOOLBAR_H              = 0.12f;
    public static final float TOOLBAR_BUTTON_SPACING = 0.04f;
    public static final float TOOLBAR_BUTTON_SHIFT   = TOOLBAR_H + TOOLBAR_BUTTON_SPACING;




    // Core data
    protected final @NotNull Context context;

    // Animation
    public static final int SPAWN_SIZE_TIME      = 8;
    public static final int CANVAS_ROTATION_TIME = 8;




    // Elements
    protected final @Nullable CanvasTitle title;

    // Inherited elements
    protected final @NotNull PanelElm bg;
    protected final @NotNull PanelElm back;
    protected final @NotNull PanelElm top;
    protected final @NotNull PanelElm bottom;

    // Getters
    public @NotNull Elm     getBg       () { assert Require.nonNull(bg,      "bg");      return bg;                 }
    public @NotNull Elm     getBack     () { assert Require.nonNull(back,    "back");    return back;               }
    public @NotNull Elm     getTop      () { assert Require.nonNull(top,     "top");     return top;                }
    public @NotNull Elm     getBottom   () { assert Require.nonNull(bottom,  "bottom");  return bottom;             }
    public @NotNull Context getContext  () { assert Require.nonNull(context, "context"); return context;            }
    public @NotNull ServerLevel getLevel() { assert Require.nonNull(context, "context"); return context.getLevel(); }

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
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected Canvas(final @NotNull Context context, final @Nullable Component defaultTitle, final float height, final float heightTop, final float heightBottom) {
        assert Require.nonNull(context, "context");
        assert Require.nonNegative(height, "background height");
        assert Require.nonNegative(heightTop, "top element height");
        assert Require.nonNegative(heightBottom, "bottom element height");

        // Set basic data
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
        final @NotNull ServerLevel level = context.getLevel();




        // If the elements don't exist yet
        if(prevCanvas == null) {

            // Initialize last rotation
            context.setRotation(context.calcRot());


            // Create the elements
            bg     = (PanelElm)addChild(createNewBgElement    (level));
            back   = (PanelElm)addChild(createNewBackElement  (level));
            top    = (PanelElm)addChild(createNewTopElement   (level));
            bottom = (PanelElm)addChild(createNewBottomElement(level));


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
            bg    .applyAnimation(new Transition().additiveTransform(transformBg),     false, false);
            back  .applyAnimation(new Transition().additiveTransform(transformBack),   false, false);
            top   .applyAnimation(new Transition().additiveTransform(transformTop),    false, false);
            bottom.applyAnimation(new Transition().additiveTransform(transformBottom), false, false);
        }




        // If the elements already exist
        else {

            // Instantly despawn and remove previous children of the background element
            for(final Div c : prevCanvas.getBg().getChildren()) c.despawn(false);
            prevCanvas.getBg().clearChildren();


            // Inherit the elements
            bg     = (PanelElm)addChild(prevCanvas.getBg());
            back   = (PanelElm)addChild(prevCanvas.getBack());
            top    = (PanelElm)addChild(prevCanvas.getTop());
            bottom = (PanelElm)addChild(prevCanvas.getBottom());


            // Animate them to match the specified visual dimensions
            final Transform transformBg     = new Transform().scaleY(newHeightBg     / prevCanvas.newHeightBg    ).moveY(newPosBg     - prevCanvas.newPosBg    );
            final Transform transformBack   = new Transform().scaleY(newHeightBack   / prevCanvas.newHeightBack  ).moveY(newPosBack   - prevCanvas.newPosBack  );
            final Transform transformTop    = new Transform().scaleY(newHeightTop    / prevCanvas.newHeightTop   ).moveY(newPosTop    - prevCanvas.newPosTop   );
            final Transform transformBottom = new Transform().scaleY(newHeightBottom / prevCanvas.newHeightBottom).moveY(newPosBottom - prevCanvas.newPosBottom);
            bg    .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBg),     false, true);
            back  .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBack),   false, true);
            top   .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformTop),    false, true);
            bottom.applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBottom), false, true);
        }




        // Add title element
        if(defaultTitle != null) {
            final Div e = bg.addChild(new CanvasTitle(level, defaultTitle));
            e.setSize(new Vector2f(TITLE_W, TITLE_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);
            title = (CanvasTitle)e;
        }
        else {
            title = null;
        }
    }


    /**
     * Creates a new Canvas.
     * @param context The context to assign this canvas to.
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected Canvas(final @NotNull Context context, final @Nullable String defaultTitle, final float height, final float heightTop, final float heightBottom) {
        this(context, defaultTitle == null ? null : new Txt(defaultTitle).white().bold().get(), height, heightTop, heightBottom);
    }




    /**
     * Updates the text displayed in the title element, if one exists.
     * @param titleText The new text to display.
     */
    public void updateTitle(final @NotNull Component titleText) {
        if(title != null) {
            title.updateDisplay(titleText);
        }
    }


    /**
     * Updates the text displayed in the title element, if one exists.
     * @param titleText The new text to display.
     */
    public void updateTitle(final @NotNull String titleText) {
        updateTitle(new Txt(titleText).white().get());
    }


    /**
     * Places the provided buttons on the toolbar, calculating their size and position automatically.
     * <p>
     * This method is meant to be called by the constructor.
     * @param buttons The buttons to place. Must contain at least 1 element.
     */
    protected void setToolbarButtons(final @NotNull Div[] buttons) {
        assert Require.notEmpty(buttons, "buttons");

        for(int i = 0; i < buttons.length; ++i) {
            final Div e = bg.addChild(buttons[i]);
            e.setSize(new Vector2f(TOOLBAR_H));
            e.setPosX(TOOLBAR_BUTTON_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        }
    }








    /**
     * Updates the rotation of the canvas.
     * @param from The current rotation.
     * @param to The target rotation.
     * @param animate Whether the rotation should be animated or instantaneous.
     */
    protected void rotate(final int from, final int to, final boolean animate) {
        assert Require.inRange(from, 0, 7, "starting rotation");
        assert Require.inRange(to,   0, 7, "target rotation");

        final Animation animation = calcCanvasRotationAnimation(from, to);
        applyAnimation(animation, true, animate, true);
    }



    /**
     * Denormalizes the transform of the element.
     * <p>
     * This means applying the translations and rotations needed to align it with the canvas's current position and orientation.
     * @param elm The element to modify.
     */
    public void denormalizeTransform(final @NotNull Div elm) {
        assert Require.nonNull(elm, "element");
        elm.applyAnimation(calcCanvasRotationAnimation(0, context.getRotation()), false, false);
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
        assert Require.nonNull(elm, "element");
        elm.applyAnimation(calcCanvasRotationAnimation(context.getRotation(), 0), false, false);
    }




    /**
     * Calculates the animations required to face from a specified direction to another one.
     * @param from The starting direction. 0 to 7.
     * @param to The new direction to face. 0 to 7.
     * @return The canvas animation.
     */
    public static @NotNull Animation calcCanvasRotationAnimation(final int from, final int to) {
        assert Require.inRange(from, 0, 7, "starting rotation");
        assert Require.inRange(to,   0, 7, "target rotation");

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
        assert Require.inRange(from, 0, 7, "starting rotation");
        assert Require.inRange(to,   0, 7, "target rotation");

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




    public PanelElm createNewBgElement    (final @NotNull ServerLevel level) { return new PanelElm(level, new PanelElmStyle()); }
    public PanelElm createNewBackElement  (final @NotNull ServerLevel level) { return new PanelElm(level, new PanelElmStyle()); }
    public PanelElm createNewTopElement   (final @NotNull ServerLevel level) { return new CanvasBorder(level); } //TODO remove CanvasBorder element. move default height to Canvas
    public PanelElm createNewBottomElement(final @NotNull ServerLevel level) { return new CanvasBorder(level); }
}
