package com.snek.frameworklib.graphics.functional.presets;

import com.snek.frameworklib.graphics.functional.styles.TextButtonStyle;




/**
 * A default TextButtonStyle with font size 6.
 * @since v1.1.0
 */
public class TextButtonStyle_Small extends TextButtonStyle {
    public TextButtonStyle_Small() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 6;
    }
}
