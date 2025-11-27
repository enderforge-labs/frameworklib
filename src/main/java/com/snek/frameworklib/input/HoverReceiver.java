package com.snek.frameworklib.input;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.debug.DebugCheck;
import com.snek.frameworklib.debug.UiDebugWindow;
import com.snek.frameworklib.graphics.core.Context;

import net.minecraft.world.entity.player.Player;
















/**
 * Utility class that detects what players are looking at and handles hover events.
 * <p>
 * This is responsible for sending hover events to contexts.
 */
public abstract class HoverReceiver {
    private HoverReceiver() {}


    // Partial ray casting batch data
    private static int updateIndex = 0;
    private static @NotNull Player[] playersWithContexts = null;


    // Optimization structures
    private static @NotNull HashSet<Context> updatedContexts     =  new HashSet<>();
    private static @NotNull HashSet<Context> prevUpdatedContexts =  new HashSet<>();








    /**
     * Tick operations. This function updates active contexts and sends them hover events when needed
     */
    public static void tick() {


        // If this is the first iteration ever or all the batches have been processed
        if(updateIndex == 0) {

            // Recompute player list snapshot
            playersWithContexts = Context.getActiveContexts().keySet().toArray(new Player[0]);

            // Send updates to contexts that have not been hovered on in the last full batch
            final Set<Context> diff = new HashSet<>(prevUpdatedContexts);
            diff.removeAll(updatedContexts);
            for(Context context : diff) {
                context.forwardHover(context.getPlayer());
            }

            // Reset lists of contexts
            prevUpdatedContexts = updatedContexts;
            updatedContexts = new HashSet<>();
        }



        //! Debug window
        if(DebugCheck.isDebug()) {
            UiDebugWindow.getW().clear();
        }


        // Update players in the current batch
        final int batchSize = Math.max(1, playersWithContexts.length / Configs.getPerf().ray_casting_batches.getValue());
        for(int i = 0; i < batchSize && updateIndex < playersWithContexts.length; ++i, ++updateIndex) {
            final Player player = playersWithContexts[updateIndex];

            // Skip player if they are dead or in spectator mode or have a HUD open //TODO this might not be needed if the contexts are killed when players dead or go in spectator or etc...
            if(player.isSpectator() || player.isDeadOrDying()) continue; //TODO this might not be needed if the contexts are killed when players dead or go in spectator or etc...

            // Send hover updates to the top-most context
            @Nullable Context topMost = Context.findTopMostContext(player);
            if(topMost != null) {
                topMost.forwardHover(player);
                updatedContexts.add(topMost);
            }
        }


        // If all the batches have been processed, reset the update index
        if(updateIndex >= playersWithContexts.length) {
            updateIndex = 0;
        }


        //! Debug window update
        if(DebugCheck.isDebug()) {
            UiDebugWindow.getW().revalidate();
            UiDebugWindow.getW().paintImmediately(0, 0, UiDebugWindow.getW().getWidth(), UiDebugWindow.getW().getHeight());
        }
    }
}
