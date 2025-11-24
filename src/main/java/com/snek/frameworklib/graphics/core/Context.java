package com.snek.frameworklib.graphics.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.animations.Animation;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;









public abstract sealed class Context permits HudContext, UiContext {

    // Active Context list
    private static final Map<Player, LinkedList<Context>> activeContexts = new HashMap<>();
    public static Map<Player, LinkedList<Context>> getActiveContexts() { return activeContexts; }

    // Hud data
    protected final Player player;
    protected @Nullable InteractionBlocker interactionBlocker = null;
    protected @Nullable Canvas activeCanvas = null;
    protected boolean spawned = false;

    // Getters
    public @NotNull  Player             getPlayer                () { return player; }
    public @Nullable Canvas             getActiveCanvas          () { return activeCanvas; }
    public @Nullable InteractionBlocker getInteractionBlocker    () { return interactionBlocker; }
    public abstract float getInteractionBlockerSize();

    // Optimization structures
    private @Nullable Elm targetedElm = null;








    protected Context(final @NotNull Player _player) {
        player = _player;
    }


    /**
     * Spawns the context at the provided coordinates.
     * @param pos The coordinates to spawn the context at.
     *     This defines the the center of the context's hitbox.
     */
    public void spawn(Vector3d pos) {
        if(!spawned) {
            spawned = true;
            final float size = getInteractionBlockerSize();
            if(activeCanvas != null) activeCanvas.spawn(pos);
            interactionBlocker = new InteractionBlocker(player.level(), size, size);
            interactionBlocker.spawn(pos);

            // Update context list
            activeContexts.putIfAbsent(player, new LinkedList<>());
            final @NotNull LinkedList<Context> contexts = activeContexts.get(player);
            contexts.add(this);
        }
    }




    /**
     * Despawns the context and all of its graphic elements.
     */
    public void despawn(final boolean animate) {
        if(spawned) {
            spawned = false;
            if(activeCanvas != null) activeCanvas.despawn(animate);
            interactionBlocker.despawn();
            interactionBlocker = null;

            // Update context list
            final @Nullable LinkedList<Context> contexts = activeContexts.get(player);
            contexts.remove(this);
            if(contexts.isEmpty()) activeContexts.remove(player);
        }
    }




    public void update() {
        if(activeCanvas != null) activeCanvas.update();
    }




    public abstract void changeCanvas(final @NotNull Canvas canvas);
    protected final void finalizeCanvasChange(final @NotNull Canvas newCanvas, final @NotNull Vector3d canvasSpawnPos) {
        final int lastRotation = activeCanvas != null ? activeCanvas.getRotation() : 0;


        // Set new active canvas and spawn canvas into the world
        activeCanvas = newCanvas;
        newCanvas.spawn(canvasSpawnPos);


        // Adjust rotation of child elements if needed
        //! newCanvas.lastRotation is updated when the canvas is created. In Canvas.Canvas
        if(lastRotation != 0) {
            final Animation animation = Canvas.calcCanvasRotationAnimation(0, lastRotation);
            for(final Div c : newCanvas.getBg().getChildren()) {
                c.applyAnimationNowRecursive(animation);
            }
        }
    }







    /**
     * Closes all the contexts of the specified player.
     *     Does nothing if the player doesn't have any context open.
     * @param player The player.
     */
    public static void closeContexts(final @NotNull Player _player) {
        final LinkedList<Context> contexts = getActiveContexts().get(_player);
        if(contexts != null) for(final Context context : contexts) {
            context.despawn(true);
        }
    }




    /**
     * Updates all active contexts.
     */
    public static void updateActiveContexts() {
        for(final LinkedList<Context> contexts : getActiveContexts().values()) {
            for(final Context context : contexts) {
                context.update();
            }
        }
    }




    /**
     * Forwards a click event.
     * @param _player The player.
     * @param action The type of click.
     * @return True if the context consumed the click, false otherwise.
     */
    public boolean forwardClick(final @NotNull Player _player, final @NotNull ClickAction action) {
        if(activeCanvas == null) return false;
        return activeCanvas.forwardClick(_player, action);
    }




    /**
     * Forwards a hover event to this context.
     * @param _player The player.
     */
    public void forwardHover(final @NotNull Player _player) {
        final Canvas canvas = activeCanvas;
        if(canvas == null) return;

        // If a targeted element is present, update its hover state
        if(targetedElm != null) {
            targetedElm.updateHoverState(_player);

            // If it's no longer being hovered on, check if a new element is being hovered
            if(!targetedElm.isHovered()) {
                targetedElm = canvas.findTargetedElement(_player);

                // If said element exists, send an update to it
                if(targetedElm != null) {
                    targetedElm.updateHoverState(_player);
                }
            }
        }

        // If a targeted element is not present, check if a new element is being hovered
        else {
            targetedElm = canvas.findTargetedElement(_player);

            // If said element exists, send an update to it
            if(targetedElm != null) targetedElm.updateHoverState(_player);
        }
    }




    public static @Nullable Context findTopMostContext(final Player _player) {

        // Get all contexts
        LinkedList<Context> contexts = getActiveContexts().get(_player);
        if(contexts == null) return null;

        // Find top-most context
        @Nullable Context topMost = null;
        double bestDistance = Double.MAX_VALUE;
        for(final @Nullable Context c : contexts) {
            if(c.activeCanvas == null) continue;
            final double distance = c.activeCanvas.getBg().getIntersectionLength(_player);
            if(distance > 0 && distance < bestDistance) {
                bestDistance = distance;
                topMost = c;
            }
        }

        // Return found context
        return topMost;
    }
}


//FIXME instantly despawn the context when players disconnect or die or are dying or go in spectator mode or change dimension
//FIXME instantly despawn the context when players disconnect or die or are dying or go in spectator mode or change dimension