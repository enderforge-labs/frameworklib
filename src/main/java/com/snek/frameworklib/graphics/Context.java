package com.snek.frameworklib.graphics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.graphics.hud._elements.Hud;
import com.snek.frameworklib.graphics.ui._elements.UI;

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

    // // Active contex list
    // private static final Map<UUID, Context> activeContexts = new HashMap<>();

    // Optimization structures
    private @Nullable Elm targetedElm = null;








    protected Context(final @NotNull Player _player) {
        // handlePreviousContext(_player);
        // activeContexts.put(_player.getUUID(), this);
        player = _player;
    }


    // /**
    //  * Allows subclasses to handle existing contexts before this new one is registered.
    //  * NOTICE: This method MUST NOT modify or access the current instance, as it is called INSIDE OF THE SUPERCLASS'S CONSTRUCTOR.
    //  * @param _player The owner of this context.
    //  */
    // protected abstract void handlePreviousContext(final @NotNull Player _player);




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
            // activeContexts.remove(player.getUUID());
            if(activeCanvas != null) activeCanvas.despawn();
            interactionBlocker.despawn();
            interactionBlocker = null;
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
    public static void closeContexts(final @NotNull Player player) {
        // final Context context = activeContexts.get(player.getUUID());
        // if(context != null) context.despawn();
        for(final List<Hud> huds : Hud.getActiveHUDs().values()) {
            if(huds != null) for(final Hud hud : huds) {
                hud.despawn();
            }
        }
        for(final List<UI> uis : UI.getActiveUIs().values()) {
            if(uis != null) for(final UI ui : uis) {
                ui.despawn();
            }
        }
    }


    // /**
    //  * Returns the contexts of the specified player.
    //  * @param player The player.
    //  * @return The contexts, or null if the player doesn't have any open context.
    //  */
    // public static @Nullable Context getOpenContexts(final @NotNull Player player) {
    //     return activeContexts.get(player.getUUID());
    // }




    /**
     * Updates all active contexts.
     */
    public static void updateActiveContexts() {
        for(final List<Hud> huds : Hud.getActiveHUDs().values()) {
            if(huds != null) for(final Hud hud : huds) {
                hud.update();
            }
        }
        for(final List<UI> uis : UI.getActiveUIs().values()) {
            if(uis != null) for(final UI ui : uis) {
                ui.update();
            }
        }
        // for(Context context : activeContexts.values()) {
        //     context.update();
        // }
    }




    /**
     * Forwards a click event to the contexts of the specified player.
     * @param _player The player.
     * @param action The type of click.
     * @return True if any of the player's open contexts consumed the click, false otherwise.
     */
    public static boolean forwardClickStatic(final @NotNull Player _player, final @NotNull ClickAction action) {
        for(final List<Hud> huds : Hud.getActiveHUDs().values()) {
            if(huds != null) for(final Hud hud : huds) {
                final boolean r = __forwardClickStatic(_player, action, hud);
                if(r) return true;
            }
        }
        for(final List<UI> uis : UI.getActiveUIs().values()) {
            if(uis != null) for(final UI ui : uis) {
                final boolean r = __forwardClickStatic(_player, action, ui);
                if(r) return true;
            }
        }
        return false;
    }
    private static boolean __forwardClickStatic(final @NotNull Player _player, final @NotNull ClickAction action, final @NotNull Context context) {
        // final Context context = getOpenContexts(_player);
        // if(context == null) return false;
        final Canvas canvas = context.activeCanvas;
        if(canvas == null) return false;

        return canvas.forwardClick(_player, action);
    }




    /**
     * Forwards a hover event to the context of the specified player.
     * @param _player The player.
     */
    public static void forwardHoverStatic(final @NotNull Player _player) {
        for(final List<Hud> huds : Hud.getActiveHUDs().values()) {
            if(huds != null) for(final Hud hud : huds) {
                __forwardHoverStatic(_player, hud);
            }
        }
        for(final List<UI> uis : UI.getActiveUIs().values()) {
            if(uis != null) for(final UI ui : uis) {
                __forwardHoverStatic(_player, ui);
            }
        }
    }
    private static void __forwardHoverStatic(final @NotNull Player _player, final @NotNull Context context) {
        // final Context context = getOpenContexts(_player);
        // if(context == null) return;
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

