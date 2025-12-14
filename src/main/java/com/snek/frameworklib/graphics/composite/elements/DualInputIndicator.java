package com.snek.frameworklib.graphics.composite.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ClickAction;








/**
 * A display for click action descriptions defined by the element that's currently being hovered on.
 */
public class DualInputIndicator extends Div implements InputIndicatorCanvas {
    public static final @NotNull Vector2f DEFAULT_DUAL_INDICATOR_SIZE  = new Vector2f(0.95f, 0.08f);
    public static final float INDICATOR_SIZE_SPACING = 0.2f;
    public static final @NotNull Vector2f INDICATOR_SIZE = new Vector2f(1, (1 - INDICATOR_SIZE_SPACING) / 2);

    private final @NotNull InputIndicator lmbIndicator;
    private final @NotNull InputIndicator rmbIndicator;




    /**
     * Creates a new DualInputIndicator.
     */
    public DualInputIndicator(final @NotNull ServerLevel level) {
        Div e;


        // Add left click display
        e = addChild(new InputIndicator(level, ClickAction.PRIMARY));
        e.setSize(INDICATOR_SIZE);
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);
        lmbIndicator = (InputIndicator)e;


        // Add right click display
        e = addChild(new InputIndicator(level, ClickAction.SECONDARY));
        e.setSize(INDICATOR_SIZE);
        e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);
        rmbIndicator = (InputIndicator)e;
    }


    @Override public @NotNull InputIndicator getLmbIndicator() { return lmbIndicator; }
    @Override public @NotNull InputIndicator getRmbIndicator() { return rmbIndicator; }
}
