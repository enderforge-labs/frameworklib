package com.snek.frameworklib.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.debug.DebugCheck;
import com.snek.frameworklib.debug.UiDebugWindow;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.core.Elm;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.core.UiContext;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
















/**
 * Utility class containing methods to detect what players are looking at and handle hover events.
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

            // // Reset the lists
            // playerListSnapshot  = new ArrayList<>();
            // playersWithContexts = new HashSet<>();

            // // Recalculate player list snapshot
            // for(final ServerLevel serverWorld : FrameworkLib.getServer().getAllLevels()) {
            //     for(final Player player : serverWorld.players()) {
            //         playerListSnapshot.add(player);
            //     }
            // }
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








    // /**
    //  * Part of tick operations.
    //  * Called after all the ray casting batches have been processed.
    //  */
    // public static void finalizeTick() {


        // // Send hover events to the currently hovered elements
        // final List<Player> toRemove = new ArrayList<>();
        // final List<Pair<Player, Elm>> toAdd = new ArrayList<>();
        // for(final Entry<Player, Elm> entry : targetedElms.entrySet()) {
        //     final @NotNull Elm elm = entry.getValue();
        //     final @NotNull Player player = entry.getKey();

        //     // If the currently hovered element is not being hovered anymore, mark it for list removal
        //     elm.updateHoverState(entry.getKey());
        //     if(!elm.isHovered()) {
        //         toRemove.add(player);

        //         // If the player is hovering over a new element, mark it for list addition
        //         final @Nullable Elm newElm = e
        //         toAdd.add(Pair.from(null, null))
        //     }
        // }



        // // // Get all contexts //TODO this should be an additional generic "all active contexts" map
        // // final List<HudContext> huds = HudContext.getActiveHUDs().get(_player.getUUID());
        // // final List<UiContext>  uis  = UiContext.getActiveUIs  ().get(_player.getUUID());

        // // // Merge contexts into a single list //TODO this should be an additional generic "all active contexts" map
        // // List<Context> contexts = new ArrayList<>();
        // // if(huds != null) contexts.addAll(huds);
        // // if(uis  != null) contexts.addAll(uis);

        // // if(!contextsWithTargetedElm.contains(context)) {
        // //     final Elm targetedElm = shop.getActiveCanvas().findTargetedElement(shop.getuser());
        // //     if(targetedElm != null) {
        // //         targetedElms.put(shop.getuser(), targetedElm);
        // //     }
        // // }


        // // Remove elements that aren't being hovered on anymore from the maps
        // for(final Player player : toRemove) {
        //     targetedElms.remove(player);
        //     targetedElms.add(player);
        // }


        // // Send hover updates to active Contexts
        // for(final Player player : playersWithContexts) {
        //     Context.forwardHoverStatic(player);
        // }
    // }
}
