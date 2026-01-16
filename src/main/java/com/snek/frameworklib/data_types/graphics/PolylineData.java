package com.snek.frameworklib.data_types.graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3i;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.utils.GeometryUtils;
import com.snek.frameworklib.utils.Txt;








/**
 * Represents the data of a multi-segment line.
 */
public class PolylineData {

    // Constants
    public static final @NotNull Vector3i DEFAULT_COLOR = new Vector3i(Txt.COLOR_WHITE);
    public static final          int      DEFAULT_ALPHA = 255;
    public static final          float    DEFAULT_WIDTH = 0.15f;

    // Data
    private @NotNull Vector3i color;
    private int alpha;
    private float width;
    private boolean cyclic;
    private final @NotNull List<@NotNull Vector2f> points;
    private final float totLen;

    // Getters
    public @NotNull Vector3i                getColor () { return color;  }
    public int                              getAlpha () { return alpha;  }
    public float                            getWidth () { return width;  }
    public boolean                          isCyclic () { return cyclic; }
    public @NotNull List<@NotNull Vector2f> getPoints() { return points; }
    public float                            getTotLen() { return totLen; }


    // Setters
    public @NotNull PolylineData withColor(final @NotNull Vector3i color) {
        assert Require.nonNull(color, "color");
        this.color = new Vector3i(color);
        return this;
    }
    public @NotNull PolylineData withAlpha(final int alpha) {
        assert Require.inRange(alpha, 0, 255, "alpha");
        this.alpha = alpha;
        return this;
    }
    public @NotNull PolylineData withWidth(final float width) {
        assert Require.nonNegative(width, "width");
        this.width = width;
        return this;
    }




    /**
     * Creates a new  non-cyclic PolylineData using the specified values.
     * @param color The color of the line.
     * @param alpha The opacity of the line.
     * @param width The width of the line.
     * @param points The list of points.
     */
    public PolylineData(
        final @NotNull Vector3i color,
        final int alpha,
        final float width,
        final @NotNull List<Vector2f> points
    ) {
        this(color, alpha, width, false, points);
    }

    /**
     * Creates a new PolylineData using the specified values.
     * @param color The color of the line.
     * @param alpha The opacity of the line.
     * @param width The width of the line.
     * @param cyclic Whether the polyline is cyclic. Cyclic polylines have their first and last points connected.
     * @param points The list of points.
     */
    public PolylineData(
        final @NotNull Vector3i color,
        final int alpha,
        final float width,
        final boolean cyclic,
        final @NotNull List<Vector2f> points
    ) {
        assert Require.nonNull(color, "color");
        assert Require.inRange(color.x, 0, 255, "color red");
        assert Require.inRange(color.y, 0, 255, "color blue");
        assert Require.inRange(color.z, 0, 255, "color green");
        assert Require.inRange(alpha, 0, 255, "alpha");
        assert Require.nonNull(points, "point list");

        // Save basic data
        this.color  = new Vector3i(color);
        this.alpha  = alpha;
        this.width  = width;
        this.cyclic = cyclic;

        // Save points
        this.points = new ArrayList<>(points.size());
        for(final Vector2f p : points) {
            this.points.add(new Vector2f(p));
        }

        // Calculate total length
        float _totLen = 0;
        for(int i = 0; i < this.points.size() - 1; ++i) {
            _totLen += this.points.get(i).distance(this.points.get(i + 1));
        }
        this.totLen = _totLen;
    }




    /**
     * Creates a new non-cyclic PolylineData using the specified values.
     * @param color The color of the line.
     * @param alpha The opacity of the line.
     * @param width The width of the line.
     * @param point1 The coordinates of the first point of the line.
     * @param point2 The coordinates of the second point of the line.
     * @param points An optional list of coordinates for additional points.
     */
    public PolylineData(
        final @NotNull Vector3i color,
        final int alpha,
        final float width,
        final @NotNull Vector2f point1,
        final @NotNull Vector2f point2,
        final @NotNull Vector2f... points
    ) {
        this(color, alpha, width, false, point1, point2, points);
    }

    /**
     * Creates a new PolylineData using the specified values.
     * @param color The color of the line.
     * @param alpha The opacity of the line.
     * @param width The width of the line.
     * @param cyclic Whether the polyline is cyclic. Cyclic polylines have their first and last points connected.
     * @param point1 The coordinates of the first point of the line.
     * @param point2 The coordinates of the second point of the line.
     * @param points An optional list of coordinates for additional points.
     */
    public PolylineData(
        final @NotNull Vector3i color,
        final int alpha,
        final float width,
        final boolean cyclic,
        final @NotNull Vector2f point1,
        final @NotNull Vector2f point2,
        final @NotNull Vector2f... points
    ) {
        this(color, alpha, width, cyclic,
            Stream.concat(
                Stream.of(point1, point2),
                Arrays.stream(points)
            ).toList()
        );
    }




    /**
     * Creates a new non-cyclic PolylineData using the specified points and default color, alpha, and line width.
     * @param points The list of points.
     */
    public PolylineData(final @NotNull List<Vector2f> points) {
        this(DEFAULT_COLOR, DEFAULT_ALPHA, DEFAULT_WIDTH, points);
    }
    /**
     * Creates a new PolylineData using the specified points and default color, alpha, and line width.
     * @param cyclic Whether the polyline is cyclic. Cyclic polylines have their first and last points connected.
     * @param points The list of points.
     */
    public PolylineData(final boolean cyclic, final @NotNull List<Vector2f> points) {
        this(DEFAULT_COLOR, DEFAULT_ALPHA, DEFAULT_WIDTH, cyclic, points);
    }




