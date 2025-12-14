package com.snek.frameworklib.graphics.core.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.core.styles.CanvasBorder_S;

import net.minecraft.server.level.ServerLevel;








/**
 * An element used to display a full-width, horizontally centered line of configurable color.
 * <p>
 * This is mainly used to draw the top and bottom borders of canvases, but it can be extended by mods freely or used for other purposes.
 */
public class CanvasBorder extends PanelElm {
    public static final float DEFAULT_HEIGHT = 0.02f;


    /**
     * Creates a new HudBorder using a custom style.
     * @param level The level to create the element in.
     * @param style The style.
     */
    public CanvasBorder(final @NotNull ServerLevel level, final @NotNull CanvasBorder_S style) {
        super(level, style);
    }


    /**
     * Creates a new HudBorder using the default style.
     * @param level The level to create the element in.
     */
    public CanvasBorder(final @NotNull ServerLevel level) {
        this(level, new CanvasBorder_S());
    }
}
