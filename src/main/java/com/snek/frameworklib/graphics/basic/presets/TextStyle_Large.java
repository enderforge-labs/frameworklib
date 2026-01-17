package com.snek.frameworklib.graphics.basic.presets;

import com.snek.frameworklib.graphics.basic.styles.TextStyle;




/**
 * A default TextStyle with font size 12.
 */
public class TextStyle_Large extends TextStyle {
    public TextStyle_Large() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 12;
    }
}
