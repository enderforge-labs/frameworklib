package com.snek.frameworklib.input;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.interfaces.Scrollable;
import com.snek.frameworklib.utils.UtilityClassBase;

import net.minecraft.world.entity.player.Player;







/**
 * A class that detects selected slot changes from players and sends scroll events to active contexts.
 */
public final class ScrollReceiver extends UtilityClassBase {
    private static final @NotNull Map<@NotNull Player, @Nullable Integer> lastSlots = new HashMap<>();
    private ScrollReceiver() {}




    /**
     * Tick event. Detects the currently selected slot of each player and sends scroll events if they have changed since the last call.
     * <p>
     * Must be called each server tick.
     */
    public static void tick() {
        for(final Player player : FrameworkLib.getServer().getPlayerList().getPlayers()) {

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
     * <p>
     * Must be called every time a player that's looking at a Scrollable UI element changes their selected hotbar slot.
     * @param player The player.
     * @param player The index of the slot the player had selected the last time they were checked.
     * @param player The index of the slot the player has currently selected.
     */
    public static void onSelectedSlotChange(final @NotNull Player player, final int last, final int cur) {
        assert Require.nonNull(player, "player");
        assert Require.inRange(last, 0, 8, "last slot");
        assert Require.inRange(cur, -1, 8, "current slot");
        //!                         ^ can be -1 if player is detected for the first time

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
                if(context instanceof HudContext hud) {
                    hud.resetInactivityTimer();
                }
            }
        }
    }
}