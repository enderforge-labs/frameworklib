package com.snek.frameworklib.utils;




/**
 * A simple class that throws an assertion error when instantiated.
 * <p>
 * This is meant for utility classes.
 * @since v1.1.0
 */
public class UtilityClassBase {
    protected UtilityClassBase() {
        throw new AssertionError("Utility class \"" + this.getClass().getCanonicalName() + "\" cannot be instantiated");
    }
}
