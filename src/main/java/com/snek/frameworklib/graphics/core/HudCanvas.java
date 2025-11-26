package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public non-sealed class HudCanvas extends Canvas {

    // HUD data
    public static final float HUD_DISTANCE = 1.3f;
    private boolean spawned = false;
    public @NotNull HudContext getHudContext() { return (HudContext)super.getContext(); }

    // Despawn detection
    private @NotNull Vector3d lastPlayerEyePos = new Vector3d();
    private long lastInputTime = Long.MAX_VALUE;








    /**
     * Creates a new HudCanvas.
     * @param _hud The HUD context.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     * @param bgStyle The style of the background element. Can be null.
     * @param backStyle The style of the back panel element. Can be null.
     */
    protected HudCanvas(
        final @NotNull HudContext _hud,
        final float height, final float heightTop, final float heightBottom,
        final @Nullable PanelElmStyle bgStyle, final @Nullable PanelElmStyle backStyle
    ) {
        super(_hud, _hud.getActiveCanvas(), (ServerLevel)(_hud.getPlayer().level()), height, heightTop, heightBottom, bgStyle, backStyle);
    }




    @Override
    public void update() {
        final Player player = context.getPlayer();


        // Calculate new position and position difference
        final Vector3d newPos = MinecraftUtils.getPlayerStandingEyePos(player);
        final Vector3d posDelta = newPos.sub(lastPlayerEyePos, new Vector3d());


        // Update rotation and position if needed
        if(((HudContext)context).attemptPositionRefresh()) {
            lastPlayerEyePos = newPos;
            updateRot(player, true); //FIXME make it disappear and reappear instead
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




    public void resetInactivityTimer() {
        lastInputTime = Scheduler.getTickNum();
    }




    @Override
    public void updateRot(final @NotNull Player player, final boolean instant) {
        final int newRot = Math.round((player.getViewYRot(1) + 180f) / 45f) % 8;
        __updateRot(newRot, instant);
    }


    public void updatePos(final @NotNull Div div) {
        if(div instanceof Elm e) {
            e.getEntity().teleport(lastPlayerEyePos);
        }
        for(Div c : div.getChildren()) {
            updatePos(c);
        }
    }




    @Override
    public void spawn(Vector3d pos) {
        if(!spawned) {
            spawned = true;
            super.spawn(pos);

            // Setup data
            lastPlayerEyePos = new Vector3d(pos);
            resetInactivityTimer();

            // Move displays away from the player's center
            applyAnimationNowRecursive(new Transition().additiveTransform(new Transform().move(__calcVisualShift())));
        }
    }

    /**
     * Calculates the translation needed to go from the player's eye position to the desired HUD origin coordinates
     * @return
     */
    public @NotNull Vector3f __calcVisualShift() {
        final float rotation = (float)Math.toRadians((lastRotation + 4) % 8 * -45f);
        final Vector3f direction = new Vector3f((float)Math.sin(rotation), 0, (float)Math.cos(rotation));
        return direction.mul(HUD_DISTANCE).sub(0, 0.5f, 0);
    }




    @Override
    public void despawn(final boolean animate) {
        if(spawned) {
            spawned = false;
            super.despawn(animate);
        }
    }
}
