package com.snek.frameworklib.input;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.core.HudCanvas;
import com.snek.frameworklib.graphics.interfaces.Scrollable;
import com.snek.frameworklib.utils.UtilityClassBase;

import net.minecraft.world.entity.player.Player;







public final class ScrollReceiver extends UtilityClassBase {
    private ScrollReceiver() {}

    // Data
    private static final @NotNull Map<Player, Integer> lastSlots = new HashMap<>();



    public static void tick() {
        for(Player player : FrameworkLib.getServer().getPlayerList().getPlayers()) {

            // Get last and current selected slots
            final int last = lastSlots.getOrDefault(player, -1);
            final int cur  = player.getInventory().selected;

            // Update map and call callback if the slot has changed
            if(cur != last) {
                lastSlots.put(player, cur);
                if(last > -1) onSelectedSlotChange(player, last, cur);
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
        float diff = (float)cur - last;
        if(diff > +4) diff -= 9;
        if(diff < -4) diff += 9;
        final float scrollAmount = diff / 9f;


        // Send scroll event if the context is scrollable
        if(context != null) {
            if(context.getTargetedElm() instanceof Scrollable s) {
                s.onScroll(player, scrollAmount);

                // Update inactivity timer
                if(context.getActiveCanvas() instanceof HudCanvas hud) {
                    hud.resetInactivityTimer();
                }
            }
        }
    }
}