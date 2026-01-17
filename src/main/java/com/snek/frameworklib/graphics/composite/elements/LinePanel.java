package com.snek.frameworklib.graphics.composite.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.composite.styles.LinePanel_S;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Hoverable;
import com.snek.frameworklib.graphics.interfaces.Scrollable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;








/**
 * A {@link PanelElm} used to draw lines in graphic contexts. It ignores alignment options and uses special transforms to achieve the desired shape.
 * <p>
 * This class removes the {@link #checkIntersection(Player, boolean)} and {@link #getIntersectionLength(Player)} logic and doesn't show up in the debug window.
 * <p>
 * Subclasses must NOT implement {@link Clickable}, {@link Scrollable} or {@link Hoverable}.
 */
public final class LinePanel extends PanelElm {
    final Vector2f absPosOg = new Vector2f();


    /**
     * Creates a new LinePanel.
     * @param level The level to create this element in.
     */
    public LinePanel(final @NotNull ServerLevel level, final @NotNull LinePanel_S style) {
        super(level, style);
        assert Require.notInstanceOf(this, Clickable .class, "line panel instance");
        assert Require.notInstanceOf(this, Scrollable.class, "line panel instance");
        assert Require.notInstanceOf(this, Hoverable .class, "line panel instance");
    }


    @Override
    public final void updateAbsPosSelf() {

        // Calculate unrestricted position
        final Vector2f p = parent == null ? new Vector2f(0, 0) : parent.getAbsPos();
        final Vector2f s = parent == null ? new Vector2f(1, 1) : parent.getAbsSize();

        // Update the value
        absPos.set(new Vector2f(
            p.x + localPos.x * s.x,
            p.y + localPos.y * s.y
        ));
    }


    @Override
    public Vector2f checkIntersection(final @NotNull Player player, final boolean calculateIntersectionCoords) {
        return null;
    }

    @Override
    public double getIntersectionLength(final @NotNull Player player) {
        return Double.MAX_VALUE;
    }
}
