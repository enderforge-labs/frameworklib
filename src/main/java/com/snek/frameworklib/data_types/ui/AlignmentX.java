package com.snek.frameworklib.data_types.ui;




/**
 * Defines the horizontal alignment of a UI element.
 */
public enum AlignmentX {

    /** The element is not horizontally aligned and can move freely.                       */ NONE  (-1),
    /** The left edge  of the element is always aligned with the left edge of its parent.  */ LEFT  ( 0),
    /** The center     of the element is always aligned with the center of its parent.     */ CENTER( 1),
    /** The right edge of the element is always aligned with the right edge of its parent. */ RIGHT ( 2);


    public final int value;
    AlignmentX(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
