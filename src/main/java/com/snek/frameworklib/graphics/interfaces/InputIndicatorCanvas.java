package com.snek.frameworklib.graphics.interfaces;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.composite.elements.InputIndicator;




/**
 * An interface for canvases that implement an input indicator.
 * @since v1.1.0
 */
public interface InputIndicatorCanvas {
    public @NotNull InputIndicator getLmbIndicator();
    public @NotNull InputIndicator getRmbIndicator();
}
