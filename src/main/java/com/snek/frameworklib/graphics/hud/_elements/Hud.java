package com.snek.frameworklib.graphics.hud._elements;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.Canvas;
import com.snek.frameworklib.graphics.Context;
import com.snek.frameworklib.graphics.Elm;
import com.snek.frameworklib.graphics.InteractionBlocker;
import com.snek.frameworklib.graphics.ui._elements.UI;
import com.snek.frameworklib.graphics.ui._elements.UiCanvas;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.phys.Vec3;








/**
 * A Context that follows its player and rotates around them
 */
public class Hud extends Context {

    public Hud(final @NotNull Player _player) {
        super(_player);
    }


    @Override
    public float getInteractionBlockerSize() {
        return 0.5f;
    }


    @Override
    public void update() {
        super.update();
        if(interactionBlocker != null) {
            final Vec3 pos = player.getEyePosition();
            interactionBlocker.teleport(new Vector3d(pos.x, pos.y - getInteractionBlockerSize() / 2f, pos.z));
        }
    }




    //FIXME add a check that makes sure the canvas is a subclass of HudCanvas
    @Override
    public void changeCanvas(final @NotNull Canvas canvas) {
        activeCanvas = canvas;

        //TODO new canvases might need something similar to this to update the rotation
        // // Adjust rotation if needed
        // if(lastDirection != 0) {
        //     final Animation animation = calcCanvasRotationAnimation(0, lastDirection);
        //     for(final Div c : canvas.getBg().getChildren()) {
        //         c.applyAnimationNowRecursive(animation);
        //     }
        // }

        // Spawn canvas into the world and play a sound to the user
        final Vec3 pos = player.getPosition(1);
        canvas.spawn(new Vector3d(pos.x, pos.y, pos.z));
    }




    public static Hud getOpenHudOrCreate(final @NotNull Player player) {
        //FIXME it might be necessary to check if the open context is an HUD or not (it might be a UI or other stuff)
        final Context hud = getOpenContext(player);
        if(hud == null) return new Hud(player);
        else return (Hud)hud;  //FIXME it might be necessary to check if the open context is an HUD or not (it might be a UI or other stuff)
    }
}
