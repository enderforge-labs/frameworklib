package com.snek.frameworklib.graphics.ui._elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.graphics.Canvas;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

















public abstract class UiCanvas extends Canvas {
    protected @NotNull RateLimiter canvasRotationLimiter = new RateLimiter();

    // Optimization data
    private Vec3 lastPlayerPos = new Vec3(0, 0, 0);
    public static final float POS_UPDATE_DISTANCE = 0.1f;




    /**
     * Creates a new UiCanvas.
     * @param prevCanvas The previous canvas. Used to inherit elements.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected UiCanvas(
        final @NotNull UI _ui,
        final @Nullable UiCanvas prevCanvas, final @NotNull ServerLevel _world, final float height, final float heightTop, final float heightBottom,
        final @Nullable PanelElmStyle bgStyle, final @Nullable PanelElmStyle backStyle
    ) {
        super(_ui, prevCanvas, _world, height, heightTop, heightBottom, bgStyle, backStyle);
    }




    @Override
    public void update() {
        final Player player = context.getPlayer();
        if(!canvasRotationLimiter.attempt()) return;

        final Vec3 playerPos = player.getPosition(1f);                      // Get player position
        if(!lastPlayerPos.closerThan(playerPos, POS_UPDATE_DISTANCE)) {     // If the player has moved far enough
            updateRot(player, false);                                           // Update rotation
            canvasRotationLimiter.renewCooldown(CANVAS_ROTATION_TIME);          // Renew the rotation cooldown
        }
    }




    @Override
    public void updateRot(final @NotNull Player player, final boolean instant) {
        final Vec3 playerPos = player.getPosition(1f);                      // Get player position
        final double dx = ((UI)context).spawnPos.x - playerPos.x;           // Calculate X difference
        final double dz = ((UI)context).spawnPos.z - playerPos.z;           // Calculate Z difference
        final double angle = Math.toDegrees(Math.atan2(-dx, dz));           // Calculate angle from position difference
        final int targetDir = (int)Math.round((angle + 180d) / 45d) % 8;    // Convert from degrees to direction
        __updateRot(targetDir, instant);                                    // Apply animations and update the current direction if needed
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {

        // Instantly rotate the canvas to face the player. This allows it to skip annoying rotation animations
        updateRot(context.getPlayer(), true);

        // Call superclass spawn
        super.spawn(pos);
    }
}
