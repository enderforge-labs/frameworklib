package com.snek.frameworklib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.configs.Configs;
import com.snek.frameworklib.input.MessageReceiver;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.MinecraftServer;








public class FrameworkLib implements ModInitializer {
    public static final String LIB_ID = "framework-lib";


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
        System.out.println("FrameworkLib loaded :3");


        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            serverInstance = server;

            // Read config files
            Configs.loadConfigs();




            // Register chat input handler
            ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
                return !MessageReceiver.onMessage(message, sender);
            });
        });
    }
}
