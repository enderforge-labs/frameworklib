package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.debug.Require;

import net.minecraft.network.chat.Component;








/**
 * A configurable version of {@link SimpleTextElmStyle}.
 */
public class ConfigurableSimpleTextElmStyle extends SimpleTextElmStyle {
    protected final @NotNull Component defaultText;
    protected final @NotNull TextAlignment defaultTextAlignment;
    protected final @NotNull TextOverflowBehaviour defaultOverflowBehaviour;


    /**
     * Creates a new ConfigurableSimpleTextElmStyle.
     */
    public ConfigurableSimpleTextElmStyle(
        final @NotNull Component defaultText,
        final @NotNull TextAlignment defaultTextAlignment,
        final @NotNull TextOverflowBehaviour defaultOverflowBehaviour
    ) {
        super();
        assert Require.nonNull(defaultText, "default text");
        assert Require.nonNull(defaultTextAlignment, "default text alignment");
        assert Require.nonNull(defaultOverflowBehaviour, "default overflow behaviour");
        this.defaultText = defaultText;
        this.defaultTextAlignment = defaultTextAlignment;
        this.defaultOverflowBehaviour = defaultOverflowBehaviour;
    }


    @Override
    public @NotNull Component getDefaultText() {
        return defaultText;
    }


    @Override
    public @NotNull TextAlignment getDefaultTextAlignment() {
        return defaultTextAlignment;
    }


    @Override
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() {
        return defaultOverflowBehaviour;
    }
}