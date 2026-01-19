package com.snek.frameworklib.data_types.graphics;








/**
 * Defines how a text element’s lines are aligned relative to each other.
 * @since v1.1.0
 */
public enum TextAlignment {
    /** Aligns all lines to the left edge of the widest line.  */ LEFT  ("left"  ),
    /** Centers all lines.                                     */ CENTER("center"),
    /** Aligns all lines to the right edge of the widest line. */ RIGHT ("right" );


    private final String text;
    TextAlignment(final String text) {
        this.text = text;
    }

    /**
     * Retrieves the lowercase name of this alignment option.
     * This matches Minecraft 1.20.1's text alignment NBT tag values for TextDisplay entities.
     * @return The lowercase name. "left", "center", or "right".
     */
    public String asString() {
        return text;
    }
}
