package com.snek.frameworklib.graphics.functional.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;








/**
 * The style of the generic TextInputElm UI element.
 */
public class TextInputElmStyle extends FancyButtonElmStyle {


    /**
     * Creates a new TextInputElmStyle.
     */
    public TextInputElmStyle() {
        super();
    }


    @Override
    public @NotNull TextOverflowBehaviour getTextOverflowBehaviour() {
        return TextOverflowBehaviour.SCROLL;
    }
}
