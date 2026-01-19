package com.snek.frameworklib.graphics.functional.presets;

import com.snek.frameworklib.graphics.functional.styles.ChatInputStyle;




/**
 * A default ChatInputStyle with font size 9.
 * @since v1.1.0
 */
public class ChatInputStyle_Medium extends ChatInputStyle {
    public ChatInputStyle_Medium() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 9;
    }
}
