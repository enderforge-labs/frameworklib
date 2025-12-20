package com.snek.frameworklib.graphics.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.debug.Require;
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
    private static final @NotNull Map<@NotNull Player, @Nullable LinkedList<@NotNull HudContext>> activeHUDs = new HashMap<>();
    public static        @NotNull Map<@NotNull Player, @Nullable LinkedList<@NotNull HudContext>> getActiveHUDs() { return activeHUDs; }

    // HUD data
    public static final float HUD_DISTANCE = 1.2f;
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
        assert Require.nonNull(div, "element");
        if(div instanceof Elm e) {
            e.getEntity().teleport(getSpawnPos());
        }
        for(Div c : div.getChildren()) {
            teleportElement(c);
        }
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
    public boolean forwardClick(final @NotNull Player player, final @NotNull ClickAction action) {
        final boolean r = super.forwardClick(player, action);
        if(r) resetInactivityTimer();
        return r;
    }



    private boolean positionInitialized = false;
    @Override
    public void tick() {
        final Vector3d newPos = MinecraftUtils.getPlayerStandingEyePos(player);



        // Close the HUD if the player moved too far since the last update or it has been inactive for longer than the configured time
        if(
            newPos.sub(getSpawnPos(), new Vector3d()).length() >= Configs.getPerf().hud_close_distance.getValue() ||
            Scheduler.getTickNum() > lastInputTime + Configs.getPerf().hud_close_time.getValue()
        ) {
            //! Schedule despawn for the end of the current tick to avoid modifying the active contexts list while the thread is iterating it
            Scheduler.run(() -> despawn(true));
            return;
        }




        // Detect not sneaking -> sneaking transition and set refresh flag if needed
        if(!positionInitialized || (player.isShiftKeyDown() && !playerHasSneaked)) {
            positionInitialized = true;

            //FIXME make it disappear and reappear instead
            //TODO add "respawnDelayed" method that calls respawn on a tracked task handler - check said task handler from despawn and spawn methods
            // Update position
            setSpawnPos(newPos);
            if(activeCanvas != null) {
                teleportElement(activeCanvas);
            }


            // If the rotation needs to be updated
            final int newRot = calcRot();
            if(getRotation() != newRot) {

                // Rotate the canvas and set the new rotation
                if(activeCanvas != null) {
                    activeCanvas.rotate(getRotation(), newRot, true); //FIXME make it disappear and reappear instead
                }
                setRotation(newRot);
            }


            // Teleport interaction entityt if necessary
            if(interactionBlocker != null) {
                final Vector3d interactionNewPos = newPos.add(__calcVisualShiftGlobal(), new Vector3d());
                interactionBlocker.teleport(interactionNewPos);
            }


            // Reset inactivity timer
            resetInactivityTimer();
        }
        playerHasSneaked = player.isShiftKeyDown();
    }




    @Override
    public int calcRot() {
        return Math.round((player.getViewYRot(1) + 180f) / 45f) % 8;
    }




    @Override
    public void changeCanvas(final @NotNull Canvas newCanvas) {
        assert Require.nonNull(newCanvas, "new canvas");
        assert Require.instanceOf(newCanvas, HudCanvas.class, "new canvas");
        finalizeCanvasChange(newCanvas);
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




    /**
     * Calculates the translation needed to go from the player's eye position to the desired HUD origin coordinates.
     * @return The translation calculated in the global frame.
     */
    public @NotNull Vector3f __calcVisualShiftGlobal() {
        final float rotation = (float)Math.toRadians((getRotation() + 4) % 8 * -45f);
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
}
