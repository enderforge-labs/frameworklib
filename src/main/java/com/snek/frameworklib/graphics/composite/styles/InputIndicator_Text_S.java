package com.snek.frameworklib.graphics.composite.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;








/**
 * The style of the InputIndicatorText UI element.
 */
public class InputIndicator_Text_S extends SimpleTextElmStyle {


    /**
     * Creates a new InputIndicatorText_S.
     */
    public InputIndicator_Text_S() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 6;
    }


    @Override
    public @NotNull TextAlignment getDefaultTextAlignment() {
        return TextAlignment.LEFT;
    }
}
