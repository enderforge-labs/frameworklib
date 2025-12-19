package com.snek.frameworklib.debug;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.utils.UtilityClassBase;
















/**
 * Debug-only validation checks.
 * <p>
 * This is meant to be used as a replacement for Java's assertions.
 * <p>
 * These methods have little cost in production, but not always none.
 * All of the parameters are evaluated before the check (apart from the supplier parameter of {@link #require(Supplier, String)}).
 * Make sure computing them doesn't add much overhead and doesn't have any side effect.
 * Passing existing values has effectively negligible cost.
 */
public final class Assert extends UtilityClassBase {


    /**
     * Checks that a condition is true.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param condition The condition to check.
     * @param message The error message to display if checks fail.
     */
    public static void require(final boolean condition, final @NotNull String message) {
        if(DebugCheck.isDebug() && !condition) {
            throw new IllegalStateException("Debug check failed: " + message);
        }
    }


    /**
     * Checks that a condition is true.
     * <p>
     * Notice: The message parameter is evaluated before the check, even in production.
     * Make sure computing it doesn't add much overhead and doesn't have any side effect.
     * Passing an existing value has effectively negligible cost.
     * @param condition A supplier that computes the condition to check.
     * @param message The error message to display if checks fail.
     */
    public static void require(final @NotNull Supplier<@NotNull Boolean> condition, final @NotNull String message) {
        if(DebugCheck.isDebug()) {
            final Boolean c = condition.get();
            if(c == null || !c) {
                throw new IllegalStateException("Debug check failed: " + message);
            }
        }
    }
















