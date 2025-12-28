package com.snek.frameworklib.configs;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworkconfig.ConfigManager;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.utils.UtilityClassBase;








/**
 * A utility class that contains configuration file data.
 */
public final class Configs extends UtilityClassBase {
    private Configs() {}

    // Data
    private static @NotNull GraphicsConfig    graphics = null;
    private static @NotNull PerformanceConfig perf     = null;

    // Getters
    public static @NotNull GraphicsConfig    getGraphics() { return graphics; }
    public static @NotNull PerformanceConfig getPerf    () { return perf;     }




    /**
     * Loads the configuration files or creates new ones if they are missing.
     */
    public static void loadConfigs() {
        graphics = ConfigManager.loadConfig("Graphics",    GraphicsConfig.class,    FrameworkLib.LIB_ID);
        perf     = ConfigManager.loadConfig("Performance", PerformanceConfig.class, FrameworkLib.LIB_ID);

        assert Require.nonNull(graphics, "graphics config data");
        assert Require.nonNull(perf,     "performance config data");
    }
}
