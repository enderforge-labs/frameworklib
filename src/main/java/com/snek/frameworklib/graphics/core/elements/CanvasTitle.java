package com.snek.frameworklib.graphics.core.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;








public class CanvasTitle extends SimpleTextElm {

    /**
     * Creates a new TitleElm.
     * @param level The level to create this element in.
     * @param defaultText The initial text to display when the element is created. This will replace the text specified in the style.
     * @param style The style to use. Its text overflow behaviour is automatically set to SCROLL.
     */
    public CanvasTitle(final @NotNull ServerLevel level, final @NotNull Component defaultText, final @NotNull SimpleTextElmStyle style) {
        super(level, style
            .withText(defaultText)
            .withTextAlignment(TextAlignment.CENTER)
            .withTextOverflowBehaviour(TextOverflowBehaviour.SCROLL)
        );
    }

    /**
     * Creates a new TitleElm.
     * @param level The level to create this element in.
     * @param defaultText The initial text to display when the element is created. This will replace the text specified in the style.
     * @param style The style to use. Its text overflow behaviour is automatically set to SCROLL.
     */
    public CanvasTitle(final @NotNull ServerLevel level, final @NotNull String defaultText, final @NotNull SimpleTextElmStyle style) {
        this(level, Component.literal(defaultText), style);
    }

    /**
     * Creates a new TitleElm.
     * @param level The level to create this element in.
     * @param defaultText The initial text to display when the element is created.
     */
    public CanvasTitle(final @NotNull ServerLevel level, final @NotNull Component defaultText) {
        this(level, defaultText, new SimpleTextElmStyle());
    }

    /**
     * Creates a new TitleElm.
     * @param level The level to create this element in.
     * @param defaultText The initial text to display when the element is created.
     */
    public CanvasTitle(final @NotNull ServerLevel level, final @NotNull String defaultText) {
        this(level, Component.literal(defaultText));
    }




    /**
     * Updates the displayed text.
     * @param text The text to display.
     */
    public void updateDisplay(final @NotNull Component text) {
        getStyle(SimpleTextElmStyle.class).setText(text);
        flushStyle();
    }
}
