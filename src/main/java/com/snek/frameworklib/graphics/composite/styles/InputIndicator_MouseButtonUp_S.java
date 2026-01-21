package com.snek.frameworklib.graphics.composite.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.styles.PanelStyle;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;








/**
 * The style of the MouseButtonUp UI element.
 * @since v1.1.0
 */
public class InputIndicator_MouseButtonUp_S extends PanelStyle {
    public static final float BUTTON_SCALE = 1.2f;


    /**
     * Creates a new MouseButtonUp_S.
     */
    public InputIndicator_MouseButtonUp_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(240, 240, 240);
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform()
            .scale(BUTTON_SCALE)
            .moveX(-(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE.x * InputIndicator.MOUSE_SIZE.x * InputIndicator.BUTTON_SIZE.x * (BUTTON_SCALE - 1)) / 2)
        ;
    }
}
