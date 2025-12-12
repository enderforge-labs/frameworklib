package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.layout.Div;








/**
 * A canvas that can be used to create HUDs.
 */
public non-sealed class HudCanvas extends Canvas {

    // HUD data
    public static final float HUD_DISTANCE = 0.8f;
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
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        if(!isSpawned) {

            // Setup data
            context.setSpawnPos(pos);

            // Spawn
            super.spawn(pos, animate);
        }
    }


    /**
     * Calculates the translation needed to go from the player's eye position to the desired HUD origin coordinates.
     * @return The translation calculated in the global frame.
     */
    public @NotNull Vector3f __calcVisualShiftGlobal() {
        final float rotation = (float)Math.toRadians((context.getRotation() + 4) % 8 * -45f);
        final Vector3f direction = new Vector3f((float)Math.sin(rotation), 0, (float)Math.cos(rotation));
        return direction.mul(HUD_DISTANCE).sub(0, 0.5f, 0);
    }


    /**
     * Calculates the translation needed to go from the player's eye position to the desired HUD origin coordinates.
     * <p>
     * @return The translation calculated in the local frame.
     */
    public @NotNull Vector3f __calcVisualShiftLocal() {
        return new Vector3f(0, -0.5f, -HUD_DISTANCE);
    }




    @Override
    public void denormalizeTransform(final @NotNull Div elm) {
        elm.applyAnimationNow(new Transition().additiveTransform(new Transform().move(__calcVisualShiftLocal())));
        super.denormalizeTransform(elm);
    }


    @Override
    public void normalizeTransform(final @NotNull Div elm) {
        elm.applyAnimationNow(new Transition().additiveTransform(new Transform().move(__calcVisualShiftLocal().mul(-1))));
        super.normalizeTransform(elm);
    }
}
