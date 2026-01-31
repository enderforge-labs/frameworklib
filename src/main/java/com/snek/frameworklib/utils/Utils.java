package com.snek.frameworklib.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.debug.Require;








/**
 * A utility class that provides a collection of handy methods.
 * @since v1.1.0
 */
public final class Utils extends UtilityClassBase {
    private Utils() {}

    // Formatters
    private static final NumberFormat formatterPrice  = new DecimalFormat("#,##0");
    private static final NumberFormat formatterAmount = new DecimalFormat("#,###");




    /**
     * Checks if a double value is within a certain threshold from a target value.
     * <p>
     * This is used to avoid precision related problems when comparing double values.
     * @param n The value to check
     * @param target The target value
     * @param threshold The threshold to use
     * @return True if the value is within the threshold, false otherwise
     */
    public static boolean doubleEquals(final double n, final double target, final double threshold) {
        return !(n < target - threshold || n > target + threshold);
    }


    /**
     * Checks if a float value is within a certain threshold from a target value.
     * <p>
     * This is used to avoid precision related problems when comparing float values.
     * @param n The value to check
     * @param target The target value
     * @param threshold The threshold to use
     * @return True if the value is within the threshold, false otherwise
     */
    public static boolean floatEquals(final float n, final float target, final float threshold) {
        return !(n < target - threshold || n > target + threshold);
    }


    /**
     * Invokes a Method on the target object using the specified arguments.
     * @param method The method to invoke.
     * @param target The target Object.
     * @param args The arguments to use. Can be empty.
     * @return The return value of the method.
     */
    public static @Nullable Object invokeSafe(final @NotNull Method method, final @NotNull Object target, final @Nullable Object... args) {
        assert Require.nonNull(method, "method");
        assert Require.nonNull(target, "target");

        try {
            return method.invoke(target, args);
        }
        catch(final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            FrameworkLib.LOGGER.error("Failed to invoke the specified method", e);
        }
        return null;
    }




    /**
     * Runs a task on a secondary thread after a specified delay.
     * @param delay The delay expressed in milliseconds.
     * @param task The task to run.
     */
    public static void runAsync(final int delay, final @NotNull Runnable task) {
        assert Require.nonNegative(delay, "delay");
        assert Require.nonNull(task, "task");


        new Thread(() -> {

            // Wait for the delay
            try {
                Thread.sleep(delay);
            }
            catch(final InterruptedException e) {
                FrameworkLib.LOGGER.warn("Async task interrupted, proceeding with execution", e);
                Thread.currentThread().interrupt();
            }

            // Run task
            try {
                task.run();
            }
            catch(final Exception e) {
                FrameworkLib.LOGGER.error("Async task failed", e);
            }

        }).start();
    }










    /**
     * Returns the value <amount> expressed as a string, abbreviating the number to 3 digits.
     * @param amount The amount to format. This MUST be >= 0.
     * @return The formatted amount.
     */
    public static @NotNull String formatAmountShort(final long amount) {
        assert Require.nonNegative(amount, "amount");
        return formatPriceShort(amount * 100l, "");
    }


    /**
     * Returns the value <price> expressed as a string and formatted using $ as currency symbol, abbreviating the number to 3 digits.
     * @param price The price to format. This MUST be >= 0.
     * @return The formatted price.
     */
    public static @NotNull String formatPriceShort(final long price) {
        assert Require.nonNegative(price, "price");
        return formatPriceShort(price, "$");
    }


    /**
     * Returns the value <price> expressed as a string and formatted as specified, abbreviating the number to 3 digits.
     * @param price The price to format. This MUST be >= 0.
     * @param currency The currency symbol to use as prefix.
     * @return The formatted price.
     */
    public static @NotNull String formatPriceShort(final long price, final @NotNull String currency) {
        assert Require.nonNegative(price, "price");
        assert Require.nonNull(currency, "currency");


        final String[] suffixes = { "", "k", "m", "b", "t", "q" };

        // Calculate exponent
        int exp = 0;
        long scaled = price;
        while(scaled >= 1000l * 100l && exp < suffixes.length - 1) {
            scaled /= 1000l;
            exp++;
        }

        // Split the value into units and cents
        final long units = scaled / 100l;
        final long cents = scaled % 100l;

        // Find optimal formatting
        String formatted =
            units < 10  ? String.format("%d.%02d", units, cents) :
            units < 100 ? String.format("%d.%d",   units, cents / 10) :
            Long.toString(units)
        ;

        // Trim trailing .00 or .0
        formatted = formatted.replaceAll("\\.0+$", "");

        // Return the formatted price with prefix and suffix added
        return currency + formatted + suffixes[exp];
    }




