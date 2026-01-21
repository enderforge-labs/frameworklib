package com.snek.frameworklib.debug;

import java.util.List;

import com.snek.frameworklib.utils.UtilityClassBase;




/**
 * Utility class that can check if the mod is currently being ran in debug mode.
 * @since v1.1.0
 */
public final class DebugCheck extends UtilityClassBase {
    private static boolean IS_DEBUG;


    // Scan arguments and look for jdwp
    static {
        final List<String> args = java.lang.management.ManagementFactory
            .getRuntimeMXBean()
            .getInputArguments()
        ;
        IS_DEBUG = false;
        for(final String arg : args) {
            if(arg.contains("-agentlib:jdwp") || arg.contains("-Xrunjdwp")) {
                IS_DEBUG = true;
                break;
            }
        }
    }




    /**
     * Checks if the mod is currently being ran in debug mode.
     * @return True if it's in debug mode. False otherwise.
     */
    public static boolean isDebug() {
        return IS_DEBUG;
    }
}