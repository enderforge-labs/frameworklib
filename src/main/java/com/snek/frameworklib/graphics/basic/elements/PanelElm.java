package com.snek.frameworklib.graphics.basic.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector4i;

import com.snek.frameworklib.data_types.animations.InterpolatedData;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.displays.CustomDisplay;
import com.snek.frameworklib.data_types.displays.CustomTextDisplay;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.server.level.ServerLevel;








/**
 * A simple UI element with a background color and animations.
 * <p>
 * Panels default to a 1x1 blocks size.
 */
public class PanelElm extends Elm {
    private @NotNull CustomTextDisplay getThisEntity() { return getEntity(CustomTextDisplay.class); }
    private @NotNull PanelElmStyle     getThisStyle () { return getStyle (PanelElmStyle    .class); }

    // The amount of panel elements of default size would be needed to cover the width of a block
    public static final float ENTITY_BLOCK_RATIO_X = 40f;
    public static final float ENTITY_BLOCK_RATIO_Y = 40f;

    // The translation on the X axis needed to align the panel entity with the element's bounding box
    public static final float ENTITY_SHIFT_X = -0.5f;






    /**
     * Creates a new PanelElm using an existing entity and a custom style.
     * @param world The world in which to place the element.
     * @param entity The display entity.
     * @param style The custom style.
     */
    protected PanelElm(final @NotNull ServerLevel world, final @NotNull CustomDisplay entity, final @NotNull ElmStyle style) {
        super(world, entity, style);
        getThisEntity().setText(new Txt().get());
    }


    /**
     * Creates a new PanelElm using a custom style.
     * @param world The world in which to place the element.
     * @param style The custom style.
     */
    public PanelElm(final @NotNull ServerLevel world, final @NotNull ElmStyle style) {
        this(world, new CustomTextDisplay(world), style);
    }


    /**
     * Creates a new PanelElm using the default style.
     * @param world The world in which to place the element.
     */
    public PanelElm(final @NotNull ServerLevel world) {
        this(world, new CustomTextDisplay(world), new PanelElmStyle());
    }




    @Override
    public void flushStyle() {
        super.flushStyle();


        // Apply color
        {
            final Flagged<Vector3i> fc = getThisStyle().getFlaggedColor();
            final Flagged<Integer>  fa = getThisStyle().getFlaggedAlpha();
            if(fc.isFlagged() || fa.isFlagged()) {
                final Vector3i color = fc.get();
                getThisEntity().setBackground(new Vector4i(fa.get(), color.x, color.y, color.z));
                fa.unflag();
                fc.unflag();
            }
        }
    }




    @Override
    protected void __applyTransitionStep(final @NotNull InterpolatedData d) {
        super.__applyTransitionStep(d);
        if(d.hasBgColor()) getThisStyle().setColor(d.getBgColor());
        if(d.hasBgAlpha()) getThisStyle().setAlpha(d.getBgAlpha());
    }




    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            getThisStyle().getTransform().copy(),
            new Vector3i(getThisStyle().getColor()),
            getThisStyle().getAlpha(),
            null
        );
    }
    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData(final int index) {
        final InterpolatedData fd = futureDataQueue.get(index);
        return new InterpolatedData(
            fd.getTransform().copy(),
            new Vector3i(fd.getBgColor()),
            fd.getBgAlpha(),
            null
        );
    }




    @Override
    public boolean stepTransition() {
        return super.stepTransition();
    }




    @Override
    public @NotNull Transform __calcTransform() {
        final Transform t = super.__calcTransform();
        return t.copy()
            .scaleX(ENTITY_BLOCK_RATIO_X * getAbsSize().x)
            .scaleY(ENTITY_BLOCK_RATIO_Y * getAbsSize().y)
            .moveX(ENTITY_SHIFT_X * getAbsSize().x)
        ;
    }
}
