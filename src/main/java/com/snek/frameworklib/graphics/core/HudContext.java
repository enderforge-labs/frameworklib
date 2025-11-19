package com.snek.frameworklib.graphics.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;








/**
 * A Context that follows its player and rotates around them
 */
public non-sealed class HudContext extends Context {

    // Active HUD list
    private static final Map<UUID, List<HudContext>> activeHUDs = new HashMap<>();
    public static Map<UUID, List<HudContext>> getActiveHUDs() { return activeHUDs; }

    // HUD data
    private boolean playerHasSneaked = false;
    private boolean positionRefreshRequired = true;


    /**
     * Check if the position needs to be refreshed, then sets the refresh flag to false at the end of the tick.
     * @return Whether the position needs to be refreshed.
     */
    public boolean attemptPositionRefresh() {
        if(positionRefreshRequired) {
            Scheduler.run(() -> { positionRefreshRequired = false; });
        }
        return positionRefreshRequired;
    }





    /**
     * Creates a new Hud.
     * This automatically closes any previous Hud owned by the player.
     * @param _player The owner of the new Hud.
     */
    public HudContext(final @NotNull Player _player) {
        super(_player);

        // Update HUD list
        activeHUDs.putIfAbsent(player.getUUID(), new ArrayList<>());
        final @Nullable List<HudContext> huds = activeHUDs.get(player.getUUID());
        huds.add(this);
    }




    @Override
    public float getInteractionBlockerSize() {
        return 1f;
    }


    @Override
    public void update() {

        // Detect not sneaking -> sneaking transition and set refresh flag if needed
        if(player.isShiftKeyDown() && !playerHasSneaked) {
            positionRefreshRequired = true;
        }
        playerHasSneaked = player.isShiftKeyDown();


        // Send updates and teleport interaction entity if necessary
        super.update();
        if(attemptPositionRefresh() && interactionBlocker != null) {
            final Vector3d pos = MinecraftUtils.getPlayerStandingEyePos(player).add(((HudCanvas)activeCanvas).__calcVisualShift());
            interactionBlocker.teleport(pos);
        }
    }




    @Override
    public void changeCanvas(final @NotNull Canvas newCanvas) {
        if(!(newCanvas instanceof HudCanvas)) {
            throw new IllegalArgumentException("Canvas must be a subclass of HudCanvas, but got: " + newCanvas.getClass().getName());
        }

        final Vec3 pos = player.getPosition(1);
        finalizeCanvasChange(newCanvas, new Vector3d(pos.x, pos.y, pos.z));
    }




    @Override
    public void despawn(){
        super.despawn();

        // Update HUD list
        final @Nullable List<HudContext> huds = activeHUDs.get(player.getUUID());
        huds.remove(this);
        if(huds.isEmpty()) activeHUDs.remove(player.getUUID());
    }
}
