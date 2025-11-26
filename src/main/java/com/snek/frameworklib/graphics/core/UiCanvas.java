package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

















/**
 * A canvas that can be used to create UIs.
 */
public non-sealed class UiCanvas extends Canvas {

    // UI data
    protected @NotNull RateLimiter canvasRotationLimiter = new RateLimiter();
    public @NotNull UiContext getUiContext() { return (UiContext)super.getContext(); }

    // Optimization data
    private Vec3 lastPlayerPos = new Vec3(0, 0, 0);
    public static final float POS_UPDATE_DISTANCE = 0.1f;




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




    @Override
    public void update() {
        final Player player = context.getPlayer();
        if(!canvasRotationLimiter.attempt()) return;

        final Vec3 playerPos = player.getPosition(1f);                      // Get player position
        if(!lastPlayerPos.closerThan(playerPos, POS_UPDATE_DISTANCE)) {     // If the player has moved far enough
            updateRot(false);                                                   // Update rotation
            canvasRotationLimiter.renewCooldown(CANVAS_ROTATION_TIME);          // Renew the rotation cooldown
        }
    }




    @Override
    public void updateRot(final boolean instant) {
        final Vec3 playerPos = context.getPlayer().getPosition(1f);                // Get player position
        final double dx = ((UiContext)context).getSpawnPos().x - playerPos.x;      // Calculate X difference
        final double dz = ((UiContext)context).getSpawnPos().z - playerPos.z;      // Calculate Z difference
        final double angle = Math.toDegrees(Math.atan2(-dx, dz));                   // Calculate angle from position difference
        final int targetDir = (int)Math.round((angle + 180d) / 45d) % 8;            // Convert from degrees to direction
        __updateRot(targetDir, instant);                                            // Apply animations and update the current direction if needed
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {

        // Instantly rotate the canvas to face the player. This allows it to skip annoying rotation animations
        updateRot(true);

        // Call superclass spawn
        super.spawn(pos);
    }
}
