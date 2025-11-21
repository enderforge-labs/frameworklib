package com.snek.frameworklib.data_types.ui;








/**
 * Defines how a text element’s lines are aligned relative to each other.
 * <p> LEFT:   Aligns all lines to the left edge of the widest line.
 * <p> CENTER: Centers all lines.
 * <p> RIGHT:  Aligns all lines to the right edge of the widest line.
 */
public enum TextAlignment {
    LEFT("left"),
    CENTER("center"),
    RIGHT("right");

    private final String text;

    TextAlignment(String text) {
        this.text = text;
    }

    public String asString() {
        return text;
    }
}
