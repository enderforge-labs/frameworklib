package com.snek.frameworklib.graphics.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;








/**
 * A Context that is bound to a block and rotates to face the player.
 * <p>
 * Unlike HUDs, UIs don't despawn automatically. They also cannot move.
 */
public non-sealed class UiContext extends Context {

    // UI data
    public static final float POS_UPDATE_DISTANCE = 0.1f;
    protected @NotNull RateLimiter canvasRotationLimiter = new RateLimiter();

    // Active UI list
    private static final Map<Player, LinkedList<UiContext>> activeUIs = new HashMap<>();
    public static Map<Player, LinkedList<UiContext>> getActiveUIs() { return activeUIs; }



    /**
     * Creates a new UiContext.
     * @param player The owner of the new UiContext.
    */
    public UiContext(final @NotNull Player player) {
        super(player);
    }




    @Override
    public float getInteractionBlockerSize() {
        return 1;
    }




    @Override
    public void tick() {

        // If the active canvas is not null and the limiter isn't blocking
        if(activeCanvas != null) {
            if(!canvasRotationLimiter.attempt()) return;

            // If the rotation needs to be updated
            final int newRot = calcRot();
            if(getRotation() != newRot) {

                // If the player has moved far enough
                final Vector3d playerPos = new Vector3d(player.getPosition(1f).toVector3f());
                if(getSpawnPos().distance(playerPos) > POS_UPDATE_DISTANCE) {

                    // Rotate the canvas and update the current rotation
                    activeCanvas.rotate(getRotation(), newRot, false);
                    canvasRotationLimiter.renewCooldown(Canvas.CANVAS_ROTATION_TIME);
                    setRotation(newRot);
                }
            }
        }
    }




    @Override
    public int calcRot() {
        final Vec3 playerPos = player.getPosition(1f);              // Get player position
        final double dx = getSpawnPos().x - playerPos.x;            // Calculate X difference
        final double dz = getSpawnPos().z - playerPos.z;            // Calculate Z difference
        final double angle = Math.toDegrees(Math.atan2(-dx, dz));   // Calculate angle from position difference
        return (int)Math.round((angle + 180d) / 45d) % 8;           // Convert from degrees to direction
    }



    @Override
    public void changeCanvas(final @NotNull Canvas newCanvas) {
        if(!(newCanvas instanceof UiCanvas)) {
            throw new IllegalArgumentException("Canvas must be a subclass of UiCanvas, but got: " + newCanvas.getClass().getName());
        }

        finalizeCanvasChange(newCanvas);
    }




    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        if(!spawned) {

            // Update UiContext list
            activeUIs.putIfAbsent(player, new LinkedList<>());
            final @Nullable LinkedList<UiContext> uis = activeUIs.get(player);
            uis.add(this);
        }
        super.spawn(pos, animate);
    }




    @Override
    public void despawn(final boolean animate) {
        if(spawned) {

            // Update UiContext list
            final @Nullable LinkedList<UiContext> uis = activeUIs.get(player);
            uis.remove(this);
            if(uis.isEmpty()) activeUIs.remove(player);
        }
        super.despawn(animate);
    }
}
