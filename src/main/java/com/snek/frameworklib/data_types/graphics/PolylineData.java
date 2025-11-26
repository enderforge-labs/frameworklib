package com.snek.frameworklib.data_types.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3i;








/**
 * Represents the data of a multi-segment line.
 */
public class PolylineData {

    // Data
    private final @NotNull Vector3i color;
    private final int alpha;
    private final float width;
    private final float edge;
    private final @NotNull List<@NotNull Vector2f> points;
    private final float totLen;

    // Getters
    public @NotNull Vector3i                getColor () { return color;  }
    public int                              getAlpha () { return alpha;  }
    public float                            getWidth () { return width;  }
    public float                            getEdge  () { return edge;   }
    public @NotNull List<@NotNull Vector2f> getPoints() { return points; }
    public float                            getTotLen() { return totLen; }


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
    public PolylineData(final @NotNull Vector3i color, final int alpha, final float width, final float edge, final @NotNull Vector2f point1, final @NotNull Vector2f point2, final @NotNull Vector2f... points) {

        // Save basic data
        this.color = color;
        this.alpha = alpha;
        this.width = width;
        this.edge  = edge;

        // Save points
        this.points = new ArrayList<>(points.length + 2);
        this.points.add(point1);
        this.points.add(point2);
        Collections.addAll(this.points, points);

        // Calculate total length
        float totLen = 0;
        for(int i = 0; i < this.points.size() - 1; ++i) {
            totLen += this.points.get(i).distance(this.points.get(i + 1));
        }
        this.totLen = totLen;
    }
}
