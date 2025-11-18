package com.snek.frameworklib.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.debug.DebugCheck;
import com.snek.frameworklib.debug.UiDebugWindow;
import com.snek.frameworklib.graphics.Context;
import com.snek.frameworklib.graphics.Elm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
















/**
 * Utility class containing methods to detect what players are looking at and handle hover events.
 */
public abstract class HoverReceiver {
    private HoverReceiver() {}


    // Partial ray casting batch data
    private static @Nullable List<Player> playerListSnapshot = null;
    private static int updateIndex = 0;
    private static @Nullable List<Player> hudPlayers = null;


    // Optimization structures
    private static @NotNull Map<@NotNull Player, com.snek.frameworklib.graphics.Elm> targetedElms = new HashMap<>();








    /**
     * Tick operations. This function updates active contexts and sends them hover events when needed
     */
    public static void tick() {


        // If this is the first iteration ever or all the batches have been processed
        if(updateIndex == 0) {

            // Reset the lists
            playerListSnapshot   = new ArrayList<>();
            hudPlayers           = new ArrayList<>();

            // Recalculate player list snapshot
            for(final ServerLevel serverWorld : FrameworkLib.getServer().getAllLevels()) {
                for(final Player player : serverWorld.players()) {
                    playerListSnapshot.add(player);
                }
            }
        }




        // Find players that currently have open contexts
        final int batchSize = Math.max(1, playerListSnapshot.size() / Configs.getPerf().ray_casting_batches.getValue());
        for(int i = 0; i < batchSize && updateIndex < playerListSnapshot.size(); ++i, ++updateIndex) {
            final Player player = playerListSnapshot.get(updateIndex);


            // Skip player if they are dead or in spectator mode or have a HUD open
            if(player.isSpectator() || player.isDeadOrDying()) continue;
            if(Context.getOpenContext(player) != null) {
                hudPlayers.add(player);
            }
        }




        // If all the batches have been processed, reset update index and finalize tick operations
        if(updateIndex >= playerListSnapshot.size()) {
            updateIndex = 0;
            finalizeTick();
        }
    }








    /**
     * Part of tick operations.
     * Called after all the ray casting batches have been processed.
     */
    public static void finalizeTick() {


        //! Debug window
        if(DebugCheck.isDebug()) {
            UiDebugWindow.getW().clear();
        }


        // Send hover events to the currently hovered elements
        final List<Player> toRemove = new ArrayList<>();
        for(final Entry<Player, Elm> entry : targetedElms.entrySet()) {
            entry.getValue().updateHoverState(entry.getKey());
            if(!entry.getValue().isHovered()) {
                toRemove.add(entry.getKey());
            }
        }


        // Remove elements that aren't being hovered on anymore from the maps
        for(final Player player : toRemove) {
            targetedElms.remove(player);
        }


        // Send hover updates to active Contexts
        for(final Player player : hudPlayers) {
            Context.forwardHoverStatic(player);
        }


        //! Debug window update
        if(DebugCheck.isDebug()) {
            UiDebugWindow.getW().revalidate();
            UiDebugWindow.getW().paintImmediately(0, 0, UiDebugWindow.getW().getWidth(), UiDebugWindow.getW().getHeight());
        }
    }
}
