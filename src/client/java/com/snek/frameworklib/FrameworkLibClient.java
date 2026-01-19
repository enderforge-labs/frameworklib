package com.snek.frameworklib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;








/**
 * A client-side mod not meant for gameplay.
 * <p>
 * Some aspects of text-based graphics depend on the rendered size of the TextDisplay, which depends on the font.
 * <p>
 * This mod is used to pre-calculate the width and height of each character of the active font.
 * This data is then used by the library to compute entity dimensions in runtime.
 * @since v1.1.0
 */
public class FrameworkLibClient implements ClientModInitializer {
    private static boolean generated = false;


    @Override
    @SuppressWarnings("java:S2696") //! Setting static member from non-static method
    public void onInitializeClient() {
        ClientTickEvents.END_WORLD_TICK.register(client -> {
            if(!generated) {
                FontDataGenerator.generate();
                generated = true;
            }
        });
    }
}