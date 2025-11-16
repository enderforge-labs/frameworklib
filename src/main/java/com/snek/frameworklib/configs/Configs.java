package com.snek.frameworklib.configs;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworkconfig.ConfigManager;
import com.snek.frameworklib.FrameworkLib;








/**
 * A utility class that contains configuration file data.
 */
public abstract class Configs {
    private Configs() {}
    public static @NotNull UiConfig ui = null;
    public static @NotNull PerformanceConfig perf = null;




    /**
     * Loads the configuration files or creates new ones if they are missing.
     */
    public static void loadConfigs() {
        ui   = ConfigManager.loadConfig("UiConfig",    UiConfig.class,          FrameworkLib.LIB_ID);
        perf = ConfigManager.loadConfig("Performance", PerformanceConfig.class, FrameworkLib.LIB_ID);
    }
}
