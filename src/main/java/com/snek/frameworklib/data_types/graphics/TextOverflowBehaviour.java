package com.snek.frameworklib.data_types.graphics;








/**
 * Defines how the text of a text element is displayed when it doesn't fit in the specified dimensions.
 */
public enum TextOverflowBehaviour {
    /** The text is allowed to overflow freely.                         */ OVERFLOW,
    /** Cuts the text, removing any character that doesn't fit.         */ TRUNCATE,
    /** Same as TRUNCATE, but ends the text with an ellipsis character. */ ELLIPSIS,


    /**
     * Periodically scrolls the text to the left, only showing a part of it at a time.
     * <p>
     * Notice: This is significantly slower than the other methods. Use only when necessary.
     */
    SCROLL;
}