    /**
     * Returns the value <price> expressed as a string and formatted using $ as currency symbol and thousands separators.
     * @param price The price to format. This MUST be >= 0.
     * @return The formatted price.
     */
    public static @NotNull String formatPrice(final long price) {
        assert Require.nonNegative(price, "price");
        return formatPrice(price, "$", true);
    }


    /**
     * Returns the value <price> expressed as a string and formatted as specified.
     * @param price The price to format. This MUST be >= 0.
     * @param currency The currency symbol to use as prefix.
     * @param thousandsSeparator Whether to use a separator between thousands
     * @return The formatted price.
     */
    public static @NotNull String formatPrice(final long price, final @NotNull String currency, final boolean thousandsSeparator) {
        assert Require.nonNegative(price, "price");
        assert Require.nonNull(currency, "currency");

        final long units = price / 100l;
        final long cents = price % 100l;

        // Calculate formatted string with no currency symbol
        final String r = thousandsSeparator?
            formatterPrice.format(units) + "." + (cents < 10 ? "0" + cents : cents):
            String.format("%d.%02d", units, cents)
        ;

        // Add currency and return the string
        return currency + r;
    }








    /**
     * Returns the value <amount> expressed as a string and formatted using thousands separators.
     * @param amount The amount to format.
     * @return The formatted price.
     */
    public static @NotNull String formatAmount(final long amount) {
        assert Require.nonNegative(amount, "amount");
        return formatAmount(amount, false, true);
    }

    /**
     * Returns the value <amount> expressed as a string and formatted as specified.
     * @param amount The amount to format.
     * @param x Whether the amount should be prefixed with a lowercase "x".
     * @param thousandsSeparator Whether to use a separator between thousands.
     * @return The formatted price.
     */
    public static @NotNull String formatAmount(final long amount, final boolean x, final boolean thousandsSeparator) {
        assert Require.nonNegative(amount, "amount");
        final String r;

        // Separator
        if(thousandsSeparator) {
            r = formatterAmount.format(amount);
        }

        // No separator
        else {
            r = String.valueOf(amount);
        }

        // Add trailing x if requested
        return x ? r + "x" : r;
    }






    public enum SizeLabelType {
        SYMBOL,
        FULL
    }

    @SuppressWarnings("java:S115") //! Bad enum constant name
    public enum SizeUnits {
        b, B,
        KB,  MB,  GB,  TB,  PB,  EB,  ZB,  YB,
        KiB, MiB, GiB, TiB, PiB, EiB, ZiB, YiB
    }


    /**
     * Returns the value <bytes> expressed as a string in the specified unit.
     * @param bytes The number of bytes to format. This MUST be >= 0.
     * @param unit The size unit to display the value in.
     * @return The formatted byte size.
     */
    public static @NotNull String formatSize(final long bytes, final @NotNull SizeUnits unit) {
        assert Require.nonNegative(bytes, "bytes");
        assert Require.nonNull(unit, "unit");
        return formatSize(bytes, unit, SizeLabelType.SYMBOL, 2);
    }


    /**
     * Returns the value <bytes> expressed as a string in the specified unit.
     * @param bytes The number of bytes to format. This MUST be >= 0.
     * @param unit The size unit to display the value in.
     * @param labelType The label type (SYMBOL or FULL).
     * @return The formatted byte size.
     */
    public static @NotNull String formatSize(final long bytes, final @NotNull SizeUnits unit, final @NotNull SizeLabelType labelType) {
        assert Require.nonNegative(bytes, "bytes");
        assert Require.nonNull(unit, "unit");
        assert Require.nonNull(labelType, "labelType");
        return formatSize(bytes, unit, labelType, 2);
    }


