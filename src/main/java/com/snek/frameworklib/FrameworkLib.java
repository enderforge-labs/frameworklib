package com.snek.frameworklib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworkconfig.FrameworkConfig;
import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.input.MessageReceiver;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;








public class FrameworkLib implements ModInitializer {
    public static final String LIB_ID = "frameworklib";
    public static final ResourceLocation INIT_PHASE_ID = new ResourceLocation(LIB_ID, "init");


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


        // Make sure Framework Config is initialized before Framework Lib
        ServerLifecycleEvents.SERVER_STARTING.addPhaseOrdering(
            FrameworkConfig.INIT_PHASE_ID,
            INIT_PHASE_ID
        );


        // Register initialization
        ServerLifecycleEvents.SERVER_STARTING.register(INIT_PHASE_ID, server -> {
            serverInstance = server;

            // Read config files
            if(!Configs.loadConfigs()) return;

            // Register chat input handler
            ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
                return !MessageReceiver.onMessage(message, sender);
            });
        });


        // Log library loading
        System.out.println("FrameworkLib loaded :3");
    }
}
