package com.snek.frameworklib.graphics.ui._elements;

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

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.phys.Vec3;








/**
 * The root element of any UI. It contains canvases which contain the UI elements.
 * Only one canvas at a time can be displayed.
 */
public class UI extends Context {

    // UI data
    protected final Vector3d spawnPos = new Vector3d();


    public UI(final @NotNull Player _player) {
        super(_player);
    }


    @Override
    public float getInteractionBlockerSize() {
        return 1;
    }


    @Override
    public void update() {
        if(activeCanvas != null) activeCanvas.update();
    }



    //FIXME add a check that makes sure the canvas is a subclass of UiCanvas
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
        canvas.spawn(spawnPos);
    }




    @Override
    public void spawn(Vector3d pos) {
        if(!spawned) {
            spawnPos.set(pos);
        }
        super.spawn(pos);
    }
}
