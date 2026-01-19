package com.snek.frameworklib.graphics.basic.presets;

import com.snek.frameworklib.graphics.basic.styles.PanelTextStyle;




/**
 * A default PanelTextStyle with font size 12.
 * @since v1.1.0
 */
public class PanelTextStyle_Large extends PanelTextStyle {
    public PanelTextStyle_Large() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 12;
    }
}
