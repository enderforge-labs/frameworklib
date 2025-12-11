package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.scheduler.Scheduler;








/**
 * A canvas that can be used to create HUDs.
 */
public non-sealed class HudCanvas extends Canvas {

    // HUD data
    public static final float HUD_DISTANCE = 0.8f;
    public @NotNull HudContext getHudContext() { return (HudContext)super.getContext(); }

    // Despawn detection
    private long lastInputTime = Long.MAX_VALUE;








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
    public void update() {

        // Calculate new position and position difference
        final Vector3d newPos = MinecraftUtils.getPlayerStandingEyePos(context.getPlayer());
        final Vector3d posDelta = newPos.sub(context.getSpawnPos(), new Vector3d());


        // Update rotation and position if needed
        if(((HudContext)context).attemptPositionRefresh()) {
            ((HudContext)context).setSpawnPos(newPos);
            updateRot(true); //FIXME make it disappear and reappear instead
            updatePos(this); //FIXME make it disappear and reappear instead
            resetInactivityTimer();
        }


        // Close the HUD if the player moved too far since the last update or it has been inactive for longer than the configured time
        //! This else makes it skip calculations when the HUD moves
        else if(
            posDelta.length() >= Configs.getPerf().hud_close_distance.getValue() ||
            Scheduler.getTickNum() > lastInputTime + Configs.getPerf().hud_close_time.getValue()
        ) {
            //! Schedule despawn for the end of the current tick to avoid modifying the active contexts list while the thread is iterating it
            Scheduler.run(() -> context.despawn(true));
        }
    }




    /**
     * Sets the inactivity timer back to 0.
     * This should be called when an input is detected.
     */
    public void resetInactivityTimer() {
        lastInputTime = Scheduler.getTickNum();
    }




    @Override
    public int calcRot() {
        return Math.round((context.getPlayer().getViewYRot(1) + 180f) / 45f) % 8;
    }


    public void updatePos(final @NotNull Div div) {
        if(div instanceof Elm e) {
            e.getEntity().teleport(context.getSpawnPos());
        }
        for(Div c : div.getChildren()) {
            updatePos(c);
        }
    }




    @Override
    public void spawn(Vector3d pos, final boolean animate) {
        if(!isSpawned) {

            // Setup data
            ((HudContext)context).setSpawnPos(pos);
            resetInactivityTimer();

            //TODO check if this was needed.
            //TODO this should be handled by Canva's built in transform normalization, now
            // // Move displays away from the player's center
            // applyAnimationNowRecursive(new Transition().additiveTransform(new Transform().move(__calcVisualShiftGlobal())));

            super.spawn(pos, animate);
            isSpawned = true;
        }
    }


    /**
     * Calculates the translation needed to go from the player's eye position to the desired HUD origin coordinates.
     * @return The translation calculated in the global frame.
     */
    public @NotNull Vector3f __calcVisualShiftGlobal() {
        final float rotation = (float)Math.toRadians((lastRotation + 4) % 8 * -45f);
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
    public void despawn(final boolean animate) {
        if(isSpawned) {
            super.despawn(animate);
            isSpawned = false;
        }
    }
}
