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
     * Creates a circle made of 8 straight lines, centered on (0, 0).
     * @param radius The radius of the circle.
     * @param close Whether to close the shape or keep the ends disconnected.
     * @param segments The amount of segments to return. Only the first {@code segments} lines are returned. Must be > 0.
     * @return The generated lines as a list of points.
     */
    public static @NotNull List<@NotNull Vector2f> createCircle(final float radius, final boolean close, final int segments) {
        assert Require.nonNegative(radius, "radius");
        assert Require.nonNegative(segments, "segment amount");

        // Create segments by rotating around the origin
        final int _segments = Math.min(8, Math.max(segments, 1));
        final @NotNull List<@NotNull Vector2f> r = new ArrayList<>(_segments + (close ? 1 : 0));
        for(int i = 0; i <= _segments; ++i) {
            r.add(GeometryUtils.rotateVec2(
                new Vector2f(0, radius),
                (float)Math.toRadians(-45) * (i + 0.5f)
            ));
        }

        // Close the shape if needed
        if(close) {
            r.add(new Vector2f(r.get(r.size() - 1)));
        }

        // Return
        return r;
    }


    /**
     * Creates a circle made of 8 straight lines, centered on (0, 0).
     * @param radius The radius of the circle.
     * @param close Whether to close the shape or keep the ends disconnected.
     * @return The generated lines as a list of points.
     */
    public static @NotNull List<@NotNull Vector2f> createCircle(final float radius, final boolean close) {
        return createCircle(radius, close, 8);
    }


    /**
     * Creates a circle made of 8 straight lines, centered on (0, 0).
     * @param radius The radius of the circle.
     * @return The generated lines as a list of points.
     */
    public static final @NotNull List<@NotNull Vector2f> createCircle(final float radius) {
        return createCircle(radius, true);
    }
}
