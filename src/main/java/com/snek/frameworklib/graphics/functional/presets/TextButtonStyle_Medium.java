package com.snek.frameworklib.graphics.functional.presets;

import com.snek.frameworklib.graphics.functional.styles.TextButtonStyle;




/**
 * A default TextButtonStyle with font size 9.
 * @since v1.1.0
 */
public class TextButtonStyle_Medium extends TextButtonStyle {
    public TextButtonStyle_Medium() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 9;
    }
}
