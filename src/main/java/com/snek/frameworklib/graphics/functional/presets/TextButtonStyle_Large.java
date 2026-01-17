package com.snek.frameworklib.graphics.functional.presets;

import com.snek.frameworklib.graphics.functional.styles.TextButtonStyle;




/**
 * A default TextButtonStyle with font size 12.
 */
public class TextButtonStyle_Large extends TextButtonStyle {
    public TextButtonStyle_Large() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 12;
    }
}
