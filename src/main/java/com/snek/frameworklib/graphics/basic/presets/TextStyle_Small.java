package com.snek.frameworklib.graphics.basic.presets;

import com.snek.frameworklib.graphics.basic.styles.TextStyle;




/**
 * A default TextStyle with font size 6.
 * @since v1.1.0
 */
public class TextStyle_Small extends TextStyle {
    public TextStyle_Small() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 6;
    }
}
