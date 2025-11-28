package com.snek.frameworklib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.frameworkconfig.FrameworkConfig;
import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.core.InteractionBlocker;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.input.ClickReceiver;
import com.snek.frameworklib.input.HoverReceiver;
import com.snek.frameworklib.input.MessageReceiver;
import com.snek.frameworklib.input.ScrollReceiver;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.inventory.ClickAction;








/**
 * The main class and entry point of Framework Lib.
 * <p>
 * This class registers all the event callbacks and contains the library's ID, the logger, the phase ID and utility methods.
 */
public class FrameworkLib implements ModInitializer {
    public static final String LIB_ID = "frameworklib";
    public static final @NotNull Logger LOGGER = LoggerFactory.getLogger(LIB_ID);
    public static final ResourceLocation PHASE_ID = new ResourceLocation(LIB_ID, "phase_id");


    // Server instance
    private static @Nullable MinecraftServer serverInstance = null;
    public  static @NotNull  MinecraftServer getServer() {
        if(serverInstance == null) {
            throw new NullPointerException("Server instance requested before initialization");
        }
        return serverInstance;
    }








    @Override
    public void onInitialize() {


        // Register initialization
        ServerLifecycleEvents.SERVER_STARTING.addPhaseOrdering(FrameworkConfig.PHASE_ID, PHASE_ID);
        ServerLifecycleEvents.SERVER_STARTING.register(PHASE_ID, server -> {
            serverInstance = server;


            // Register chat input handler
            ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
                return !MessageReceiver.onMessage(message, sender);
            });


            // Read config files
            Configs.loadConfigs();
        });




        // Register post-initialization events
        ServerLifecycleEvents.SERVER_STARTED.register(PHASE_ID, server -> {


            // Schedule UI element update loop
            Scheduler.loop(0, Configs.getPerf().animation_refresh_time.getValue(), () -> {
                Elm.processUpdateQueue();
                Context.updateActiveContexts();
            });




            // Schedule hover manager loop
            Scheduler.loop(0, 1, HoverReceiver::tick);

            // Schedule scroll receiver loop
            Scheduler.loop(0, 1, ScrollReceiver::tick);




            // Create and register entity click events (interaction blocker clicks)
            AttackEntityCallback.EVENT.register(PHASE_ID, (player, world, hand, entity, hitResult) -> {
                return ClickReceiver.onClickEntity(world, player, hand, ClickAction.PRIMARY, entity);
            });
            UseEntityCallback.EVENT.register(PHASE_ID, (player, world, hand, entity, hitResult) -> {
                return ClickReceiver.onClickEntity(world, player, hand, ClickAction.SECONDARY, entity);
            });




            // Create and register block click events
            AttackBlockCallback.EVENT.register(PHASE_ID, (player, world, hand, blockPos, direction) -> {
                return ClickReceiver.onClickBlock(world, player, hand, ClickAction.PRIMARY, blockPos.offset(direction.getNormal()));
            });
            UseBlockCallback.EVENT.register(PHASE_ID, (player, world, hand, hitResult) -> {
                return ClickReceiver.onClickBlock(world, player, hand, ClickAction.SECONDARY, hitResult.getBlockPos().offset(hitResult.getDirection().getNormal()));
            });




            // Register scheduler
            ServerTickEvents.END_SERVER_TICK.register(PHASE_ID, _server -> {
                Scheduler.tick();
            });


            // Register entity display purge
            ServerEntityEvents.ENTITY_LOAD.register(PHASE_ID, (entity, world) -> {
                Elm.onEntityLoad(entity);
                InteractionBlocker.onEntityLoad(entity);
            });
        });




        // Log library loading
        LOGGER.info("FrameworkLib loaded :3");
    }
}
