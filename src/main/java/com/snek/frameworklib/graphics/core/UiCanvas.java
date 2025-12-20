package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;

















/**
 * A canvas that can be used to create UIs.
 */
public non-sealed class UiCanvas extends Canvas {

    // UI data
    public @NotNull UiContext getUiContext() {
        assert Require.nonNull(getContext(), "context");
        assert Require.instanceOf(getContext(), UiContext.class, "context");
        return (UiContext)getContext();
    }




    /**
     * Creates a new UiCanvas.
     * @param context The UI context.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     * @param bgStyle The style of the background element. Can be null.
     * @param backStyle The style of the back panel element. Can be null.
     */
    protected UiCanvas(
        final @NotNull UiContext context,
        final float height, final float heightTop, final float heightBottom,
        final @Nullable PanelElmStyle bgStyle, final @Nullable PanelElmStyle backStyle
    ) {
        super(context, height, heightTop, heightBottom, bgStyle, backStyle);
    }
}
