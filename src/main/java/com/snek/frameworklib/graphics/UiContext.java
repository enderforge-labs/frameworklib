package com.snek.frameworklib.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import net.minecraft.world.entity.player.Player;








/**
 * The root element of any UI. It contains canvases which contain the UI elements.
 * Only one canvas at a time can be displayed.
 */
public non-sealed class UiContext extends Context {

    // UI data
    protected final Vector3d spawnPos = new Vector3d();
    public @NotNull Vector3d getSpawnPos() { return spawnPos; }

    // Active UI list
    private static final Map<UUID, List<UiContext>> activeUIs = new HashMap<>();
    public static Map<UUID, List<UiContext>> getActiveUIs() { return activeUIs; }




    public UiContext(final @NotNull Player _player) {
        super(_player);

        // Update UI list
        activeUIs.putIfAbsent(player.getUUID(), new ArrayList<>());
        final @Nullable List<UiContext> uis = activeUIs.get(player.getUUID());
        uis.add(this);
    }




    @Override
    public float getInteractionBlockerSize() {
        return 1;
    }


    @Override
    public void update() {
        if(activeCanvas != null) activeCanvas.update();
    }



    @Override
    public void changeCanvas(final @NotNull Canvas newCanvas) {
        if(!(newCanvas instanceof UiCanvas)) {
            throw new IllegalArgumentException("Canvas must be a subclass of UiCanvas, but got: " + newCanvas.getClass().getName());
        }

        finalizeCanvasChange(newCanvas, spawnPos);
    }




    @Override
    public void spawn(Vector3d pos) {
        if(!spawned) {
            spawnPos.set(pos);
        }
        super.spawn(pos);
    }




    @Override
    public void despawn(){
        super.despawn();

        // Update UI list
        final @Nullable List<UiContext> uis = activeUIs.get(player.getUUID());
        uis.remove(this);
        if(uis.isEmpty()) activeUIs.remove(player.getUUID());
    }
}