    /**
     * Returns the value <bytes> expressed as a string in the specified unit.
     * @param bytes The number of bytes to format. This MUST be >= 0.
     * @param unit The size unit to display the value in.
     * @param labelType The label type (SYMBOL or FULL).
     * @param precision The maximum number of decimal places to show. This is redundant is displaying in Bits.
     * @return The formatted byte size.
     */
    @SuppressWarnings("java:S3457") //! Concatenation in String.format
    public static @NotNull String formatSize(final long bytes, final @NotNull SizeUnits unit, final @NotNull SizeLabelType labelType, final int precision) {
        assert Require.nonNegative(bytes, "bytes");
        assert Require.nonNull(unit, "unit");
        assert Require.nonNull(labelType, "labelType");
        assert Require.nonNegative(precision, "precision");

        // Custom logic for bits
        if(unit == SizeUnits.b) {
            final long bits = bytes * 8L;
            return String.format("%d %s", bits, getSizeLabel(labelType, bits, unit));
        }

        // Determine if using binary (1024) or decimal (1000) units
        final boolean isBinary = unit.name().endsWith("iB");
        final long divisor = isBinary ? 1024L : 1000L;

        // Get the exponent for the requested unit
        final int exp = switch(unit) {
            case b -> 0;
            case B -> 0;
            case KB, KiB -> 1;
            case MB, MiB -> 2;
            case GB, GiB -> 3;
            case TB, TiB -> 4;
            case PB, PiB -> 5;
            case EB, EiB -> 6;
            case ZB, ZiB -> 7;
            case YB, YiB -> 8;
        };

        // Calculate the value in the requested unit
        final double value = bytes / Math.pow(divisor, exp);

        // Format with the specified precision
        final String formatted = String.format("%." + precision + "f", value)
            .replaceAll("\\.0+$", "")                // Remove trailing .00
            .replaceAll("(\\.\\d*[1-9])0+$", "$1")   // Remove trailing zeros after decimal point
        ;

        // Get the appropriate label
        final String label = getSizeLabel(labelType, (long)value, unit);

        // Return the formatted size with label
        return formatted + " " + label;
    }


    private static @NotNull String getSizeLabel(final @NotNull SizeLabelType type, final long value, final @NotNull SizeUnits scale) {
        return switch(type) {
            case SYMBOL -> switch(scale) {
                case b  -> "b";
                case B  -> "B";
                case KB -> "KB"; case KiB -> "KiB";
                case MB -> "MB"; case MiB -> "MiB";
                case GB -> "GB"; case GiB -> "GiB";
                case TB -> "TB"; case TiB -> "TiB";
                case PB -> "PB"; case PiB -> "PiB";
                case EB -> "EB"; case EiB -> "EiB";
                case ZB -> "ZB"; case ZiB -> "ZiB";
                case YB -> "YB"; case YiB -> "YiB";
            };
            case FULL -> switch(scale) {
                case b  -> "Bit";
                case B  -> "Byte";
                case KB ->  "Kilobyte"; case KiB -> "Kibibyte";
                case MB ->  "Megabyte"; case MiB -> "Mebibyte";
                case GB ->  "Gigabyte"; case GiB -> "Gibibyte";
                case TB ->  "Terabyte"; case TiB -> "Tebibyte";
                case PB ->  "Petabyte"; case PiB -> "Pebibyte";
                case EB ->   "Exabyte"; case EiB -> "Exbibyte";
                case ZB -> "Zettabyte"; case ZiB -> "Zebibyte";
                case YB -> "Yottabyte"; case YiB -> "Yobibyte";
            } + (value != 1 ? "s" : "");
        };
    }








    public enum DurationLabelType { INITIAL, FULL }
    public enum DurationPrecision { SECONDS, MINUTES }
    private enum DurationUnits { MS, S, M, H, D, MO, Y }

    /**
     * Returns the duration <ticks> expressed as a string and formatted as specified.
     * This includes the number of milliseconds (optional), seconds (optional), minutes, hours, days, months, and years.
     * @param ticks The duration to format, expressed in ticks (20th of a second).
     * @param labelType The type of label to suffix numbers with.
     * @return The formatted duration.
     */
    public static @NotNull String formatDuration(final long ticks, final @NotNull DurationLabelType labelType) {
        return formatDuration(ticks, labelType, null);
    }


