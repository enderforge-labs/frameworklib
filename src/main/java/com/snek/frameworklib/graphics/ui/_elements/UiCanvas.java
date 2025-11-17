package com.snek.frameworklib.graphics.ui._elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.ui.AlignmentX;
import com.snek.frameworklib.data_types.ui.AlignmentY;
import com.snek.frameworklib.graphics.Canvas;
import com.snek.frameworklib.graphics.Context;
import com.snek.frameworklib.graphics.Div;
import com.snek.frameworklib.graphics.Elm;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.hud._elements.Hud;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;
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

        // Calculate target direction
        final Vec3 playerPos = player.getPosition(1f);                      // Get player position
        if(!lastPlayerPos.closerThan(playerPos, POS_UPDATE_DISTANCE)) {     // If the player has moved enough
            final double dx = ((UI)context).spawnPos.x - playerPos.x;           // Calculate X difference
            final double dz = ((UI)context).spawnPos.z - playerPos.z;           // Calculate Z difference
            final double angle = Math.toDegrees(Math.atan2(-dx, dz));           // Calculate angle from position difference
            final int targetDir = (int)Math.round((angle + 180d) / 45d) % 8;    // Convert from degrees to direction

            updateRot(targetDir);                                               // Apply animations and update the current direction if needed
            canvasRotationLimiter.renewCooldown(CANVAS_ROTATION_TIME);          // Renew the  rotation cooldown
        }
    }
}
