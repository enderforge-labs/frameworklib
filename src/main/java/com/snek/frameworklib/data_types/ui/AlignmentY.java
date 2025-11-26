package com.snek.frameworklib.data_types.ui;




/**
 * Defines the vertical alignment of a UI element.
 */
public enum AlignmentY {
    /** The element is not vertically aligned and can move freely.                           */ NONE  (-1),
    /** The top edge    of the element is always aligned with the top edge    of its parent. */ TOP   ( 0),
    /** The center      of the element is always aligned with the center      of its parent. */ CENTER( 1),
    /** The bottom edge of the element is always aligned with the bottom edge of its parent. */ BOTTOM( 2);


    private final int value;
    AlignmentY(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
