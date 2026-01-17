package com.snek.frameworklib.debug;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.utils.UtilityClassBase;
















/**
 * Debug-only validation checks.
 * <p>
 * This can be used in conjunction with Java's assertions or as a substitute.
 * <p>
 * These methods have little cost in production, but not always none.
 * All of the parameters are evaluated before the check (apart from the supplier parameter of {@link #condition(BooleanSupplier, String)}).
 * Make sure computing them doesn't add much overhead and doesn't have any side effect.
 * Passing existing values has effectively negligible cost.
 */
public final class Require extends UtilityClassBase {


    /**
     * Checks that a condition is true.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param condition The condition to check.
     * @param message The error message to display if checks fail.
     * @return True.
     */
    public static boolean condition(final boolean condition, final @NotNull String message) {
        if(DebugCheck.isDebug() && !condition) {
            throw new IllegalStateException("Debug check failed: " + message);
        }
        return true;
    }


    /**
     * Checks that a condition is true.
     * <p>
     * Notice: The message parameter is evaluated before the check, even in production.
     * Make sure computing it doesn't add much overhead and doesn't have any side effect.
     * Passing an existing value has effectively negligible cost.
     * @param condition A supplier that computes the condition to check.
     * @param message The error message to display if checks fail.
     * @return True.
     */
    public static boolean condition(final @NotNull BooleanSupplier condition, final @NotNull String message) {
        if(DebugCheck.isDebug()) {
            final Boolean c = condition.getAsBoolean();
            if(c == null || !c) {
                throw new IllegalStateException("Debug check failed: " + message);
            }
        }
        return true;
    }
