    /**
     * Checks that a value is not NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requireFinite(final double value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireFinite(value, name);
        }
    }
    private static void __unconditional_requireFinite(final double value, final @NotNull String name) {
        if(Double.isNaN(value)) {
            throw new IllegalArgumentException(name + " must not be NaN");
        }
        if(Double.isInfinite(value)) {
            throw new IllegalArgumentException(name + " must not be infinite");
        }
    }


    /**
     * Checks that a value is not NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requireFinite(final float value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireFinite(value, name);
        }
    }
    private static void __unconditional_requireFinite(final float value, final @NotNull String name) {
        if(Float.isNaN(value)) {
            throw new IllegalArgumentException(name + " must not be NaN");
        }
        if(Float.isInfinite(value)) {
            throw new IllegalArgumentException(name + " must not be infinite");
        }
    }
















    /**
     * Checks that an object is not null.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param obj The object to check.
     * @param name The name of the object for the error message.
     */
    public static void requireNonNull(final @Nullable Object obj, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(obj, name);
        }
    }
    private static void __unconditional_requireNonNull(final @Nullable Object obj, final @NotNull String name) {
        if(obj == null) {
            throw new NullPointerException(name + " must not be null");
        }
    }
















    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     */
    public static void requireInRange(final long value, final long min, final long max, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            if(min > max) {
                throw new IllegalArgumentException("Minimum value of " + name + "(" + min + ") must be smaller than the maximum value (" + max + ")");
            }
            if(value < min || value > max) {
                throw new IllegalArgumentException(name + " must be in range [" + min + ", " + max + "], got: " + value);
            }
        }
    }


    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     */
    public static void requireInRange(final int value, final int min, final int max, final @NotNull String name) {
        requireInRange((long)value, (long)min, (long)max, name);
    }


    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     */
    public static void requireInRange(final short value, final short min, final short max, final @NotNull String name) {
        requireInRange((long)value, (long)min, (long)max, name);
    }


    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     */
    public static void requireInRange(final byte value, final byte min, final byte max, final @NotNull String name) {
        requireInRange((long)value, (long)min, (long)max, name);
    }


    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * This check fails if either {@code value}, {@code min} or {@code max} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     */
    public static void requireInRange(final double value, final double min, final double max, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireFinite(value, name);
            __unconditional_requireFinite(min, name + " min");
            __unconditional_requireFinite(max, name + " max");
            if(min > max) {
                throw new IllegalArgumentException("Minimum value of " + name + "(" + min + ") must be smaller than the maximum value (" + max + ")");
            }
            if(value < min || value > max) {
                throw new IllegalArgumentException(name + " must be in range [" + min + ", " + max + "], got: " + value);
            }
        }
    }


    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * This check fails if either {@code value}, {@code min} or {@code max} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     */
    public static void requireInRange(final float value, final float min, final float max, final @NotNull String name) {
        requireInRange((double)value, (double)min, (double)max, name);
    }
















    /**
     * Checks that a value is positive (> 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requirePositive(final long value, final @NotNull String name) {
        if(DebugCheck.isDebug() && value <= 0) {
            throw new IllegalArgumentException(name + " must be positive, got: " + value);
        }
    }


    /**
     * Checks that a value is positive (> 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requirePositive(final int value, final @NotNull String name) {
        requirePositive((long)value, name);
    }


    /**
     * Checks that a value is positive (> 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requirePositive(final short value, final @NotNull String name) {
        requirePositive((long)value, name);
    }


    /**
     * Checks that a value is positive (> 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requirePositive(final byte value, final @NotNull String name) {
        requirePositive((long)value, name);
    }


    /**
     * Checks that a value is positive (> 0).
     * This check also fails if {@code value} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requirePositive(final double value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireFinite(value, name);
            if(value <= 0) {
                throw new IllegalArgumentException(name + " must be positive, got: " + value);
            }
        }
    }


    /**
     * Checks that a value is positive (> 0).
     * This check also fails if {@code value} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requirePositive(final float value, final @NotNull String name) {
        requirePositive((double)value, name);
    }
















    /**
     * Checks that a value is non-negative (>= 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requireNonNegative(final long value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            if(value < 0) {
                throw new IllegalArgumentException(name + " must be non-negative, got: " + value);
            }
        }
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requireNonNegative(final int value, final @NotNull String name) {
        requireNonNegative((long)value, name);
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requireNonNegative(final short value, final @NotNull String name) {
        requireNonNegative((long)value, name);
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requireNonNegative(final byte value, final @NotNull String name) {
        requireNonNegative((long)value, name);
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * This check also fails if {@code value} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requireNonNegative(final double value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireFinite(value, name);
            if(value < 0) {
                throw new IllegalArgumentException(name + " must be non-negative, got: " + value);
            }
        }
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * This check also fails if {@code value} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param value The value to check.
     * @param name The name of the value for the error message.
     */
    public static void requireNonNegative(final float value, final @NotNull String name) {
        requireNonNegative((double)value, name);
    }
















    /**
     * Checks that a collection is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param collection The collection to check.
     * @param name The name of the collection for the error message.
     */
    public static void requireNotEmpty(final @Nullable Collection<?> collection, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(collection, name);
            if(collection.isEmpty()) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param array The array to check.
     * @param name The name of the array for the error message.
     */
    public static void requireNotEmpty(final @Nullable Object[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param array The array to check.
     * @param name The name of the array for the error message.
     */
    public static void requireNotEmpty(final @Nullable long[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param array The array to check.
     * @param name The name of the array for the error message.
     */
    public static void requireNotEmpty(final @Nullable int[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param array The array to check.
     * @param name The name of the array for the error message.
     */
    public static void requireNotEmpty(final @Nullable short[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param array The array to check.
     * @param name The name of the array for the error message.
     */
    public static void requireNotEmpty(final @Nullable byte[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param array The array to check.
     * @param name The name of the array for the error message.
     */
    public static void requireNotEmpty(final @Nullable double[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param array The array to check.
     * @param name The name of the array for the error message.
     */
    public static void requireNotEmpty(final @Nullable float[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
    }


    /**
     * Checks that a string is not null or empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param str The string to check.
     * @param name The name of the string for the error message.
     */
    public static void requireNotEmpty(final @Nullable String str, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(str, name);
            if(str.isEmpty()) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
    }
















    /**
     * Checks that two values are equal.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param expected The expected value.
     * @param actual The actual value.
     * @param name The name for the error message.
     */
    public static void requireEqual(final @Nullable Object expected, final @Nullable Object actual, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            if(!Objects.equals(expected, actual)) {
                throw new IllegalStateException(name + ". expected: " + expected + ", but got: " + actual);
            }
        }
    }


    /**
     * Checks that an object is an instance of the specified class.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param object The object to check.
     * @param expected The class to check for.
     * @param name The name of the object.
     */
    public static <T> void requireInstanceOf(final @Nullable Object object, final @NotNull Class<T> expected, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_requireNonNull(object, name);
            __unconditional_requireNonNull(expected, "Expected base class of " + name);
            if(!expected.isInstance(object)) {
                throw new IllegalStateException(name + " expected to be an instance of " + expected.getName() + ", but got: " + object.getClass().getName());
            }
        }
    }


    /**
     * Fails immediately with a message. Use for execution paths that should never be reached.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()){ ... }</code>
     * @param message The error message.
     */
    public static void fail(final @NotNull String message) {
        if(DebugCheck.isDebug()) {
            throw new IllegalStateException("Debug check failed: " + message);
        }
    }
}