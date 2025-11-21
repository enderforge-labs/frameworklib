package com.snek.frameworklib.utils;




/**
 * A class that throws an assertion error when instantiated.
 */
public class UtilityClassBase {
    protected UtilityClassBase() {
        throw new AssertionError("Utility class \"" + this.getClass().getCanonicalName() + "\" cannot be instantiated");
    }
}
