package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.layout.Div;








/**
 * A canvas that can be used to create HUDs.
 */
public non-sealed class HudCanvas extends Canvas {
    public @NotNull HudContext getHudContext() { return (HudContext)super.getContext(); }




    /**
     * Creates a new HudCanvas.
     * @param hud The HUD context.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     * @param bgStyle The style of the background element. Can be null.
     * @param backStyle The style of the back panel element. Can be null.
     */
    protected HudCanvas(
        final @NotNull HudContext hud,
        final float height, final float heightTop, final float heightBottom,
        final @Nullable PanelElmStyle bgStyle, final @Nullable PanelElmStyle backStyle
    ) {
        super(hud, height, heightTop, heightBottom, bgStyle, backStyle);
    }




    @Override
    public void denormalizeTransform(final @NotNull Div elm) {
        elm.applyAnimationNow(new Transition().additiveTransform(new Transform().move(getHudContext().__calcVisualShiftLocal())));
        super.denormalizeTransform(elm);
    }


    @Override
    public void normalizeTransform(final @NotNull Div elm) {
        elm.applyAnimationNow(new Transition().additiveTransform(new Transform().move(getHudContext().__calcVisualShiftLocal().mul(-1))));
        super.normalizeTransform(elm);
    }
}
