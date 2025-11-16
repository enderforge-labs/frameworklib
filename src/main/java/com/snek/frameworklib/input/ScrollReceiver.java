package com.snek.frameworklib.input;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.player.Player;








public abstract class ScrollReceiver {
    private ScrollReceiver() {}

    /**
     * Handles slot selection changes, aka scroll wheel inputs.
     * Must be called every time a player that's looking at a Scrollable UI element changes their selected hotbar slot.
     */
    public void onSelectedSlotChange(final @NotNull Player player) {
        //player.getInventory().selected; //TODO
    }
}
//TODO detect scrolls (if player has a ui open) and call this