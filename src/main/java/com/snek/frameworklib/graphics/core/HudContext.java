package com.snek.frameworklib.graphics.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








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

    // Despawn detection
    private long lastInputTime = Long.MAX_VALUE;


    /**
     * Sets the inactivity timer back to 0.
     * This should be called when an input is detected.
     */
    public void resetInactivityTimer() {
        lastInputTime = Scheduler.getTickNum();
    }




    public void teleportElement(final @NotNull Div div) {
        if(div instanceof Elm e) {
            e.getEntity().teleport(getSpawnPos());
        }
        for(Div c : div.getChildren()) {
            teleportElement(c);
        }
    }
    // private boolean positionRefreshRequired = true;
    // private @NotNull Vector3d lastSpawnPos = new Vector3d();


    // /**
    //  * Check if the position needs to be refreshed, then sets the refresh flag to false at the end of the tick.
    //  * @return Whether the position needs to be refreshed.
    //  */
    // public boolean attemptPositionRefresh() {
    //     if(positionRefreshRequired) {
    //         Scheduler.run(() -> { positionRefreshRequired = false; });
    //     }
    //     return positionRefreshRequired;
    // }





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
    public boolean forwardClick(final @NotNull Player player, final @NotNull ClickAction action) {
        final boolean r = super.forwardClick(player, action);
        if(r) resetInactivityTimer();
        return r;
    }




    @Override
    public void tick() {
        final Vector3d newPos = MinecraftUtils.getPlayerStandingEyePos(getPlayer());

        // Detect not sneaking -> sneaking transition and set refresh flag if needed
        if(player.isShiftKeyDown() && !playerHasSneaked) {

            // Teleport interaction entityt if necessary
            if(interactionBlocker != null) {
                final Vector3d interactionNewPos = newPos.add(((HudCanvas)activeCanvas).__calcVisualShiftGlobal());
                interactionBlocker.teleport(interactionNewPos);
            }


            //FIXME make it disappear and reappear instead
            // Update rotation and position if needed
            setSpawnPos(newPos);
            if(activeCanvas != null) {
                teleportElement(activeCanvas);
                activeCanvas.rotate(getRotation(), calcRot(), true); //FIXME make it disappear and reappear instead
            }
            resetInactivityTimer();
        }
        playerHasSneaked = player.isShiftKeyDown();


        // Close the HUD if the player moved too far since the last update or it has been inactive for longer than the configured time
        //! This else makes it skip calculations when the HUD moves
        if(
            newPos.sub(getSpawnPos(), new Vector3d()).length() >= Configs.getPerf().hud_close_distance.getValue() ||
            Scheduler.getTickNum() > lastInputTime + Configs.getPerf().hud_close_time.getValue()
        ) {
            //! Schedule despawn for the end of the current tick to avoid modifying the active contexts list while the thread is iterating it
            Scheduler.run(() -> despawn(true));
        }
    }




    @Override
    public int calcRot() {
        return Math.round((getPlayer().getViewYRot(1) + 180f) / 45f) % 8;
    }




    @Override
    public void changeCanvas(final @NotNull Canvas newCanvas) {
        if(!(newCanvas instanceof HudCanvas)) {
            throw new IllegalArgumentException("Canvas must be a subclass of HudCanvas, but got: " + newCanvas.getClass().getName());
        }

        finalizeCanvasChange(newCanvas, getSpawnPos());
    }




    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        if(!spawned) {

            // Update HudContext list
            activeHUDs.putIfAbsent(player, new LinkedList<>());
            final @Nullable LinkedList<HudContext> huds = activeHUDs.get(player);
            huds.add(this);

            // Update inactivity timer
            resetInactivityTimer();
        }
        super.spawn(pos, animate);
    }




    @Override
    public void despawn(final boolean animate) {
        if(spawned) {

            // Update HudContext list
            final @Nullable LinkedList<HudContext> huds = activeHUDs.get(player);
            huds.remove(this);
            if(huds.isEmpty()) activeHUDs.remove(player);

            // Update inactivity timer
            lastInputTime = Long.MAX_VALUE;
        }
        super.despawn(animate);
    }
}
