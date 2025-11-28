package com.snek.frameworklib.input;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.utils.UtilityClassBase;

import net.minecraft.world.entity.player.Player;







//TODO this might not be necessary if we don't use mouse scrolling
public final class ScrollReceiver extends UtilityClassBase {
    private ScrollReceiver() {}

    // Data
    private static final @NotNull Map<Player, Integer> lastSlots = new HashMap<>();



    public static void tick() {
        for(Player player : FrameworkLib.getServer().getPlayerList().getPlayers()) {

            // Get last and current selected slots
            final int last = lastSlots.getOrDefault(player, 0);
            final int cur  = player.getInventory().selected;

            //TODO account for last == 0. don't scroll that
            // Update map and call callback if the slot has changed
            if(cur != last) {
                lastSlots.put(player, cur);
                onSelectedSlotChange(player, last, cur);
            }
        }
    }



    /**
     * Handles slot selection changes, aka scroll wheel inputs.
     * Must be called every time a player that's looking at a Scrollable UI element changes their selected hotbar slot.
     */
    public static void onSelectedSlotChange(final @NotNull Player player, final int last, final int cur) {

        // Find active context and calculate the amount of scrolling
        final @Nullable Context context = HoverReceiver.getTargetedContext(player);
        final float scrollAmount = (cur - last) / 8f; //FIXME out of bounds edge cases

        if(context != null) {
            //TODO
            System.err.println("scroll: " + scrollAmount);
        }
    }
}
//TODO detect scrolls (if player has a ui open) and call this