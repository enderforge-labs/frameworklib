package com.snek.frameworklib.graphics.composite.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.utils.Txt;








public class LinePanelStyle extends PanelElmStyle {
    public LinePanelStyle() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(Txt.COLOR_WHITE);
    }
}