    /**
     * Checks that a value is not NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean finite(final double value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_Finite(value, name);
        }
        return true;
    }
    private static boolean __unconditional_Finite(final double value, final @NotNull String name) {
        if(Double.isNaN(value)) {
            throw new IllegalArgumentException(name + " must not be NaN");
        }
        if(Double.isInfinite(value)) {
            throw new IllegalArgumentException(name + " must not be infinite");
        }
        return true;
    }


    /**
     * Checks that a value is not NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean finite(final float value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_Finite(value, name);
        }
        return true;
    }
    private static boolean __unconditional_Finite(final float value, final @NotNull String name) {
        if(Float.isNaN(value)) {
            throw new IllegalArgumentException(name + " must not be NaN");
        }
        if(Float.isInfinite(value)) {
            throw new IllegalArgumentException(name + " must not be infinite");
        }
        return true;
    }
















    /**
     * Checks that an object is not null.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param obj The object to check.
     * @param name The name of the object for the error message.
     * @return True.
     */
    public static boolean nonNull(final @Nullable Object obj, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(obj, name);
        }
        return true;
    }
    private static boolean __unconditional_NonNull(final @Nullable Object obj, final @NotNull String name) {
        if(obj == null) {
            throw new NullPointerException(name + " must not be null");
        }
        return true;
    }
















    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean inRange(final long value, final long min, final long max, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            if(min > max) {
                throw new IllegalArgumentException("Minimum value of " + name + " (" + min + ") must be smaller than the maximum value (" + max + ")");
            }
            if(value < min || value > max) {
                throw new IllegalArgumentException(name + " must be in range [" + min + ", " + max + "], got: " + value);
            }
        }
        return true;
    }


    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean inRange(final int value, final int min, final int max, final @NotNull String name) {
        return inRange((long)value, (long)min, (long)max, name);
    }


    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean inRange(final short value, final short min, final short max, final @NotNull String name) {
        return inRange((long)value, (long)min, (long)max, name);
    }


    /**
     * Checks that a value is within an inclusive range.
     * This check fails if {@code min > max}.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean inRange(final byte value, final byte min, final byte max, final @NotNull String name) {
        return inRange((long)value, (long)min, (long)max, name);
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
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean inRange(final double value, final double min, final double max, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_Finite(value, name);
            __unconditional_Finite(min, name + " min");
            __unconditional_Finite(max, name + " max");
            if(min > max) {
                throw new IllegalArgumentException("Minimum value of " + name + " (" + min + ") must be smaller than the maximum value (" + max + ")");
            }
            if(value < min || value > max) {
                throw new IllegalArgumentException(name + " must be in range [" + min + ", " + max + "], got: " + value);
            }
        }
        return true;
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
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param min The minimum allowed value (inclusive).
     * @param max The maximum allowed value (inclusive).
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean inRange(final float value, final float min, final float max, final @NotNull String name) {
        return inRange((double)value, (double)min, (double)max, name);
    }
















    /**
     * Checks that a value is positive (> 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean positive(final long value, final @NotNull String name) {
        if(DebugCheck.isDebug() && value <= 0) {
            throw new IllegalArgumentException(name + " must be positive, got: " + value);
        }
        return true;
    }


    /**
     * Checks that a value is positive (> 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean positive(final int value, final @NotNull String name) {
        return positive((long)value, name);
    }


    /**
     * Checks that a value is positive (> 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean positive(final short value, final @NotNull String name) {
        return positive((long)value, name);
    }


    /**
     * Checks that a value is positive (> 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean positive(final byte value, final @NotNull String name) {
        return positive((long)value, name);
    }


    /**
     * Checks that a value is positive (> 0).
     * This check also fails if {@code value} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean positive(final double value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_Finite(value, name);
            if(value <= 0) {
                throw new IllegalArgumentException(name + " must be positive, got: " + value);
            }
        }
        return true;
    }


    /**
     * Checks that a value is positive (> 0).
     * This check also fails if {@code value} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean positive(final float value, final @NotNull String name) {
        return positive((double)value, name);
    }
















    /**
     * Checks that a value is non-negative (>= 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean nonNegative(final long value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            if(value < 0) {
                throw new IllegalArgumentException(name + " must be non-negative, got: " + value);
            }
        }
        return true;
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean nonNegative(final int value, final @NotNull String name) {
        return nonNegative((long)value, name);
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean nonNegative(final short value, final @NotNull String name) {
        return nonNegative((long)value, name);
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean nonNegative(final byte value, final @NotNull String name) {
        return nonNegative((long)value, name);
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * This check also fails if {@code value} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean nonNegative(final double value, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_Finite(value, name);
            if(value < 0) {
                throw new IllegalArgumentException(name + " must be non-negative, got: " + value);
            }
        }
        return true;
    }


    /**
     * Checks that a value is non-negative (>= 0).
     * This check also fails if {@code value} is NaN or infinite.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param value The value to check.
     * @param name The name of the value for the error message.
     * @return True.
     */
    public static boolean nonNegative(final float value, final @NotNull String name) {
        return nonNegative((double)value, name);
    }
















    /**
     * Checks that a collection is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param collection The collection to check.
     * @param name The name of the collection for the error message.
     * @return True.
     */
    public static boolean notEmpty(final @Nullable Collection<?> collection, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(collection, name);
            if(collection.isEmpty()) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
        return true;
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param array The array to check.
     * @param name The name of the array for the error message.
     * @return True.
     */
    public static boolean notEmpty(final @Nullable Object[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
        return true;
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param array The array to check.
     * @param name The name of the array for the error message.
     * @return True.
     */
    public static boolean notEmpty(final @Nullable long[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
        return true;
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param array The array to check.
     * @param name The name of the array for the error message.
     * @return True.
     */
    public static boolean notEmpty(final @Nullable int[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
        return true;
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param array The array to check.
     * @param name The name of the array for the error message.
     * @return True.
     */
    public static boolean notEmpty(final @Nullable short[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
        return true;
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param array The array to check.
     * @param name The name of the array for the error message.
     * @return True.
     */
    public static boolean notEmpty(final @Nullable byte[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
        return true;
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param array The array to check.
     * @param name The name of the array for the error message.
     * @return True.
     */
    public static boolean notEmpty(final @Nullable double[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
        return true;
    }


    /**
     * Checks that an array is not empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param array The array to check.
     * @param name The name of the array for the error message.
     * @return True.
     */
    public static boolean notEmpty(final @Nullable float[] array, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(array, name);
            if(array.length == 0) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
        return true;
    }


    /**
     * Checks that a string is not null or empty.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param str The string to check.
     * @param name The name of the string for the error message.
     * @return True.
     */
    public static boolean notEmpty(final @Nullable String str, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(str, name);
            if(str.isEmpty()) {
                throw new IllegalArgumentException(name + " must not be empty");
            }
        }
        return true;
    }
















    /**
     * Checks that two values are equal.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param expected The expected value.
     * @param actual The actual value.
     * @param name The name for the error message.
     * @return True.
     */
    @SuppressWarnings("java:S1221") //! Name too similar to "equals"
    public static boolean equal(final @Nullable Object expected, final @Nullable Object actual, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            if(!Objects.equals(expected, actual)) {
                throw new IllegalStateException(name + ". expected: " + expected + ", but got: " + actual);
            }
        }
        return true;
    }


    /**
     * Checks that an object is an instance of the specified class.
     * This check fails if either {@code object} or {@code expected} are null.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param object The object to check.
     * @param expected The class to check for.
     * @param name The name of the object.
     * @return True.
     */
    public static <T> boolean instanceOf(final @Nullable Object object, final @NotNull Class<T> expected, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(object, name);
            __unconditional_NonNull(expected, "Expected base class of " + name);
            if(!expected.isInstance(object)) {
                throw new IllegalStateException(name + " expected to be an instance of " + expected.getName() + ", but got: " + object.getClass().getName());
            }
        }
        return true;
    }


    /**
     * Checks that an object is not an instance of the specified class.
     * Null objects pass this check.
     * This check fails if {@code excluded} is null.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param object The object to check.
     * @param excluded The class to check for.
     * @param name The name of the object.
     * @return True.
     */
    public static <T> boolean notInstanceOf(final @Nullable Object object, final @NotNull Class<T> excluded, final @NotNull String name) {
        if(DebugCheck.isDebug()) {
            __unconditional_NonNull(excluded, "Excluded base class of " + name);
            if(excluded.isInstance(object)) {
                throw new IllegalStateException(name + " expected to not be an instance of " + excluded.getName() + ", but it is");
            }
        }
        return true;
    }


    /**
     * Fails immediately with a message. Use for execution paths that should never be reached.
     * <p>
     * Notice: All of the parameters are evaluated before the check, even in production.
     * Make sure computing them doesn't add much overhead and doesn't have any side effect.
     * Passing existing values has effectively negligible cost.
     * <p>
     * For true zero cost checks, enclose the code in <code>if(DebugCheck.isDebug()) { ... }</code> or prefix it with {@code assert } (requires -ea)
     * @param message The error message.
     * @return True.
     */
    public static boolean fail(final @NotNull String message) {
        if(DebugCheck.isDebug()) {
            throw new IllegalStateException("Debug check failed: " + message);
        }
        return true;
    }
}