    /**
     * Returns the duration <ticks> expressed as a string and formatted as specified.
     * This includes the number of milliseconds (optional), seconds (optional), minutes, hours, days, months, and years.
     * @param ticks The duration to format, expressed in ticks (20th of a second).
     * @param labelType The type of label to suffix numbers with.
     * @param separator The separator characters to add between numbers. Can be null (equivalent to an empty string).
     * @return The formatted duration.
     */
    public static @NotNull String formatDuration(final long ticks, final @NotNull DurationLabelType labelType, final @Nullable String separator) {
        return formatDuration(ticks, labelType, separator, DurationPrecision.SECONDS);
    }


    /**
     * Returns the duration <ticks> expressed as a string and formatted as specified.
     * This includes the number of milliseconds (optional), seconds (optional), minutes, hours, days, months, and years.
     * @param ticks The duration to format, expressed in ticks (20th of a second).
     * @param labelType The type of label to suffix numbers with.
     * @param separator The separator characters to add between numbers. Can be null (equivalent to an empty string).
     * @param precision The precision of the formatted string.
     * @return The formatted duration.
     */
    public static @NotNull String formatDuration(
        final long ticks,
        final @NotNull DurationLabelType labelType,
        final @Nullable String separator,
        final @NotNull DurationPrecision precision
    ) {

        // Calculate separator string and requirement flag
        final StringBuilder r = new StringBuilder();
        final String sep = separator != null ? separator : "";
        final boolean needSep = !sep.isEmpty();


        // Convert ticks to total seconds and round to multiples of 60 if precision is MINUTES
        long totalSeconds = ticks / 20;
        if(precision == DurationPrecision.MINUTES) totalSeconds = (totalSeconds / 60) * 60;


        // Calculate time components
        final long years   =  totalSeconds / 31536000;              // 365 days
        final long months  = (totalSeconds % 31536000) / 2592000;   // 30 days
        final long days    = (totalSeconds %  2592000) /   86400;   //
        final long hours   = (totalSeconds %    86400) /    3600;   // 3000 seconds
        final long minutes = (totalSeconds %     3600) /      60;   //
        final long seconds =  totalSeconds % 60;                    //
        final long millis  = (ticks % 20) * 50;                     // Remaining ticks to milliseconds


        // Build the formatted string
        if(years > 0) {
            r.append(years).append(getTimeLabel(labelType, years, DurationUnits.Y));
        }

        if(months > 0) {
            if(needSep) r.append(sep);
            r.append(months);
            r.append(getTimeLabel(labelType, months, DurationUnits.MO));
        }

        if(days > 0) {
            if(needSep) r.append(sep);
            r.append(days);
            r.append(getTimeLabel(labelType, days, DurationUnits.D));
        }

        if(hours > 0) {
            if(needSep) r.append(sep);
            r.append(hours);
            r.append(getTimeLabel(labelType, hours, DurationUnits.H));
        }

        if(minutes > 0) {
            if(needSep) r.append(sep);
            r.append(minutes);
            r.append(getTimeLabel(labelType, minutes, DurationUnits.M));
        }

        if(precision == DurationPrecision.SECONDS) {
            if(seconds > 0 || totalSeconds == 0) {
                if(needSep) r.append(sep);
                r.append(seconds);
                r.append(getTimeLabel(labelType, seconds, DurationUnits.S));
            }

            if(millis > 0) {
                if(needSep) r.append(sep);
                r.append(millis);
                r.append(getTimeLabel(labelType, millis, DurationUnits.MS));
            }
        }


        // Handle edge case of zero duration
        if(r.isEmpty()) {
            r.append("0");
            r.append(getTimeLabel(labelType, 0, DurationUnits.S));
        }

        // Return
        return r.toString();
    }


