package com.snek.frameworklib.configs;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworkconfig.ConfigManager;
import com.snek.frameworklib.FrameworkLib;








/**
 * A utility class that contains configuration file data.
 */
public abstract class Configs {

    private Configs() {}

    private static @NotNull UiConfig          ui   = null;
    private static @NotNull PerformanceConfig perf = null;

    public static @NotNull UiConfig          getUi  (){ return ui;   }
    public static @NotNull PerformanceConfig getPerf(){ return perf; }




    /**
     * Loads the configuration files or creates new ones if they are missing.
     */
    public static boolean loadConfigs() {
        ui   = ConfigManager.loadConfig("UiConfig",    UiConfig.class,          FrameworkLib.LIB_ID);
        perf = ConfigManager.loadConfig("Performance", PerformanceConfig.class, FrameworkLib.LIB_ID);
        return ui != null && perf != null;
    }
}
