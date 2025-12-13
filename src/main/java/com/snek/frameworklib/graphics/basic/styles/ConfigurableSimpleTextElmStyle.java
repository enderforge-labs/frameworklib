package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;

import net.minecraft.network.chat.Component;








/**
 * A configurable version of {@link SimpleTextElmStyle}.
 */
public class ConfigurableSimpleTextElmStyle extends SimpleTextElmStyle {
    protected final @NotNull Component defaultText;
    protected final @NotNull TextOverflowBehaviour defaultOverflowBehaviour;


    /**
     * Creates a new ConfigurableSimpleTextElmStyle.
     */
    public ConfigurableSimpleTextElmStyle(final @NotNull Component defaultText, final @NotNull TextOverflowBehaviour defaultOverflowBehaviour) {
        super();
        this.defaultText = defaultText;
        this.defaultOverflowBehaviour = defaultOverflowBehaviour;
    }


    @Override
    public @NotNull Component getDefaultText() {
        return defaultText;
    }


    @Override
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() {
        return defaultOverflowBehaviour;
    }
}