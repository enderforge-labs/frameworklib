package com.snek.frameworklib.graphics.functional.presets;

import com.snek.frameworklib.graphics.functional.styles.ChatInputStyle;




/**
 * A default ChatInputStyle with font size 12.
 */
public class ChatInputStyle_Large extends ChatInputStyle {
    public ChatInputStyle_Large() {
        super();
    }


    @Override
    public int getDefaultFontSize() {
        return 12;
    }
}
