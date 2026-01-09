package com.snek.frameworklib.graphics.basic.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;

import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.data_types.animations.InterpolatedData;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.displays.CustomDisplay;
import com.snek.frameworklib.data_types.displays.CustomTextDisplay;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.styles.FancyTextElmStyle;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display.BillboardConstraints;








/**
 * A text element that also has a configurable, animatable background color.
 */
public non-sealed class FancyTextElm extends __base_TextElm {

    // In-world data
    private final @NotNull CustomDisplay text;
    public @NotNull CustomTextDisplay getFgEntity()  {
        assert Require.nonNull(text, "foreground entity");
        assert Require.instanceOf(text, CustomTextDisplay.class, "foreground entity");
        return (CustomTextDisplay)text;
    }
    public @NotNull CustomTextDisplay getBgEntity()  {
        assert Require.nonNull(getEntity(), "background entity");
        assert Require.instanceOf(getEntity(), CustomTextDisplay.class, "background entity");
        return getEntity(CustomTextDisplay.class);
    }
    private @NotNull FancyTextElmStyle getThisStyle() {
        assert Require.nonNull(getStyle(), "style");
        assert Require.instanceOf(getStyle(), FancyTextElmStyle.class, "style");
        return getStyle(FancyTextElmStyle.class);
    }

    @Override
    public @NotNull CustomTextDisplay getTextDisplay() {
        return getFgEntity();
    }











    /**
     * Creates a new FancyTextElm using a custom style.
     * @param level The level in which to place the element.
     * @param style The custom style.
     */
    public FancyTextElm(final @NotNull ServerLevel level, final @NotNull FancyTextElmStyle style) {
        super(level, new CustomTextDisplay(level), style);
        text = new CustomTextDisplay(level);
    }


    /**
     * Creates a new FancyTextElm using the default style.
     * @param level The level in which to place the element.
     */
    public FancyTextElm(final @NotNull ServerLevel level) {
        this(level, new FancyTextElmStyle());
    }








    /**
     * Helper function. Calculates the final transformation that is applied to the foreground entity.
     * <p>
     * This is the transform used by the foreground element.
     * <p>
     * The shared transform is returned by {@link #__calcTransform()}.
     * @param initialTransform The value to start from.
     * @return The final transformation.
     */
    public @NotNull Transform __calcTransformFg(final @NotNull Transform initialTransform) {
        assert Require.nonNull(initialTransform, "initial transform");
        return
            initialTransform.copy()
            .apply(getThisStyle().getTransformFg())                                     // Apply specified foreground transform
            .scale(getThisStyle().getFontSize() * SimpleTextElmStyle.TEXT_FONT_FACTOR)  // Scale to font size
            .moveZ((getZIndex() + 1) * Configs.getGraphics().z_layer_spacing.getValue())
            //!^ Add 1 Z-layer to the forground entity so it's not covered by the background
        ;
    }

    /**
     * Helper function. Calculates the final transformation that is applied to the background entity.
     * <p>
     * This is the transform used by the background element.
     * <p>
     * The shared transform is returned by {@link #__calcTransform()}.
     * @param initialTransform The value to start from.
     * @return The final transformation.
     */
    public @NotNull Transform __calcTransformBg(final @NotNull Transform initialTransform) {
        assert Require.nonNull(initialTransform, "initial transform");
        return
            initialTransform.copy()
            .apply(getThisStyle().getTransformBg())
            .scaleX(PanelElm.ENTITY_BLOCK_RATIO_X * getAbsSize().x)
            .scaleY(PanelElm.ENTITY_BLOCK_RATIO_Y * getAbsSize().y)
            .move(new Vector3f(PanelElm.ENTITY_SHIFT_X * getAbsSize().x, 0, 0).rotate(initialTransform.getRot()))
        ;
    }








    @Override
    public void flushStyle() {

        // Alias entities
        final @NotNull CustomTextDisplay fg = getFgEntity();
        final @NotNull CustomTextDisplay bg = getBgEntity();


        // Handle text first
        //! Must be computed before the transform as it depends on i
        { final Flagged<Component> f = getThisStyle().getFlaggedText();
        if(f.isFlagged()) {
            updateTotTextSizeCache();
            //! ^ This takes into account the style's text, not the actual text in the entity
            //! The entity's text is set by updateOverflowBehaviour based on the specified overflow behaviour
            updateVisualTextSizeCache();
            updateOverflowBehaviour();
            f.unflag();
        }}


        // Handle overflow behaviour
        //! Must be computed before transforms as it changes the style's text, which changes the visual width of the entity
        { final Flagged<TextOverflowBehaviour> f = getThisStyle().getFlaggedTextOverflowBehaviour();
        if(f.isFlagged()) {
            updateOverflowBehaviour();
            f.unflag();
        }}


        // Handle transforms
        {
            final Flagged<Transform>     f  = getThisStyle().getFlaggedTransform();
            final Flagged<TextAlignment> fa = getThisStyle().getFlaggedTextAlignment();
            final Flagged<Component>     ft = getThisStyle().getFlaggedText();
            final Flagged<Integer>       fs = getThisStyle().getFlaggedFontSize();
            final Flagged<Transform>     fFg = getThisStyle().getFlaggedTransformFg();
            final Flagged<Transform>     fBg = getThisStyle().getFlaggedTransformBg();
            final boolean fgNeedsUpdate = f.isFlagged() || fFg.isFlagged() || fa.isFlagged() || ft.isFlagged() || fs.isFlagged();
            final boolean bgNeedsUpdate = f.isFlagged() || fBg.isFlagged();
            if(f.isFlagged()) f.unflag();


            // Calculate superclass transform if needed
            if(fgNeedsUpdate || bgNeedsUpdate) {
                final Transform t = __calcTransform();

                // Update foreground transform if necessary
                if(fgNeedsUpdate) {
                    final Transform tFg = __calcTransformFg(t);
                    if(fa.get() == TextAlignment.LEFT ) tFg.moveX(-(getAbsSize().x - calcVisualEntityWidth()) / 2f);
                    if(fa.get() == TextAlignment.RIGHT) tFg.moveX(+(getAbsSize().x - calcVisualEntityWidth()) / 2f);
                    tFg.moveY((getAbsSize().y - calcTotEntityHeightWithMargins()) / 2f);
                    fg.setTransformation(tFg.moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
                    fFg.unflag();
                }

                // Update background transform if necessary
                if(bgNeedsUpdate) {
                    bg.setTransformation(__calcTransformBg(t).moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
                    fBg.unflag();
                }
            }
        }


        // Handle the other Elm values normally, applying them to both entities
        { final Flagged<Float> f = getThisStyle().getFlaggedViewRange();
        if(f.isFlagged()) {
            fg.setViewRange(f.get());
            bg.setViewRange(f.get());
            f.unflag();
        }}
        { final Flagged<BillboardConstraints> f = getThisStyle().getFlaggedBillboardMode();
        if(f.isFlagged()) {
            fg.setBillboardMode(f.get());
            bg.setBillboardMode(f.get());
            f.unflag();
        }}


        // Handle TextElm values
        { final Flagged<Integer> f = getThisStyle().getFlaggedTextOpacity();
            //! Opacity is updated every time to keep the opacity cache moving
            fg.setTextOpacity(f.get());
            f.unflag();
        }
        { final Flagged<TextAlignment> f = getThisStyle().getFlaggedTextAlignment();
        if(f.isFlagged()) {
            fg.setTextAlignment(f.get());
            f.unflag();
        }}
        {
            final Flagged<Vector3i> fc = getThisStyle().getFlaggedBgColor();
            final Flagged<Integer>  fa = getThisStyle().getFlaggedBgAlpha();
            if(fc.isFlagged() || fa.isFlagged()) {
                final Vector3i color = fc.get();
                bg.setBackground(new Vector4i(fa.get(), color.x, color.y, color.z));
                fa.unflag();
                fc.unflag();
            }
        }


        // Transform, view range and billboard mode are already unflagged
        super.flushStyle();
    }




    @Override
    public int getLayerCount() {
        return 2;
    }




    @Override
    protected void __applyTransitionStep(final @NotNull InterpolatedData d) {
        super.__applyTransitionStep(d);
        if(d.hasOpacity    ()) { getThisStyle().setTextOpacity(d.getOpacity    ()); }
        if(d.hasBgAlpha    ()) { getThisStyle().setBgAlpha    (d.getBgAlpha    ()); }
        if(d.hasBgColor    ()) { getThisStyle().setBgColor    (d.getBgColor    ()); }
        if(d.hasTransformFg()) { getThisStyle().setTransformFg(d.getTransformFg()); }
        if(d.hasTransformBg()) { getThisStyle().setTransformBg(d.getTransformBg()); }
    }




    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            getThisStyle().getTransform().copy(),
            new Vector3i(getThisStyle().getBgColor()),
            getThisStyle().getBgAlpha(),
            getThisStyle().getTextOpacity(),
            getThisStyle().getTransformFg().copy(),
            getThisStyle().getTransformBg().copy()
        );
    }
    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData(final int index) {
        final InterpolatedData fd = futureDataQueue.get(index);
        return new InterpolatedData(
            fd.getTransform().copy(),
            new Vector3i(fd.getBgColor()),
            fd.getBgAlpha(),
            fd.getOpacity(),
            fd.getTransformFg().copy(),
            fd.getTransformBg().copy()
        );
    }




    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {

        // Call superclass spawn (which also spawns background entity)
        super.spawn(pos, animate);

        // Spawn foreground entity
        getFgEntity().spawn(level, pos);

        // Set tracking custom name to foreground entity
        //! Name must be set after spawning as entities that load in with the tracking name are purged
        getFgEntity().setCustomNameVisible(false);
        getFgEntity().setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
    }
    @Override
    protected void prepareEntityForSpawn(final @NotNull Vector3d pos) {
        getBgEntity().setText(new Txt().get());
        getFgEntity().setBackground(new Vector4i(0, 0, 0, 0));
        getFgEntity().setLineWidth(Integer.MAX_VALUE);
    }




    @Override
    public void despawn(final boolean animate) {
        super.despawn(animate);
        getFgEntity().despawn();
    }




    @Override
    public void stepTransition() {

        // Flush style of the foreground entity and start a new minecraft interpolation
        //! This is the same operation that's in super.stepTransition(), but for the foreground entity
        if(!futureDataQueue.isEmpty()) {
            flushStyle();
            getFgEntity().setInterpolationDuration(Configs.getPerf().animation_refresh_time.getValue());
            getFgEntity().setStartInterpolation();

            // Tick foreground entity.
            //! Text displays need to be ticked to keep the opacity calculations consistent
            //! Same as SimpleTextElm
            getFgEntity().tick();
        }

        // Call superclass's stepTransition
        super.stepTransition();
    }
}