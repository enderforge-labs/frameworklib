package com.snek.frameworklib.graphics.core;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static final Map<Player, List<Context>> activeContexts = new HashMap<>();
    public static Map<Player, List<Context>> getActiveContexts() { return activeContexts; }

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

        // Update context list
        activeContexts.putIfAbsent(player, new ArrayList<>());
        final @Nullable List<Context> contexts = activeContexts.get(player);
        contexts.add(this);
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
        }
    }




    /**
     * Despawns the context and all of its graphic elements.
     */
    public void despawn() {
        if(spawned) {
            spawned = false;
            if(activeCanvas != null) activeCanvas.despawn();
            interactionBlocker.despawn();
            interactionBlocker = null;
        }

        // Update context list
        final @Nullable List<Context> contexts = activeContexts.get(player);
        contexts.remove(this);
        if(contexts.isEmpty()) activeContexts.remove(player);
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
        final List<Context> contexts = Context.getActiveContexts().get(_player);
        if(contexts != null) for(final Context context : contexts) {
            context.despawn();
        }
    }




    /**
     * Updates all active contexts.
     */
    public static void updateActiveContexts() {
        for(final List<Context> contexts : Context.getActiveContexts().values()) {
            for(final Context context : contexts) {
                context.update();
            }
        }
    }




    /**
     * Forwards a click event to the top-most context of the specified player.
     * Only one context will receive the event.
     * @param _player The player.
     * @param action The type of click.
     * @return True if any of the player's open contexts consumed the click, false otherwise.
     */
    public static boolean forwardClickStatic(final @NotNull Player _player, final @NotNull ClickAction action) {

        // Find top most context
        @Nullable Context topMost = getTopMostContext(_player);

        // Send click event
        if(topMost == null) return false;
        final Canvas canvas = topMost.activeCanvas;
        if(canvas == null) return false;
        return canvas.forwardClick(_player, action);
    }




    /**
     * Forwards a hover event to the top-most context of the specified player.
     * Only one context will receive the event.
     * @param _player The player.
     */
    public static void forwardHoverStatic(final @NotNull Player _player) {

        // Find top most context
        @Nullable Context topMost = getTopMostContext(_player);

        // Send click event
        if(topMost == null) return;
        __forwardHoverStatic(_player, topMost);
    }




    private static void __forwardHoverStatic(final @NotNull Player _player, final @NotNull Context context) {
        final Canvas canvas = context.activeCanvas;
        if(canvas == null) return;

        // If a targeted element is present, update its hover state
        if(context.targetedElm != null) {
            context.targetedElm.updateHoverState(_player);

            // If it's no longer being hovered on, check if a new element is being hovered
            if(!context.targetedElm.isHovered()) {
                context.targetedElm = canvas.findTargetedElement(_player);

                // If said element exists, send an update to it
                if(context.targetedElm != null) {
                    context.targetedElm.updateHoverState(_player);
                }
            }
        }

        // If a targeted element is not present, check if a new element is being hovered
        else {
            context.targetedElm = canvas.findTargetedElement(_player);

            // If said element exists, send an update to it
            if(context.targetedElm != null) context.targetedElm.updateHoverState(_player);
        }
    }




    private static Context getTopMostContext(final Player _player) {

        // Get all contexts
        List<Context> contexts = Context.getActiveContexts().get(_player);

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
//FIXME instantly despawn the context when players disconnect or die or are dying or go in spectator mode or change dimension
//FIXME instantly despawn the context when players disconnect or die or are dying or go in spectator mode or change dimension
//FIXME instantly despawn the context when players disconnect or die or are dying or go in spectator mode or change dimension
//FIXME instantly despawn the context when players disconnect or die or are dying or go in spectator mode or change dimension
//FIXME instantly despawn the context when players disconnect or die or are dying or go in spectator mode or change dimension