package com.snek.frameworklib.graphics.hud._elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.frameworklib.graphics.Canvas;
import com.snek.frameworklib.graphics.Context;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;








/**
 * A Context that follows its player and rotates around them
 */
public class Hud extends Context {


    /**
     * Creates a new Hud.
     * This automatically closes any previous Hud owned by the player.
     * @param _player The owner of the new Hud.
     */
    public Hud(final @NotNull Player _player) {
        super(_player);
    }


    @Override
    protected void handlePreviousContext(final @NotNull Player _player) {

        // Close previous HUD if present
        final Context oldContext = getOpenContext(_player);
        if(oldContext instanceof Hud) Context.closeContext(_player);
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




    @Override
    public void changeCanvas(final @NotNull Canvas canvas) {
        if(!(canvas instanceof HudCanvas)) {
            throw new IllegalArgumentException("Canvas must be a subclass of HudCanvas, but got: " + canvas.getClass().getName());
        }

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



    //FIXME REMOVE
    // public static Hud getOpenHudOrCreate(final @NotNull Player player) {
    //     //FIXME it might be necessary to check if the open context is an HUD or not (it might be a UI or other stuff)
    //     final Context hud = getOpenContext(player);
    //     if(hud == null) return new Hud(player);
    //     else return (Hud)hud;  //FIXME it might be necessary to check if the open context is an HUD or not (it might be a UI or other stuff)
    // }
}
