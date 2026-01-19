package com.snek.frameworklib;

import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.frameworkconfig.FrameworkConfig;
import com.snek.frameworklib.cache.PlayerDataCache;
import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.enhanced_recipes.shaped.EnhancedShapedRecipe;
import com.snek.frameworklib.enhanced_recipes.shaped.EnhancedShapedRecipeSerializer;
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
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.LevelResource;








/**
 * The main class and entry point of Framework Lib.
 * <p>
 * This class registers all the event callbacks and contains the library's ID, the logger, the phase ID and utility methods.
 * @since v1.1.0
 */
public class FrameworkLib implements ModInitializer {


    // IDs and loggers
    public static final String LIB_ID = "frameworklib";
    public static final @NotNull Logger LOGGER = LoggerFactory.getLogger(LIB_ID);
    public static final ResourceLocation PHASE_ID = new ResourceLocation(LIB_ID, "phase_id");


    // EnhancedShapedRecipe serialier
    public static final RecipeSerializer<EnhancedShapedRecipe> ENHANCED_SHAPED_RECIPE_SERIALIZER = Registry.register(
        BuiltInRegistries.RECIPE_SERIALIZER,
        new ResourceLocation("frameworklib", "enhanced_crafting_shaped"),
        new EnhancedShapedRecipeSerializer()
    );


    /**
     * Computes the path to the data storage directory for the specified mod.
     * @param modId The ID of the mod.
     * @return The path to the storage directory.
     */
    public static Path getStorageDir(final @NotNull String modId) {
        return FrameworkLib.getServer().getWorldPath(LevelResource.ROOT).resolve("data/" + modId);
    }

    /**
     * Computes the path to the config directory for the specified mod.
     * @param modId The ID of the mod.
     * @return The path to the config directory.
     */
    public static Path getConfigDir(final @NotNull String modId) {
        return FabricLoader.getInstance().getConfigDir().resolve(modId);
    }


    // Server instance
    private static @Nullable MinecraftServer serverInstance = null;
    public  static @NotNull  MinecraftServer getServer() {
        assert Require.condition(serverInstance != null, "Server instance requested before initialization");
        return serverInstance;
    }








    @SuppressWarnings("java:S2696") //! Assigning static values from a non-static method
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


            // Register player login callbacks
            ServerPlayConnectionEvents.JOIN.register((handler, sender, _server) -> {
                PlayerDataCache.onPlayerJoin(handler.getPlayer());
            });


            // Read config files
            Configs.loadConfigs();


            // Read caches files
            PlayerDataCache.loadAllCaches();
        });




        // Register post-initialization events
        ServerLifecycleEvents.SERVER_STARTED.register(PHASE_ID, server -> {


            // Schedule UI element update loop
            Scheduler.loop(0, Configs.getPerf().animation_refresh_time.getValue(), () -> {
                Elm.processUpdateQueue();
                Context.tickActiveContexts();
            });




            // Schedule hover manager loop
            Scheduler.loop(0, 1, HoverReceiver::tick);

            // Schedule scroll receiver loop
            Scheduler.loop(0, 1, ScrollReceiver::tick);




            // Create and register entity click events (interaction blocker clicks)
            AttackEntityCallback.EVENT.register(PHASE_ID, (player, level, hand, entity, hitResult) -> {
                return ClickReceiver.onClickEntity(level, player, hand, ClickAction.PRIMARY, entity);
            });
            UseEntityCallback.EVENT.register(PHASE_ID, (player, level, hand, entity, hitResult) -> {
                return ClickReceiver.onClickEntity(level, player, hand, ClickAction.SECONDARY, entity);
            });




            // Create and register block click events
            AttackBlockCallback.EVENT.register(PHASE_ID, (player, level, hand, blockPos, direction) -> {
                return ClickReceiver.onClickBlock(level, player, hand, ClickAction.PRIMARY, blockPos.offset(direction.getNormal()));
            });
            UseBlockCallback.EVENT.register(PHASE_ID, (player, level, hand, hitResult) -> {
                return ClickReceiver.onClickBlock(level, player, hand, ClickAction.SECONDARY, hitResult.getBlockPos().offset(hitResult.getDirection().getNormal()));
            });




            // Register scheduler
            ServerTickEvents.END_SERVER_TICK.register(PHASE_ID, _server -> {
                Scheduler.tick();
            });


            // Register entity display purge
            ServerEntityEvents.ENTITY_LOAD.register(PHASE_ID, (entity, level) -> {
                Elm.onEntityLoad(entity);
                InteractionBlocker.onEntityLoad(entity);
            });
        });




        // Log library loading
        LOGGER.info("FrameworkLib loaded :3");
    }
}
