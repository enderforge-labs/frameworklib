package com.snek.frameworklib.graphics.core.ui._styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;








/**
 * The style of the UiBorder UI element.
 */
public class UiBorder_S extends PanelElmStyle {
    public static final @NotNull Vector3i COLOR = new Vector3i(33, 33, 35);


    /**
     * Creates a new UiBorder_S.
     */
    public UiBorder_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(COLOR);
    }
}
