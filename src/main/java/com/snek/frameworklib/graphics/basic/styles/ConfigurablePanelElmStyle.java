package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;








/**
 * A configurable version of {@link PanelElmStyle}.
 */
public class ConfigurablePanelElmStyle extends PanelElmStyle {
    protected final int defaultAlpha;
    protected final @NotNull Vector3i defaultColor;


    /**
     * Creates a new ConfigurableFancyTextElmStyle.
     */
    public ConfigurablePanelElmStyle(final int defaultAlpha, final @NotNull Vector3i defaultColor) {
        super();
        this.defaultAlpha = defaultAlpha;
        this.defaultColor = defaultColor;
    }


    @Override
    public int getDefaultAlpha() {
        return defaultAlpha;
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(defaultColor);
    }
}