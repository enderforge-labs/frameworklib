package com.snek.frameworklib.utils;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.generated.FontData;








/**
 * A utility class that can calculate the width and height of strings and characters.
 * <p>
 * This relies on the font dimensions defined in {@link FontData}.
 */
public final class FontSize extends UtilityClassBase {
    private FontSize() {}




    /**
     * Calculates the width a string would have when rendered in-game, in texture pixels.
     * <p>
     * This includes the empty space between each character.
     * @return The width of the string in pixels.
     */
    public static int getStringWidthPx(final @NotNull String s) {
        assert Require.nonNull(s, "string");

        int r = 0;
        for(int i = 0; i < s.length(); ++i) {
            r += getCharWidthPx(s.charAt(i));
        }
        return r;
    }
    /**
     * Calculates the width a string would have when rendered in-game.
     * <p>
     * This includes the empty space between each character.
     * @return The width of the string in blocks.
     */
    public static double getStringWidth(final @NotNull String s) {
        assert Require.nonNull(s, "string");
        return (double)getStringWidthPx(s) / FontData.TEXT_PIXEL_BLOCK_RATIO;
    }




    /**
     * Calculates the width a character would have when rendered in-game, in texture pixels.
     * <p>
     * This includes the empty space between the characters of a string.
     * @return The width of the character in pixels.
     */
    public static int getCharWidthPx(final char c) {
        //! Return a default "square character" width for characters not defined in the widths array
        //! Normally, this should only include Character.MAX_VALUE (valid char value, but not a valid character in text)
        if(c >= FontData.WIDTHS.length) return FontData.HEIGHT;
        return FontData.WIDTHS[c];
    }
    /**
     * Calculates the width a character would have when rendered in-game.
     * <p>
     * This includes the empty space between the characters of a string.
     * @return The width of the character in blocks.
     */
    public static double getCharWidth(final char c) {
        return (double)getCharWidthPx(c) / FontData.TEXT_PIXEL_BLOCK_RATIO;
    }




    /**
     * Returns the height a line would have when rendered, in texture pixels.
     * <p>
     * This does NOT include the space between lines.
     * @return The height of a line in pixels.
     */
    public static int getHeightPx() {
        return FontData.HEIGHT;
    }

    /**
     * Returns the height a line would have when rendered.
     * <p>
     * This does NOT include the space between lines.
     * @return The height of a line in blocks.
     */
    public static double getHeight() {
        return (double)getHeightPx() / FontData.TEXT_PIXEL_BLOCK_RATIO;
    }




    /**
     * Finds the end of  the longest substring of s that strarts at index offset and fits in maxWidth pixels.
     * @param s The full string value.
     * @param offset The character offset from the start of the string. This specifies where the substring starts.
     *      Must be between {@code 0} and {@code s.length()} inclusive.
     * @param maxWidth The maximum allowed width of the returned substring, in pixels.
     *      Must be {@code >= 0}.
     * @return The calculated EXCLUSIVE end index.
     */
    public static int calcMaxStringEnd(final @NotNull String s, final int offset, final int maxWidth) {
        assert Require.nonNull(s, "string");
        assert Require.inRange(offset, 0, s.length() - 1, "offset");
        assert Require.nonNegative(maxWidth, "maxWidth");

        // Find end index
        int end = offset;
        int width = 0;
        while(end < s.length()) {
            width += getCharWidthPx(s.charAt(end));
            if(width > maxWidth) return end;
            ++end;
        }
        return end;
    }
}