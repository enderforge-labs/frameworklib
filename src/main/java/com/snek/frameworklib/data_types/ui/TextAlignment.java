package com.snek.frameworklib.data_types.ui;








/**
 * Defines how a text element’s lines are aligned relative to each other.
 */
public enum TextAlignment {
    /** Aligns all lines to the left edge of the widest line.  */ LEFT  ("left"  ),
    /** Centers all lines.                                     */ CENTER("center"),
    /** Aligns all lines to the right edge of the widest line. */ RIGHT ("right" );


    private final String text;
    TextAlignment(String text) {
        this.text = text;
    }

    public String asString() {
        return text;
    }
}
