package com.snek.frameworklib.graphics.functional.presets;

import com.snek.frameworklib.graphics.functional.styles.ChatInputStyle;




/**
 * A default ChatInputStyle with font size 6.
 * @since v1.1.0
 */
public class ChatInputStyle_Small extends ChatInputStyle {
    public ChatInputStyle_Small() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 6;
    }
}
