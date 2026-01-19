package com.snek.frameworklib.utils;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;

import com.snek.frameworklib.debug.Require;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;







/**
 * A simpler but more readable minecraft.text.MutableText.
 * <p>
 * This version also implements a {@link #substring()} method that preserves the style and a cached {@link #length()}, on top of extra color constants.
 * <p>
 * Use {@link #get()} to create a MutableText from this object's data.
 * @since v1.1.0
 */
public class Txt {
    private final MutableComponent rawText;
    private Style style;
    private int _length;
    public int length() { return _length; }




    /**
     * Creates a new empty Txt.
     */
    public Txt() {
        rawText = Component.empty();
        _length = 0;
        style = Style.EMPTY;
    }

    /**
     * Creates a new Txt using a String content.
     * @param s The String.
     */
    public Txt(final @NotNull String s) {
        assert Require.nonNull(s, "string");

        rawText = Component.literal(s);
        _length = s.length();
        style = Style.EMPTY;
    }

    /**
     * Creates a new Txt using a copy of a MutableText as content.
     * @param s The MutableText.
     */
    public Txt(final @NotNull MutableComponent s) {
        assert Require.nonNull(s, "component");

        rawText = s.copy();
        _length = s.getString().length();
        style = rawText.getStyle();
    }

    /**
     * Creates a new Txt using a copy of a Component as content.
     * @param s The Component.
     */
    public Txt(final @NotNull Component s) {
        assert Require.nonNull(s, "component");

        rawText = s.copy();
        _length = s.getString().length();
        style = rawText.getStyle();
    }




    private Txt(final @NotNull MutableComponent s, final int length) {
        assert Require.nonNull(s, "component");
        assert Require.nonNegative(length, "length");

        rawText = s.copy();
        _length = length;
        style = rawText.getStyle();
    }

    /**
     * Creates a copy of this Txt.
     * @return A copy of this Txt.
     */
    public @NotNull Txt copy() {
        rawText.setStyle(style);
        return new Txt(rawText.copy(), _length);
    }




    // Minecraft colors
    public static final @NotNull Vector3i COLOR_BLACK      = new Vector3i(  0,   0,   0); public @NotNull Txt black    () { return color(COLOR_BLACK     ); }
    public static final @NotNull Vector3i COLOR_BLUE       = new Vector3i(  0,   0, 170); public @NotNull Txt blue     () { return color(COLOR_BLUE      ); }
    public static final @NotNull Vector3i COLOR_GREEN      = new Vector3i(  0, 170,   0); public @NotNull Txt green    () { return color(COLOR_GREEN     ); }
    public static final @NotNull Vector3i COLOR_AQUA       = new Vector3i(  0, 170, 170); public @NotNull Txt aqua     () { return color(COLOR_AQUA      ); }
    public static final @NotNull Vector3i COLOR_DARKRED    = new Vector3i(170,   0,   0); public @NotNull Txt darkRed  () { return color(COLOR_DARKRED   ); }
    public static final @NotNull Vector3i COLOR_PURPLE     = new Vector3i(170,   0, 170); public @NotNull Txt purple   () { return color(COLOR_PURPLE    ); }
    public static final @NotNull Vector3i COLOR_GOLD       = new Vector3i(255, 170,   0); public @NotNull Txt gold     () { return color(COLOR_GOLD      ); }
    public static final @NotNull Vector3i COLOR_LIGHTGRAY  = new Vector3i(170, 170, 170); public @NotNull Txt lightGray() { return color(COLOR_LIGHTGRAY ); }
    public static final @NotNull Vector3i COLOR_GRAY       = new Vector3i( 85,  85,  85); public @NotNull Txt gray     () { return color(COLOR_GRAY      ); }
    public static final @NotNull Vector3i COLOR_LIGHTBLUE  = new Vector3i( 85,  85, 255); public @NotNull Txt lightBlue() { return color(COLOR_LIGHTBLUE ); }
    public static final @NotNull Vector3i COLOR_LIME       = new Vector3i( 85, 255,  85); public @NotNull Txt lime     () { return color(COLOR_LIME      ); }
    public static final @NotNull Vector3i COLOR_CYAN       = new Vector3i( 85, 255, 255); public @NotNull Txt cyan     () { return color(COLOR_CYAN      ); }
    public static final @NotNull Vector3i COLOR_RED        = new Vector3i(255,  85,  85); public @NotNull Txt red      () { return color(COLOR_RED       ); }
    public static final @NotNull Vector3i COLOR_MAGENTA    = new Vector3i(255,  85, 255); public @NotNull Txt magenta  () { return color(COLOR_MAGENTA   ); }
    public static final @NotNull Vector3i COLOR_YELLOW     = new Vector3i(255, 255,  85); public @NotNull Txt yellow   () { return color(COLOR_YELLOW    ); }
    public static final @NotNull Vector3i COLOR_WHITE      = new Vector3i(235, 235, 235); public @NotNull Txt white    () { return color(COLOR_WHITE     ); }
    public static final @NotNull Vector3i COLOR_PURE_WHITE = new Vector3i(255, 255, 255); public @NotNull Txt pureWhite() { return color(COLOR_PURE_WHITE); }




