package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.network.chat.Component;








/**
 * A canvas that can be used to create HUDs.
 */
public non-sealed class HudCanvas extends Canvas {
    public @NotNull HudContext getHudContext() {
        assert Require.nonNull(getContext(), "context");
        assert Require.instanceOf(getContext(), HudContext.class, "context");
        return (HudContext)getContext();
    }




    /**
     * Creates a new HudCanvas.
     * @param hud The HUD context.
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected HudCanvas(
        final @NotNull HudContext hud,
        final @Nullable Component defaultTitle,
        final float height, final float heightTop, final float heightBottom
    ) {
        super(hud, defaultTitle, height, heightTop, heightBottom);
    }

    /**
     * Creates a new HudCanvas.
     * @param hud The HUD context.
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected HudCanvas(
        final @NotNull HudContext hud,
        final @Nullable String defaultTitle,
        final float height, final float heightTop, final float heightBottom
    ) {
        super(hud, defaultTitle, height, heightTop, heightBottom);
    }




    @Override
    public void denormalizeTransform(final @NotNull Div elm) {
        elm.applyAnimation(new Transition().additiveTransform(new Transform().move(getHudContext().__calcVisualShiftLocal())), false, false);
        super.denormalizeTransform(elm);
    }


    @Override
    public void normalizeTransform(final @NotNull Div elm) {
        elm.applyAnimation(new Transition().additiveTransform(new Transform().move(getHudContext().__calcVisualShiftLocal().mul(-1))), false, false);
        super.normalizeTransform(elm);
    }
}
