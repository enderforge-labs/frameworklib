package com.snek.frameworklib.graphics.hud._elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.Canvas;
import com.snek.frameworklib.graphics.Div;
import com.snek.frameworklib.graphics.Elm;
import com.snek.frameworklib.graphics.hud._styles.HudCanvasBack_S;
import com.snek.frameworklib.graphics.hud._styles.HudCanvasBackground_S;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;








public class HudCanvas extends Canvas implements __HudElm {

    // HUD
    public static final float HUD_DISTANCE = 1.3f;

    // Canvas data
    private boolean spawned = false;

    // Optimization data
    private @NotNull Vector3d lastPlayerEyePos = new Vector3d();
    public static final double POS_UPDATE_DISTANCE = 0.1f;








    public HudCanvas(final @NotNull Hud _hud, final float height, final float heightTop, final float heightBottom) {
        super(_hud, _hud.getActiveCanvas(), (ServerLevel)(_hud.getPlayer().level()), height, heightTop, heightBottom, new HudCanvasBackground_S(), new HudCanvasBack_S());
    }


    @Override
    public void update() {
        final Player player = context.getPlayer();

        // Update rotation
        final int newRot = Math.round((player.getViewYRot(1) + 180f) / 45f) % 8;
        updateRot(newRot);

        // Calculate new position and position difference
        final Vector3d newPos = new Vector3d(player.getEyePosition().toVector3f());
        final Vector3d posDelta = newPos.sub(lastPlayerEyePos, new Vector3d());

        // If the player moved too far since the last update, teleport the entities
        if(posDelta.length() >= POS_UPDATE_DISTANCE) {
            lastPlayerEyePos = newPos;
            updatePos(this);
        }
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
            lastPlayerEyePos = new Vector3d(pos);

            // Move displays away from the player's center
            applyAnimationNowRecursive(new Transition().additiveTransform(new Transform().move(__calcVisualShift())));
        }
    }
    public @NotNull Vector3f __calcVisualShift() {
        final float rotation = (float)Math.toRadians((lastRotation + 4) % 8 * -45f);
        final Vector3f direction = new Vector3f((float)Math.sin(rotation), 0, (float)Math.cos(rotation));
        return direction.mul(HUD_DISTANCE).sub(0, 0.5f, 0);
    }




    @Override
    public void despawn() {
        if(spawned) {
            spawned = false;
            super.despawn();
        }
    }
}