    private static @NotNull String getTimeLabel(final @NotNull DurationLabelType type, final long value, final @NotNull DurationUnits scale) {
        return switch(type) {
            case INITIAL -> switch(scale) {
                case MS -> "ms";
                case S  -> "s";
                case M  -> "m";
                case H  -> "h";
                case D  -> "d";
                case MO -> "mo";
                case Y  -> "y";
            };
            case FULL -> " " + switch(scale) {
                case MS -> "millisecond";
                case S  -> "second";
                case M  -> "minute";
                case H  -> "hour";
                case D  -> "day";
                case MO -> "month";
                case Y  -> "year";
            } + (value > 1 ? "s" : "");
        };
    }








    /**
     * Converts an RGB color to a shade of gray by setting its saturation to 0.
     * @param rgb The RGB color.
     * @return The shade of gray expressed as an RGB color.
     */
    public static @NotNull Vector3i toBW(final @NotNull Vector3i rgb) {
        assert Require.nonNull(rgb, "rgb");
        assert Require.inRange(rgb.x, 0, 255, "red");
        assert Require.inRange(rgb.y, 0, 255, "green");
        assert Require.inRange(rgb.z, 0, 255, "blue");
        return HSVtoRGB(toBW(RGBtoHSV(rgb)));
    }

    /**
     * Converts an HSV color to a shade of gray by setting its saturation to 0.
     * @param hsv The HSV color.
     * @return The shade of gray expressed as an HSV color.
     */
    public static @NotNull Vector3f toBW(final @NotNull Vector3f hsv) {
        assert Require.nonNull(hsv, "hsv");
        assert Require.inRange(hsv.x, 0, 360, "hue");
        assert Require.inRange(hsv.y, 0, 1, "saturation");
        assert Require.inRange(hsv.z, 0, 1, "value");
        return hsv.mul(1, 0, 1, new Vector3f());
    }




    /**
     * Converts an RGB color to HSV.
     * <p> Red:   0 to 255
     * <p> Green: 0 to 255
     * <p> Blue:  0 to 255
     * <p> Hue:         0 to 360.0
     * <p> Saturation:  0 to 1.0
     * <p> Value:       0 to 1.0
     * @param rgb The RGB color.
     * @return The color as an HSV value.
     */
    public static @NotNull Vector3f RGBtoHSV(final @NotNull Vector3i rgb) {
        assert Require.nonNull(rgb, "rgb");
        assert Require.inRange(rgb.x, 0, 255, "red");
        assert Require.inRange(rgb.y, 0, 255, "green");
        assert Require.inRange(rgb.z, 0, 255, "blue");


        final float r = rgb.x / 255.0f;
        final float g = rgb.y / 255.0f;
        final float b = rgb.z / 255.0f;

        final float max = Math.max(r, Math.max(g, b));
        final float min = Math.min(r, Math.min(g, b));
        final float delta = max - min;

        float h = 0;
        final float s;
        final float v = max;


        if(max != 0) {
            s = delta / max;
        }
        else {
            return new Vector3f(-1, 0, v);
        }

        if(delta == 0) {
            h = 0;
        }
        else if(r == max) {
            h = (g - b) / delta;
        }
        else if(g == max) {
            h = 2 + (b - r) / delta;
        }
        else {
            h = 4 + (r - g) / delta;
        }

        h *= 60;
        if(h < 0) h += 360;

        return new Vector3f(h, s, v);
    }




