package com.snek.frameworklib.graphics.composite.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.composite.styles.LinePanelStyle;

import net.minecraft.server.level.ServerLevel;








/**
 * A special {@link PanelElm} used to draw lines in graphic contexts.
 */
public final class LinePanel extends PanelElm {
    final Vector2f absPosOg = new Vector2f();


    public LinePanel(final @NotNull ServerLevel world) {
        super(world, new LinePanelStyle());
    }




    @Override
    protected final void updateAbsPosSelf() {

        // Calculate unrestricted position
        final Vector2f p = parent == null ? new Vector2f(0, 0) : parent.getAbsPos();
        final Vector2f s = parent == null ? new Vector2f(1, 1) : parent.getAbsSize();

        // Apply horizontal alignment
        final float x = switch(alignmentX) {
            case LEFT   -> p.x - (s.x - absSize.x) / 2;
            case RIGHT  -> p.x + (s.x - absSize.x) / 2;
            case CENTER -> p.x;
            case NONE   -> p.x + localPos.x * s.x;
        };

        // Apply vertical alignment
        final float y = switch(alignmentY) {
            case TOP    -> p.y + (s.y - absSize.y);
            case BOTTOM -> p.y;
            case CENTER -> p.y + (s.y - absSize.y) / 2;
            case NONE   -> p.y + localPos.y * s.y;
        };

        // Update the value
        absPos.set(x, y);
    }
}
