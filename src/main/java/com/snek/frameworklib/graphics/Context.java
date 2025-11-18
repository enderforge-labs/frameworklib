package com.snek.frameworklib.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;









public abstract class Context {

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

    // Active contex list
    private static final Map<UUID, Context> activeContexts = new HashMap<>();

    // Optimization structures
    private @Nullable Elm targetedElm = null;








    protected Context(final @NotNull Player _player) {
        handlePreviousContext(_player);
        activeContexts.put(_player.getUUID(), this);
        player = _player;
    }


    /**
     * Allows subclasses to handle existing contexts before this new one is registered.
     * NOTICE: This method MUST NOT modify or access the current instance, as it is called INSIDE OF THE SUPERCLASS'S CONSTRUCTOR.
     * @param _player The owner of this context.
     */
    protected abstract void handlePreviousContext(final @NotNull Player _player);




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
            activeContexts.remove(player.getUUID());
            if(activeCanvas != null) activeCanvas.despawn();
            interactionBlocker.despawn();
            interactionBlocker = null;
        }
    }

    public void update() {
        if(activeCanvas != null) activeCanvas.update();
    }

    public abstract void changeCanvas(final @NotNull Canvas canvas);







    /**
     * Closes the context of the specified player.
     *     Does nothing if the player doesn't have any context open.
     * @param player The player.
     */
    public static void closeContext(final @NotNull Player player) {
        final Context context = activeContexts.get(player.getUUID());
        if(context != null) context.despawn();
    }


    /**
     * Returns the context of the specified player.
     * @param player The player.
     * @return The context, or null if the player doesn't have any open context.
     */
    public static @Nullable Context getOpenContext(final @NotNull Player player) {
        return activeContexts.get(player.getUUID());
    }


    /**
     * Updates all active contexts.
     */
    public static void updateActiveContexts() {
        for(Context context : activeContexts.values()) {
            context.update();
        }
    }


    /**
     * Forwards a click event to the context of the specified player.
     * @param _player The player.
     * @param action The type of click.
     * @return True if the player has an open context, false otherwise.
     */
    public static boolean forwardClickStatic(final @NotNull Player _player, final @NotNull ClickAction action) {
        final Context context = activeContexts.get(_player.getUUID());
        if(context == null) return false;
        final Canvas canvas = context.activeCanvas;
        if(canvas == null) return false;

        canvas.forwardClick(_player, action);
        return true;
    }


    /**
     * Forwards a hover event to the context of the specified player.
     * @param _player The player.
     */
    public static void forwardHoverStatic(final @NotNull Player _player) {
        final Context context = activeContexts.get(_player.getUUID());
        if(context == null) return;
        final Canvas canvas = context.activeCanvas;
        if(canvas == null) return;

        if(context.targetedElm != null) {
            context.targetedElm.updateHoverState(_player);
            if(!context.targetedElm.isHovered()) context.targetedElm = null;
        }
        else {
            context.targetedElm = canvas.findTargetedElement(_player);
            if(context.targetedElm != null) context.targetedElm.updateHoverState(_player);
        }
    }
}

