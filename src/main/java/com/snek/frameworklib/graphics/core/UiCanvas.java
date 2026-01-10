package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.debug.Require;

import net.minecraft.network.chat.Component;

















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
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected UiCanvas(
        final @NotNull UiContext context,
        final @Nullable Component defaultTitle,
        final float height, final float heightTop, final float heightBottom
    ) {
        super(context, defaultTitle, height, heightTop, heightBottom);
    }

    /**
     * Creates a new UiCanvas.
     * @param context The UI context.
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected UiCanvas(
        final @NotNull UiContext context,
        final @Nullable String defaultTitle,
        final float height, final float heightTop, final float heightBottom
    ) {
        super(context, defaultTitle, height, heightTop, heightBottom);
    }
}
