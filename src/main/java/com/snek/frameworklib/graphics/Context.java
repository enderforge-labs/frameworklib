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
    public static final float INTERACTION_BLOCKER_SIZE = 0.5f;

    // Hud data
    protected final Player player;
    protected @Nullable InteractionBlocker interactionBlocker = null;
    protected @Nullable Canvas activeCanvas = null;
    protected boolean spawned = false;

    // Getters
    public @NotNull  Player             getPlayer            () { return player; }
    public @Nullable Canvas             getActiveCanvas      () { return activeCanvas; }
    public @Nullable InteractionBlocker getInteractionBlocker() { return interactionBlocker; }

    // Active contex list
    private static final Map<UUID, Context> activeContexts = new HashMap<>();

    // Optimization structures
    private @Nullable Elm targetedElm = null;








    protected Context(final @NotNull Player _player) {
        player = _player;
        activeContexts.put(player.getUUID(), this);
    }


    public void spawn(Vector3d pos) {
        if(!spawned) {
            spawned = true;
            if(activeCanvas != null) activeCanvas.spawn(pos);
            interactionBlocker = new InteractionBlocker(player.level(), INTERACTION_BLOCKER_SIZE, INTERACTION_BLOCKER_SIZE);
            interactionBlocker.spawn(pos.sub(new Vector3d(0, INTERACTION_BLOCKER_SIZE / -2f, 0), new Vector3d()));
        }
    }

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








    public static void closeContext(final @NotNull Player player) {
        final Context context = activeContexts.get(player.getUUID());
        if(context != null) context.despawn();
    }


    public static Context getOpenContext(final @NotNull Player player) {
        return activeContexts.get(player.getUUID());
    }


    public static void updateActiveContexts() {
        for(Context context : activeContexts.values()) {
            context.update();
        }
    }


    /**
     * Forwards a click event to the context of a player.
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

