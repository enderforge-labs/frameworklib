package com.snek.frameworklib.data_types.ui;








/**
 * Defines how the text of a text element is displayed when it doesn't fit in the specified dimensions.
   <p> OVERFLOW: The text is allowed to overflow freely.
   <p> TRUNCATE: Cuts the text, removing any character that doesn't fit.
   <p> ELLIPSIS: Same as TRUNCATE, but ends the text with an ellipsis character.
   <p> SCROLL:   Periodically scrolls the text to the left, only showing a part of it at a time.
       This is significantly slower than the other methods.
 */
public enum TextOverflowBehaviour {
    OVERFLOW("overflow"),
    TRUNCATE("truncate"),
    ELLIPSIS("ellipsis"),
    SCROLL("scroll");

    private final String text;

    TextOverflowBehaviour(String text) {
        this.text = text;
    }

    public String asString() {
        return text;
    }
}