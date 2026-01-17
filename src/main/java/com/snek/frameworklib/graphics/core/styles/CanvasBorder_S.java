package com.snek.frameworklib.graphics.core.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelStyle;








public class CanvasBorder_S extends PanelStyle {
    public static final @NotNull Vector3i COLOR = new Vector3i(33, 33, 35);


    /**
     * Creates a new CanvasBorder_S.
     */
    public CanvasBorder_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(COLOR);
    }
}
