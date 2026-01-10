package com.snek.frameworklib.graphics.functional.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;








/**
 * The style of the generic ChatInputElm UI element.
 */
public class ChatInputStyle extends TextButtonStyle {


    /**
     * Creates a new ChatInputStyle.
     */
    public ChatInputStyle(final boolean reset) {
        super(false);
        if(reset) resetAll();
    }

    /**
     * Creates a new ChatInputStyle.
     */
    public ChatInputStyle() {
        this(true);
    }


    @Override
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() {
        return TextOverflowBehaviour.SCROLL;
    }
}