    // Extra colors
    public static final @NotNull Vector3i COLOR_ORANGE     = new Vector3i(255, 140,   0); public @NotNull Txt Orange    () { return color(COLOR_ORANGE    ); }
    public static final @NotNull Vector3i COLOR_BROWN      = new Vector3i(139,  69,  19); public @NotNull Txt Brown     () { return color(COLOR_BROWN     ); }
    public static final @NotNull Vector3i COLOR_DARKBROWN  = new Vector3i(101,  67,  33); public @NotNull Txt DarkBrown () { return color(COLOR_DARKBROWN ); }
    public static final @NotNull Vector3i COLOR_TAN        = new Vector3i(210, 180, 140); public @NotNull Txt Tan       () { return color(COLOR_TAN       ); }
    public static final @NotNull Vector3i COLOR_BEIGE      = new Vector3i(245, 245, 220); public @NotNull Txt Beige     () { return color(COLOR_BEIGE     ); }

    public static final @NotNull Vector3i COLOR_NAVY       = new Vector3i(  0,   0, 128); public @NotNull Txt Navy      () { return color(COLOR_NAVY      ); }
    public static final @NotNull Vector3i COLOR_INDIGO     = new Vector3i( 75,   0, 130); public @NotNull Txt Indigo    () { return color(COLOR_INDIGO    ); }
    public static final @NotNull Vector3i COLOR_VIOLET     = new Vector3i(148,   0, 211); public @NotNull Txt Violet    () { return color(COLOR_VIOLET    ); }
    public static final @NotNull Vector3i COLOR_DARKVIOLET = new Vector3i(238,   0, 238); public @NotNull Txt DarkViolet() { return color(COLOR_DARKVIOLET); }
    public static final @NotNull Vector3i COLOR_LAVENDER   = new Vector3i(230, 230, 250); public @NotNull Txt Lavender  () { return color(COLOR_LAVENDER  ); }

    public static final @NotNull Vector3i COLOR_PINK       = new Vector3i(255, 192, 203); public @NotNull Txt Pink      () { return color(COLOR_PINK      ); }
    public static final @NotNull Vector3i COLOR_HOTPINK    = new Vector3i(255, 105, 180); public @NotNull Txt HotPink   () { return color(COLOR_HOTPINK   ); }
    public static final @NotNull Vector3i COLOR_SALMON     = new Vector3i(250, 128, 114); public @NotNull Txt Salmon    () { return color(COLOR_SALMON    ); }

    public static final @NotNull Vector3i COLOR_TURQUOISE  = new Vector3i( 64, 224, 208); public @NotNull Txt Turquoise () { return color(COLOR_TURQUOISE ); }
    public static final @NotNull Vector3i COLOR_TEAL       = new Vector3i(  0, 128, 128); public @NotNull Txt Teal      () { return color(COLOR_TEAL      ); }
    public static final @NotNull Vector3i COLOR_OLIVE      = new Vector3i(128, 128,   0); public @NotNull Txt Olive     () { return color(COLOR_OLIVE     ); }

