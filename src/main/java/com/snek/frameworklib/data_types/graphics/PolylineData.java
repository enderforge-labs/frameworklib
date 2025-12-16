package com.snek.frameworklib.data_types.graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3i;








/**
 * Represents the data of a multi-segment line.
 */
public class PolylineData {

    // Data
    private @NotNull Vector3i color;
    private int alpha;
    private float width;
    private float edge;
    private final @NotNull List<@NotNull Vector2f> points;
    private final float totLen;

    // Getters
    public @NotNull Vector3i                getColor () { return color;  }
    public int                              getAlpha () { return alpha;  }
    public float                            getWidth () { return width;  }
    public float                            getEdge  () { return edge;   }
    public @NotNull List<@NotNull Vector2f> getPoints() { return points; }
    public float                            getTotLen() { return totLen; }

    // Setters
    public void setColor (@NotNull Vector3i color) { this.color = new Vector3i(color); }
    public void setAlpha (int               alpha) { this.alpha = alpha; }
    public void setWidth (float             width) { this.width = width; }
    public void setEdge  (float              edge) { this.edge  = edge;  }




    /**
     * Creates a new PolylineData using the specified values.
     * @param color The color of the line.
     * @param alpha The opacity of the line.
     * @param width The width of the line.
     * @param edge  The additional width to add on each end of each line
     * @param points The list of points.
     */
    public PolylineData(
        final @NotNull Vector3i color,
        final int alpha,
        final float width,
        final float edge,
        final @NotNull List<Vector2f> points
    ) {

        // Save basic data
        this.color = new Vector3i(color);
        this.alpha = alpha;
        this.width = width;
        this.edge  = edge;

        // Save points
        this.points = new ArrayList<>(points.size());
        for(final Vector2f p : points) {
            this.points.add(new Vector2f(p));
        }

        // Calculate total length
        float totLen = 0;
        for(int i = 0; i < this.points.size() - 1; ++i) {
            totLen += this.points.get(i).distance(this.points.get(i + 1));
        }
        this.totLen = totLen;
    }




    /**
     * Creates a new PolylineData using the specified values.
     * @param color The color of the line.
     * @param alpha The opacity of the line.
     * @param width The width of the line.
     * @param edge  The additional width to add on each end of each line
     * @param point1 The coordinates of the first point of the line.
     * @param point2 The coordinates of the second point of the line.
     * @param points An optional list of coordinates for additional points.
     */
    public PolylineData(
        final @NotNull Vector3i color,
        final int alpha,
        final float width,
        final float edge,
        final @NotNull Vector2f point1,
        final @NotNull Vector2f point2,
        final @NotNull Vector2f... points
    ) {
        this(color, alpha, width, edge,
            Stream.concat(
                Stream.of(point1, point2),
                Arrays.stream(points)
            ).collect(Collectors.toList())
        );
    }
}
