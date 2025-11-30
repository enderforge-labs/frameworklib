package com.snek.frameworklib.graphics.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;








/**
 * A Context that follows its player and rotates around them.
 * <p>
 * Unlike UIs, HUDs can be stacked and despawn when the player walks away or stops interacting with it.
 * They are also not bound to a block and can move freely.
 */
public non-sealed class HudContext extends Context {

    // Active HUD list
    private static final Map<Player, LinkedList<HudContext>> activeHUDs = new HashMap<>();
    public static Map<Player, LinkedList<HudContext>> getActiveHUDs() { return activeHUDs; }

    // HUD data
    private boolean playerHasSneaked = false;
    private boolean positionRefreshRequired = true;
    private @NotNull Vector3d lastSpawnPos = new Vector3d();


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
     * Creates a new HudContext.
     * @param player The owner of the new HudContext.
     */
    public HudContext(final @NotNull Player player) {
        super(player);
    }




    @Override
    public float getInteractionBlockerSize() {
        return 1f;
    }


    @Override
    public @NotNull Vector3d getSpawnPos() {
        return lastSpawnPos;
    }
    public void setSpawnPos(final @NotNull Vector3d newPos) {
        //FIXME this might need to be native to HudContext. the whole position update thing
        lastSpawnPos.set(newPos);
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

        finalizeCanvasChange(newCanvas, getSpawnPos());
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {
        if(!spawned) {

            // Update HudContext list
            activeHUDs.putIfAbsent(player, new LinkedList<>());
            final @Nullable LinkedList<HudContext> huds = activeHUDs.get(player);
            huds.add(this);
        }
        super.spawn(pos);
    }




    @Override
    public void despawn(final boolean animate) {
        if(spawned) {

            // Update HudContext list
            final @Nullable LinkedList<HudContext> huds = activeHUDs.get(player);
            huds.remove(this);
            if(huds.isEmpty()) activeHUDs.remove(player);
        }
        super.despawn(animate);
    }
}