    public static final @NotNull Vector3i COLOR_MAROON     = new Vector3i(128,   0,   0); public @NotNull Txt Maroon    () { return color(COLOR_MAROON    ); }
    public static final @NotNull Vector3i COLOR_BURGUNDY   = new Vector3i(128,   0,  32); public @NotNull Txt Burgundy  () { return color(COLOR_BURGUNDY  ); }
    public static final @NotNull Vector3i COLOR_CRIMSON    = new Vector3i(220,  20,  60); public @NotNull Txt Crimson   () { return color(COLOR_CRIMSON   ); }

    public static final @NotNull Vector3i COLOR_CHARCOAL   = new Vector3i( 54,  69,  79); public @NotNull Txt Charcoal  () { return color(COLOR_CHARCOAL  ); }
    public static final @NotNull Vector3i COLOR_JETBLACK   = new Vector3i( 20,  20,  20); public @NotNull Txt JetBlack  () { return color(COLOR_JETBLACK  ); }
    public static final @NotNull Vector3i COLOR_SILVER     = new Vector3i(192, 192, 192); public @NotNull Txt Silver    () { return color(COLOR_SILVER    ); }




    /**
     * Sets the color of the text.
     * @param r The Red value. 0-255.
     * @param g The Green value. 0-255.
     * @param b The Blue value. 0-255.
     * @return This.
     */
    public @NotNull Txt color(final int r, final int g, final int b) {
        assert Require.inRange(r, 0, 255, "red");
        assert Require.inRange(g, 0, 255, "green");
        assert Require.inRange(b, 0, 255, "blue");
        style = style.withColor((r << 16) | (g << 8) | b);
        return this;
    }

    /**
     * Sets the color of the text.
     * @param rgb A vector representing the Red, Green and Blue values. 0-255.
     * @return This.
     */
    public @NotNull Txt color(final @NotNull Vector3i rgb) {
        assert Require.nonNull(rgb, "rgb");
        style = style.withColor((rgb.x << 16) | (rgb.y << 8) | rgb.z);
        return this;
    }

    /**
     * Sets the color of the text.
     * @param hsv A vector representing the Hue, Saturation and Value values. Hue: 0-360. Saturation and Value: 0-1.
     * @return This.
     */
    public @NotNull Txt colorHSV(final @NotNull Vector3f hsv) {
        assert Require.nonNull(hsv, "hsv");
        return color(Utils.HSVtoRGB(hsv));
    }

    /**
     * Sets the color of the text.
     * @param h 0 to 360.0
     * @param s 0 to 1.0
     * @param v 0 to 1.0
     * @return This.
     */
    public @NotNull Txt colorHSV(final float h, final float s, final float v) {
        assert Require.inRange(h, 0, 360, "hue");
        assert Require.inRange(s, 0, 1, "saturation");
        assert Require.inRange(v, 0, 1, "value");
        return color(Utils.HSVtoRGB(new Vector3f(h, s, v)));
    }

    /**
     * Sets the color of the text.
     * @param argb A vector representing the Alpha, Red, Green and Blue values. 0-255, Alpha is ignored.
     * @return This.
     */
    public @NotNull Txt color(final @NotNull Vector4i argb) {
        assert Require.nonNull(argb, "argb");
        style = style.withColor((argb.y << 16) | (argb.z << 8) | argb.w);
        return this;
    }

    /**
     * Sets the color of the text.
     * @param rgb An int representing the Red, Green and Blue values, each using 1 byte of the int. 0-255. The first byte is ignored.
     * @return This.
     */
    public @NotNull Txt color(final int rgb) {
        assert Require.nonNull(rgb, "rgb");
        style = style.withColor(rgb);
        return this;
    }




    // Basic formatting
    public @NotNull Txt bold           () { style = style.withBold         (true); return this; }
    public @NotNull Txt italic         () { style = style.withItalic       (true); return this; }
    public @NotNull Txt obfuscated     () { style = style.withObfuscated   (true); return this; }
    public @NotNull Txt strikethrough  () { style = style.withStrikethrough(true); return this; }

    // Remove basic formatting
    public @NotNull Txt noBold         () { style = style.withBold         (false); return this; }
    public @NotNull Txt noItalic       () { style = style.withItalic       (false); return this; }
    public @NotNull Txt noObfuscated   () { style = style.withObfuscated   (false); return this; }
    public @NotNull Txt noStrikethrough() { style = style.withStrikethrough(false); return this; }




