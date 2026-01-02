package com.snek.frameworklib.graphics.designs;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.utils.GeometryUtils;
import com.snek.frameworklib.utils.UtilityClassBase;





public final class DesignPrimitives extends UtilityClassBase {

    /**
     * Creates a circle made of 8 straight points, centered on (0, 0).
     * <p>
     * Use cyclic = true to connect the first and last points.
     * @param radius The radius of the circle.
     * @param segments The amount of segments to return. Only the first {@code segments} lines are returned. Must be > 0.
     * @return The generated points.
     */
    public static @NotNull List<@NotNull Vector2f> createCircle(final float radius, final int segments) {
        assert Require.nonNegative(radius, "radius");
        assert Require.nonNegative(segments, "segment amount");

        // Create segments by rotating around the origin
        final int _segments = Math.min(8, Math.max(segments, 1));
        final @NotNull List<@NotNull Vector2f> r = new ArrayList<>(_segments);
        for(int i = 0; i < _segments; ++i) {
            r.add(GeometryUtils.rotateVec2(
                new Vector2f(0, radius),
                (float)Math.toRadians(-45) * (i + 0.5f)
            ));
        }

        // Return
        return r;
    }


    /**
     * Creates a circle made of 8 points, centered on (0, 0).
     * <p>
     * Use cyclic = true to connect the first and last points.
     * @param radius The radius of the circle.
     * @return The generated points.
     */
    public static @NotNull List<@NotNull Vector2f> createCircle(final float radius) {
        return createCircle(radius, 8);
    }
}
