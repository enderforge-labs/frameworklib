package com.snek.frameworklib.graphics.composite.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelStyle;
import com.snek.frameworklib.utils.Txt;








/**
 * The style of the MouseButtonDown UI element.
 */
public class InputIndicator_MouseButtonDown_S extends PanelStyle {

    /**
     * Creates a new MouseButtonDown_S.
     */
    public InputIndicator_MouseButtonDown_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(Txt.COLOR_LIGHTGRAY);
    }
}
