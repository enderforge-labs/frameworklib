package com.snek.frameworklib.graphics.hud._elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.graphics.Canvas;
import com.snek.frameworklib.graphics.Context;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;








/**
 * A Context that follows its player and rotates around them
 */
public class Hud extends Context {

    // Active HUD list
    private static final Map<UUID, List<Hud>> activeHUDs = new HashMap<>();
    public static Map<UUID, List<Hud>> getActiveHUDs() { return activeHUDs; }

    // HUD data
    private boolean playerHasSneaked = false;
    private boolean positionRefreshRequired = false;


    public boolean attemptPositionRefresh() {
        final boolean old = positionRefreshRequired;
        positionRefreshRequired = false;
        return old;
    }





    /**
     * Creates a new Hud.
     * This automatically closes any previous Hud owned by the player.
     * @param _player The owner of the new Hud.
     */
    public Hud(final @NotNull Player _player) {
        super(_player);

        // Update HUD list
        activeHUDs.putIfAbsent(player.getUUID(), new ArrayList<>());
        final @Nullable List<Hud> huds = activeHUDs.get(player.getUUID());
        huds.add(this);
    }




    @Override
    public float getInteractionBlockerSize() {
        return 0.5f;
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
        if(positionRefreshRequired && interactionBlocker != null) {
            final Vec3 pos = player.getEyePosition();
            interactionBlocker.teleport(new Vector3d(pos.x, pos.y - getInteractionBlockerSize() / 2f, pos.z));
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
        final @Nullable List<Hud> huds = activeHUDs.get(player.getUUID());
        huds.remove(this);
        if(huds.isEmpty()) activeHUDs.remove(player.getUUID());
    }
}
