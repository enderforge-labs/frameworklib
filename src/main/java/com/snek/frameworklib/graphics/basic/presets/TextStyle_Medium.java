package com.snek.frameworklib.graphics.basic.presets;

import com.snek.frameworklib.graphics.basic.styles.TextStyle;




/**
 * A default TextStyle with font size 9.
 * @since v1.1.0
 */
public class TextStyle_Medium extends TextStyle {
    public TextStyle_Medium() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 9;
    }
}