    /**
     * Concatenates a Component to this Txt.
     * @param s The Component value.
     * @return This.
     */
    public @NotNull Txt cat(final @NotNull Component s) {
        assert Require.nonNull(s, "component");
        rawText.append(s);
        _length += s.getString().length();
        return this;
    }

    /**
     * Concatenates a Txt to this Txt.
     * @param s The Txt value.
     * @return This.
     */
    public @NotNull Txt cat(final @NotNull Txt s) {
        assert Require.nonNull(s, "txt value");
        rawText.append(s.get());
        _length += s._length;
        return this;
    }

    /**
     * Concatenates a String to this Txt.
     * @param s The String value.
     * @return This.
     */
    public @NotNull Txt cat(final @NotNull String s) {
        assert Require.nonNull(s, "string");
        rawText.append(s);
        _length += s.length();
        return this;
    }

    /**
     * Concatenates a char to this Txt.
     * @param c The char value.
     * @return This.
     */
    public @NotNull Txt cat(final char c) {
        rawText.append(String.valueOf(c));
        ++_length;
        return this;
    }




    /**
     * Returns a copy of this Txt as a Minecraft Component.
     * @return The Component value.
     */
    public @NotNull Component get() {
        rawText.setStyle(style);
        return rawText;
    }

    /**
     * Returns the raw string value of this Txt.
     * @return The Txt as a raw String.
     */
    public @NotNull String getString() {
        return rawText.getString();
    }








    /**
     * Extracts a substring from a Txt, preserving styles.
     * @param component The component.
     * @param start Starting index (inclusive).
     *     This can safely exceed the length of the text.
     * @param end Ending index (exclusive).
     *     This can safely exceed the length of the text.
     * @return A new Txt containing the substring with preserved styling.
     */
    public Txt substring(final int start, final int end) {
        assert Require.condition(start >= 0 && end >= start, "Invalid range: start: " + start + ", end: " + end);

        if(start == end) {
            return new Txt();
        }

        final List<MutableComponent> parts = new ArrayList<>();
        extractRecursive(get(), start, end, 0, style, parts);

        if(parts.isEmpty()) {
            return new Txt();
        }

        // Build component chain
        final MutableComponent result = parts.get(0);
        for(int i = 1; i < parts.size(); i++) {
            result.append(parts.get(i));
        }

        return new Txt(result);
    }




    /**
     * Safe substring that clamps indices to valid range.
     * @param component The component.
     * @param start Starting index (inclusive).
     *     This can safely exceed the length of the text.
     * @param end Ending index (exclusive).
     *     This can safely exceed the length of the text.
     * @return A new Txt containing the substring with preserved styling.
     */
    public Txt substringClamped(final int start, final int end) {
        return substring(
            Math.max(0, start),
            Math.max(start, end)
        );
    }




    private static int extractRecursive(final @NotNull Component comp, final int start, final int end, int pos, final @NotNull Style parentStyle, final @NotNull List<@NotNull MutableComponent> parts) {
        assert Require.nonNull(comp, "component");
        assert Require.condition(start >= 0 && end >= start, "Invalid range: start: " + start + ", end: " + end);
        assert Require.nonNegative(pos, "position");
        assert Require.nonNull(parentStyle, "parent style");
        assert Require.nonNull(parts, "parts");


        // Inherit missing styles from parent
        final @NotNull Style effectiveStyle = parentStyle.applyTo(comp.getStyle());


        // Extract text content from component
        final @NotNull String content = comp.getString();
        final int contentLen = content.length();
        final int contentEnd = pos + contentLen;


        // Check if this component's content intersects with the target range
        if(pos < end && contentEnd > start) {
            final int localStart = Math.max(0, start - pos);
            final int localEnd = Math.min(contentLen, end - pos);

            if(localStart < localEnd) {
                final String substring = content.substring(localStart, localEnd);
                parts.add(Component.literal(substring).setStyle(effectiveStyle));
            }
        }
        pos = contentEnd;


        // Process siblings
        for(final @NotNull Component sibling : comp.getSiblings()) {
            if(pos >= end) break;
            pos = extractRecursive(sibling, start, end, pos, effectiveStyle, parts);
        }

        return pos;
    }
}