    /**
     * Converts an HSV color to RGB.
     * <p> Red:   0 to 255
     * <p> Green: 0 to 255
     * <p> Blue:  0 to 255
     * <p> Hue:         0 to 360.0
     * <p> Saturation:  0 to 1.0
     * <p> Value:       0 to 1.0
     * @param hsv The HSV color.
     * @return The color as an HSV value.
     */
    public static @NotNull Vector3i HSVtoRGB(final @NotNull Vector3f hsv) {
        assert Require.nonNull(hsv, "hsv");
        assert Require.inRange(hsv.x, 0, 360, "hue");
        assert Require.inRange(hsv.y, 0, 1, "saturation");
        assert Require.inRange(hsv.z, 0, 1, "value");


        final float h = hsv.x;
        final float s = hsv.y;
        final float v = hsv.z;

        final float c = v * s;
        final float x = c * (1 - Math.abs(((h / 60) % 2) - 1));
        final float m = v - c;

        float r = 0;
        float g = 0;
        float b = 0;

        if(0 <= h && h < 60) {
            r = c; g = x; b = 0;
        }
        else if(60 <= h && h < 120) {
            r = x; g = c; b = 0;
        }
        else if(120 <= h && h < 180) {
            r = 0; g = c; b = x;
        }
        else if(180 <= h && h < 240) {
            r = 0; g = x; b = c;
        }
        else if(240 <= h && h < 300) {
            r = x; g = 0; b = c;
        }
        else if(300 <= h && h < 360) {
            r = c; g = 0; b = x;
        }

        r += m; g += m; b += m;

        return new Vector3i(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }




    /**
     * Interpolates two RGB colors while maintaining luminosity.
     * @param rgb1 The starting color.
     * @param rgb2 The target color
     * @param factor The interpolation factor.
     * @return The resulting color.
     */
    public static @NotNull Vector3i interpolateRGB(final @NotNull Vector3i rgb1, final @NotNull Vector3i rgb2, final float factor) {
        assert Require.nonNull(rgb1, "rgb 1");
        assert Require.inRange(rgb1.x, 0, 255, "red 1");
        assert Require.inRange(rgb1.y, 0, 255, "green 1");
        assert Require.inRange(rgb1.z, 0, 255, "blue 1");
        assert Require.nonNull(rgb2, "rgb 2");
        assert Require.inRange(rgb2.x, 0, 255, "red 2");
        assert Require.inRange(rgb2.y, 0, 255, "green 2");
        assert Require.inRange(rgb2.z, 0, 255, "blue 2");


        final Vector3f hsv1 = RGBtoHSV(rgb1);
        final Vector3f hsv2 = RGBtoHSV(rgb2);

        float h1 = hsv1.x;
        final float s1 = hsv1.y;
        final float v1 = hsv1.z;

        float h2 = hsv2.x;
        final float s2 = hsv2.y;
        final float v2 = hsv2.z;

        // Adjust hue to allow the interpolation to take the shortest path
        if(Math.abs(h1 - h2) > 180) {
            if(h1 > h2) h2 += 360;
            else h1 += 360;
        }

        // Interpolate values and return color vector
        return HSVtoRGB(new Vector3f(
            interpolateF(h1, h2, factor) % 360,
            interpolateF(s1, s2, factor),
            interpolateF(v1, v2, factor)
        ));
    }




    /**
     * Interpolates two ARGB colors while maintaining luminosity.
     * @param argb1 The starting color.
     * @param argb2 The target color
     * @param factor The interpolation factor.
     * @return The resulting color.
     */
    public static @NotNull Vector4i interpolateARGB(final @NotNull Vector4i argb1, final @NotNull Vector4i argb2, final float factor) {
        assert Require.nonNull(argb1, "argb 1");
        assert Require.inRange(argb1.x, 0, 255, "alpha 1");
        assert Require.nonNull(argb2, "argb 2");
        assert Require.inRange(argb2.x, 0, 255, "alpha 2");


        final Vector3i rgbRet = interpolateRGB(
            new Vector3i(argb1.y, argb1.z, argb1.w),
            new Vector3i(argb2.y, argb2.z, argb2.w), factor
        );
        return new Vector4i(
            interpolateI(argb1.x, argb2.x, factor),
            rgbRet.x,
            rgbRet.y,
            rgbRet.z
        );
    }




    /**
     * Interpolates two float values.
     * @param v1 The first value.
     * @param v2 The second value.
     * @param factor The interpolation factor.
     * @return The resulting value.
     */
    public static float interpolateF(final float v1, final float v2, final float factor) {
        return v1 + (v2 - v1) * factor;
    }




    /**
     * Interpolates two double values.
     * @param v1 The first value.
     * @param v2 The second value.
     * @param factor The interpolation factor.
     * @return The resulting value.
     */
    public static double interpolateF(final double v1, final double v2, final double factor) {
        return v1 + (v2 - v1) * factor;
    }




    /**
     * Interpolates two int values.
     * @param v1 The first value.
     * @param v2 The second value.
     * @param factor The interpolation factor.
     * @return The resulting value.
     */
    public static int interpolateI(final int v1, final int v2, final float factor) {
        return Math.round(v1 + (v2 - v1) * factor);
    }
}
