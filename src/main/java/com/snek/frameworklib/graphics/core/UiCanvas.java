package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;

















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
     */
    protected UiCanvas(
        final @NotNull UiContext context,
        final float height, final float heightTop, final float heightBottom
    ) {
        super(context, height, heightTop, heightBottom);
    }
}
