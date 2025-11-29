package com.snek.frameworklib.graphics.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import net.minecraft.world.entity.player.Player;








/**
 * A Context that is bound to a block and rotates to face the player.
 * <p>
 * Unlike HUDs, UIs don't despawn automatically. They also cannot move.
 */
public non-sealed class UiContext extends Context {

    // UI data
    protected final Vector3d spawnPos = new Vector3d();

    // Active UI list
    private static final Map<Player, LinkedList<UiContext>> activeUIs = new HashMap<>();
    public static Map<Player, LinkedList<UiContext>> getActiveUIs() { return activeUIs; }



    /**
     * Creates a new UiContext.
     * @param player The owner of the new UiContext.
    */
    public UiContext(final @NotNull Player player) {
        super(player);
    }




    @Override
    public float getInteractionBlockerSize() {
        return 1;
    }


    @Override
    public @NotNull Vector3d getSpawnPos() {
        return spawnPos;
    }



    @Override
    public void changeCanvas(final @NotNull Canvas newCanvas) {
        if(!(newCanvas instanceof UiCanvas)) {
            throw new IllegalArgumentException("Canvas must be a subclass of UiCanvas, but got: " + newCanvas.getClass().getName());
        }

        finalizeCanvasChange(newCanvas, getSpawnPos());
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {
        if(!spawned) {
            spawnPos.set(pos);

            // Update UiContext list
            activeUIs.putIfAbsent(player, new LinkedList<>());
            final @Nullable LinkedList<UiContext> uis = activeUIs.get(player);
            uis.add(this);
        }
        super.spawn(pos);
    }




    @Override
    public void despawn(final boolean animate) {
        if(spawned) {

            // Update UiContext list
            final @Nullable LinkedList<UiContext> uis = activeUIs.get(player);
            uis.remove(this);
            if(uis.isEmpty()) activeUIs.remove(player);
        }
        super.despawn(animate);
    }
}
