package com.snek.frameworklib.graphics.core.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.core.styles.CanvasBorder_S;

import net.minecraft.server.level.ServerLevel;








/**
 * An element that can display a full-width, horizontally centered line of configurable color.
 */
public class CanvasBorder extends PanelElm {
    public static final float DEFAULT_HEIGHT = 0.02f;


    /**
     * Creates a new HudBorder using a custom style.
     * @param _world The world to create the element in.
     * @param _style The style.
     */
    public CanvasBorder(final @NotNull ServerLevel _world, final @NotNull CanvasBorder_S _style) {
        super(_world, _style);
    }


    /**
     * Creates a new HudBorder using the default style.
     * @param _world The world to create the element in.
     */
    public CanvasBorder(final @NotNull ServerLevel _world) {
        this(_world, new CanvasBorder_S());
    }
}
