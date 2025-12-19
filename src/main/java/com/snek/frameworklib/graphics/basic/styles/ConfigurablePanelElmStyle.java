package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.debug.Require;








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
        assert Require.inRange(defaultAlpha, 0, 255, "default alpha");
        assert Require.nonNull(defaultColor, "default color");
        assert Require.inRange(defaultColor.x, 0, 255, "default color red");
        assert Require.inRange(defaultColor.y, 0, 255, "default color green");
        assert Require.inRange(defaultColor.z, 0, 255, "default color blue");
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