package com.snek.frameworklib.graphics.basic.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.frameworklib.data_types.animations.InterpolatedData;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.displays.CustomTextDisplay;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;








/**
 * An element that can display text.
 * <p>
 * This class has transparent background. For a text element with background color, use {@link FancyTextElm}.
 */
public non-sealed class SimpleTextElm extends __base_TextElm {

    // In-world data
    private @NotNull CustomTextDisplay  getThisEntity() {
        assert Require.nonNull(getEntity(), "entity");
        assert Require.instanceOf(getEntity(), CustomTextDisplay.class, "entity");
        return getEntity(CustomTextDisplay.class);
    }
    private @NotNull SimpleTextElmStyle getThisStyle () {
        assert Require.nonNull(getStyle(), "style");
        assert Require.instanceOf(getStyle(), SimpleTextElmStyle.class, "style");
        return getStyle(SimpleTextElmStyle.class);
    }

    @Override
    public @NotNull CustomTextDisplay getTextDisplay() {
        return getThisEntity();
    }





    /**
     * Creates a new SimpleTextElm using a custom style.
     * @param level The level in which to place the element.
     * @param style The custom style.
     */
    public SimpleTextElm(final @NotNull ServerLevel level, final @NotNull ElmStyle style) {
        super(level, new CustomTextDisplay(level), style);
    }


    /**
     * Creates a new SimpleTextElm using the default style.
     * @param level The level in which to place the element.
     */
    public SimpleTextElm(final @NotNull ServerLevel level) {
        super(level, new CustomTextDisplay(level), new SimpleTextElmStyle());
    }




    @Override
    public @NotNull Transform __calcTransform() {

        // Scale to font size
        return super.__calcTransform().scale(getThisStyle().getFontSize() * SimpleTextElmStyle.TEXT_FONT_FACTOR);
    }




    @Override
    public void flushStyle() {


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
        //! Must be computed before transforms as it changes the visual width of the entity
        { final Flagged<TextOverflowBehaviour> f = getThisStyle().getFlaggedTextOverflowBehaviour();
        if(f.isFlagged()) {
            updateOverflowBehaviour();
            f.unflag();
        }}


        // Handle transform calculations separately
        {
            final Flagged<Transform>     f  = getThisStyle().getFlaggedTransform();
            final Flagged<TextAlignment> fa = getThisStyle().getFlaggedTextAlignment();
            final Flagged<Component>     ft = getThisStyle().getFlaggedText();
            final Flagged<Integer>       fs = getThisStyle().getFlaggedFontSize();
            if(f.isFlagged() || fa.isFlagged() || ft.isFlagged() || fs.isFlagged()) {
                final Transform t = __calcTransform();
                if(fa.get() == TextAlignment.LEFT ) t.moveX(-(getAbsSize().x - calcVisualEntityWidth()) / 2f);
                if(fa.get() == TextAlignment.RIGHT) t.moveX(+(getAbsSize().x - calcVisualEntityWidth()) / 2f);
                t.moveY((getAbsSize().y - calcTotEntityHeightWithMargins()) / 2f);
                getThisEntity().setTransformation(t.moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
                f.unflag();
            }
        }


        // Call superconstructor (transform is already unflagged) and handle the other values normally
        super.flushStyle();
        { final Flagged<Integer> f = getThisStyle().getFlaggedTextOpacity();
        if(f.isFlagged()) {
            getThisEntity().setTextOpacity(f.get());
            f.unflag();
        }}
        { final Flagged<TextAlignment> f = getThisStyle().getFlaggedTextAlignment();
        if(f.isFlagged()) {
            getThisEntity().setTextAlignment(f.get());
            f.unflag();
        }}
    }




    @Override
    protected void __applyTransitionStep(final @NotNull InterpolatedData d) {
        super.__applyTransitionStep(d);
        if(d.hasOpacity()) getThisStyle().setTextOpacity(d.getOpacity());
    }




    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            getThisStyle().getTransform().copy(),
            null,
            null,
            getThisStyle().getTextOpacity()
        );
    }
    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData(final int index) {
        final InterpolatedData fd = futureDataQueue.get(index);
        return new InterpolatedData(
            fd.getTransform().copy(),
            null,
            null,
            fd.getOpacity()
        );
    }




    @Override
    protected void prepareEntityForSpawn(final @NotNull Vector3d pos) {
        getThisEntity().setBackground(new Vector4i(0, 0, 0, 0));
        getThisEntity().setLineWidth(Integer.MAX_VALUE);
    }


    @Override
    public void stepTransition() {
        super.stepTransition();
        getThisEntity().tick();
    }
}