    /**
     * Creates a new non-cyclic PolylineData using the specified points and default color, alpha, and line width.
     * @param point1 The coordinates of the first point of the line.
     * @param point2 The coordinates of the second point of the line.
     * @param points An optional list of coordinates for additional points.
     */
    public PolylineData(
        final @NotNull Vector2f point1,
        final @NotNull Vector2f point2,
        final @NotNull Vector2f... points
    ) {
        this(DEFAULT_COLOR, DEFAULT_ALPHA, DEFAULT_WIDTH, point1, point2, points);
    }

    /**
     * Creates a new PolylineData using the specified points and default color, alpha, and line width.
     * @param cyclic Whether the polyline is cyclic. Cyclic polylines have their first and last points connected.
     * @param point1 The coordinates of the first point of the line.
     * @param point2 The coordinates of the second point of the line.
     * @param points An optional list of coordinates for additional points.
     */
    public PolylineData(
        final boolean cyclic,
        final @NotNull Vector2f point1,
        final @NotNull Vector2f point2,
        final @NotNull Vector2f... points
    ) {
        this(DEFAULT_COLOR, DEFAULT_ALPHA, DEFAULT_WIDTH, cyclic, point1, point2, points);
    }




    public @NotNull PolylineData transform(
        final float shiftX,   final float shiftY,
        final float stretchX, final float stretchY,
        final float rotate
    ) {
        for(final Vector2f p : points) {
            p.set(
                GeometryUtils.rotateVec2(
                    new Vector2f(
                        (p.x + shiftX) * stretchX,
                        (p.y + shiftY) * stretchY
                    ),
                    rotate
                )
            );
        }
        return this;
    }




    /**
     * Shifts the lines.
     * @param amountX The amount to shift the lines by along the X axis.
     * @param amountY The amount to shift the lines by along the y axis.
     * @return {@code this}.
     */
    public @NotNull PolylineData shift(final float amountX, final float amountY) {
        return transform(amountX, amountY, 1, 1, 0);
    }

    /**
     * Shifts the lines along the X axis.
     * @param amount The amount to shift the lines by.
     * @return {@code this}.
     */
    public @NotNull PolylineData shiftX(final float amount) {
        return shift(amount, 0);
    }

    /**
     * Shifts the lines along the Y axis.
     * @param amount The amount to shift the lines by.
     * @return {@code this}.
     */
    public @NotNull PolylineData shiftY(final float amount) {
        return shift(0, amount);
    }





    /**
     * Stretches or shrinks the lines, using the center of the design (0.5, 0.5) as origin.
     * @param amountX The amount of stretching on the X asis.
     *     Values between -0.5 and 0.5 shrink the design.
     *     Negative values flip the design.
     * @param amountY The amount of stretching on the Y axis.
     *     Values between -0.5 and 0.5 shrink the design.
     *     Negative values flip the design.
     * @return {@code this}.
     */
    public @NotNull PolylineData stretch(final float amountX, final float amountY) {
        return transform(-0.5f, -0.5f, amountX, amountY, 0).shift(0.5f, 0.5f);
    }

    /**
     * Stretches or shrinks the lines along the X axis, using the center of the design (0.5, 0.5) as origin.
     * @param amount The amount of stretching.
     *     Values between -0.5 and 0.5 shrink the design.
     *     Negative values flip the design.
     * @return {@code this}.
     */
    public @NotNull PolylineData stretchX(final float amount) {
        return stretch(amount, 1);
    }

    /**
     * Stretches or shrinks the lines along the Y axis, using the center of the design (0.5, 0.5) as origin.
     * @param amount The amount of stretching.
     *     Values between -0.5 and 0.5 shrink the design.
     *     Negative values flip the design.
     * @return {@code this}.
     */
    public @NotNull PolylineData stretchY(final float amount) {
        return stretch(1, amount);
    }




    /**
     * Flips the lines along the X and Y axes, using the center of the design(0.5, 0.5) as origin.
     * @return {@code this}.
     */
    public @NotNull PolylineData flipXY() {
        return stretch(-1, -1);
    }

    /**
     * Flips the lines along the X axis, using the center of the design(0.5, 0.5) as origin.
     * @return {@code this}.
     */
    public @NotNull PolylineData flipX() {
        return stretchX(-1);
    }

    /**
     * Flips the lines along the X axis, using the center of the design(0.5, 0.5) as origin.
     * @return {@code this}.
     */
    public @NotNull PolylineData flipY() {
        return stretchY(-1);
    }




    /**
     * Rotates the lines around the origin (0, 0)
     * @param amount The angle, in radians.
     * @return {@code this}.
     */
    public @NotNull PolylineData rotate(final float amount) {
        return transform(0, 0, 1, 1, amount);
    }




    /**
     * Creates a deep copy of this data.
     * @return A copy of this data.
     */
    public @NotNull PolylineData copy() {
        return new PolylineData(color, alpha, width, cyclic, points);
    }
}
