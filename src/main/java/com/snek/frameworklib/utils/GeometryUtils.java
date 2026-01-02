package com.snek.frameworklib.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.snek.frameworklib.debug.DebugCheck;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.debug.UiDebugWindow;








/**
 * A utility class providing functions for 2D and 3D Euclidean geometry calculations.
 */
public final class GeometryUtils extends UtilityClassBase {
    private GeometryUtils() {}




    /**
     * Applies a rotation to a 2d vector.
     * <p>
     * The original vector object is not modified. Instead, the rotation is performed on a copy.
     * @param v The vector to rotate.
     * @param angle The angle in radians.
     * @return The rotated vector.
     */
    public static @NotNull Vector2f rotateVec2(final @NotNull Vector2f v, final float angle) {
        final float cos = (float)Math.cos(angle);
        final float sin = (float)Math.sin(angle);
        return new Vector2f(
            v.x * cos - v.y * sin,
            v.x * sin + v.y * cos
        );
    }




    /**
     * Checks whether a line intersects a sphere.
     * <p>
     * The line is assumed to be infinite in both directions, regardless of its length.
     * @param lineOrigin The starting point of the line.
     * @param lineDirection The direction of the line. Must be normalized.
     * @param targetCenter The center of the target sphere.
     * @param targetRadius The radius of the target sphere. Must be positive.
     * @return True if the line intersects the sphere, false otherwise.
     */
    public static boolean checkLineSphereIntersection(final @NotNull Vector3f lineOrigin, final @NotNull Vector3f lineDirection, final @NotNull Vector3f targetCenter, final float targetRadius) {
        assert Require.nonNull(lineOrigin, "line origin");
        assert Require.nonNull(lineDirection, "line direction");
        assert Require.nonNull(targetCenter, "target center");

        // Calculate the direction vector from lineOrigin to targetCenter
        final Vector3f toCenter = new Vector3f(targetCenter).sub(lineOrigin);

        // Find the point on the line that is closest to targetCenter
        final float t = toCenter.dot(lineDirection);
        final Vector3f closestPoint = new Vector3f(lineDirection).mul(t).add(lineOrigin);

        // Calculate its distance from targetCenter and return true if it's smaller than the radiud
        return targetCenter.distanceSquared(closestPoint) <= targetRadius * targetRadius;
    }




    /**
     * Computes the 2d coordinates (0 to 1) where a line intersects a rectangle in a 3D space.
     * <p>
     * The line is assumed to be infinite in both directions, regardless of its length.
     * @param lineOrigin The starting point of the line.
     * @param lineDirection The direction of the line. Must be normalized.
     * @param corners The four corners of the rectangle.
     * @return The 2d coordinates of the intersection if the line intersects the rectangle,
     *     (or (0, 0) if {@code calculateIntersectionCoords == false}),
     *     null otherwise.
     */
    public static @Nullable Vector2f findLineRectangleIntersection(final @NotNull Vector3f lineOrigin, final @NotNull Vector3f lineDirection, final @NotNull Vector3f[] corners, final boolean calculateIntersectionCoords) {
        assert Require.nonNull(lineOrigin, "line origin");
        assert Require.nonNull(lineDirection, "line direction");
        assert Require.nonNull(corners, "corners");
        assert Require.condition(corners.length == 4, "corner array must contain exactly 4 points");


        // Build a stable coordinate system with dir as the Z axis
        final Vector3f right = new Vector3f();
        final Vector3f up = new Vector3f();

        // Choose a reference vector that's not parallel to dir
        final Vector3f ref = Math.abs(lineDirection.y) < 0.9f ? new Vector3f(0, 1, 0) : new Vector3f(1, 0, 0);

        // right = ref × dir (perpendicular to both)
        ref.cross(lineDirection, right).normalize();

        // up = dir × right (completes the orthonormal basis)
        lineDirection.cross(right, up).normalize();

        // Project corners onto the right-up plane
        final Vector3f c1 = new Vector3f(corners[0]).sub(lineOrigin);
        final Vector3f c2 = new Vector3f(corners[1]).sub(lineOrigin);
        final Vector3f c3 = new Vector3f(corners[2]).sub(lineOrigin);
        final Vector3f c4 = new Vector3f(corners[3]).sub(lineOrigin);
        final Vector2f[] p = {
            new Vector2f(c1.dot(right), c1.dot(up)),
            new Vector2f(c2.dot(right), c2.dot(up)),
            new Vector2f(c3.dot(right), c3.dot(up)),
            new Vector2f(c4.dot(right), c4.dot(up))
        };

        //! Debug draw calls
        if(DebugCheck.isDebug()) {
            UiDebugWindow.getW().add(p[0]);
            UiDebugWindow.getW().add(p[1]);
            UiDebugWindow.getW().add(p[2]);
            UiDebugWindow.getW().add(p[3]);
        }


        // Check intersection and return
        if(isPointInQuad(new Vector2f(0, 0), p)) {
            if(calculateIntersectionCoords) {
                return inverseBilinearInterpolation(new Vector2f(0, 0), p);
            }
            else {
                return new Vector2f(0, 0);
            }
        }
        else {
            return null;
        }
    }




    /**
     * Finds the UV coordinates (0-1) of a point within a quad using inverse bilinear interpolation.
     * @param point The point to find coordinates for.
     * @param p1 The 4 corners of the quad (bottom-left, bottom-right, top-right, top-left).
     * @return The UV coordinates (0-1) of the point within the quad.
     */
    public static @NotNull Vector2f inverseBilinearInterpolation(final @NotNull Vector2f point, final @NotNull Vector2f[] p) {
        assert Require.nonNull(point, "point");
        assert Require.nonNull(p, "corners");
        assert Require.condition(p.length == 4, "corner array must contain exactly 4 points");

        // Bilinear form: P(u,v) = (1-v)[(1-u)p1 + u*p2] + v[(1-u)p4 + u*p3]
        // Rearranged: P = a + b*u + c*v + d*u*v

        final Vector2f a = new Vector2f(p[0]);
        final Vector2f b = new Vector2f(p[1].x - p[0].x, p[1].y - p[0].y);
        final Vector2f c = new Vector2f(p[3].x - p[0].x, p[3].y - p[0].y);
        final Vector2f d = new Vector2f(p[0].x - p[1].x + p[2].x - p[3].x, p[0].y - p[1].y + p[2].y - p[3].y);

        // Iterative solution using Newton-Raphson
        float u = 0.5f;
        float v = 0.5f;
        for(int i = 0; i < 10; i++) {
            final float currentX = a.x + b.x * u + c.x * v + d.x * u * v;
            final float currentY = a.y + b.y * u + c.y * v + d.y * u * v;

            final float dx = point.x - currentX;
            final float dy = point.y - currentY;
            if(Math.abs(dx) < 0.0001f && Math.abs(dy) < 0.0001f) break;

            // Compute Jacobian partial derivatives
            final float dfdx_u = b.x + d.x * v;
            final float dfdy_u = b.y + d.y * v;
            final float dfdx_v = c.x + d.x * u;
            final float dfdy_v = c.y + d.y * u;

            final float det = dfdx_u * dfdy_v - dfdy_u * dfdx_v;
            if(Math.abs(det) < 0.0001f) break; // Avoid division by zero

            // Newton-Raphson update step
            u += (dx * dfdy_v - dy * dfdx_v) / det;
            v += (dfdx_u * dy - dfdy_u * dx) / det;
        }

        return new Vector2f(u, v);
    }




    //FIXME test intersection before checking the distance
    /**
    * Calculates the distance from the line origin to the intersection point on a rectangle.
    * @param lineOrigin The starting point of the line.
    * @param lineDirection The direction of the line. Must be normalized.
    * @param corners The four corners of the rectangle.
    * @return The distance to the intersection point.
    *     Returns a negative value if the rectangle is behind the line.
    *     Returns Double.MAX_VALUE if no intersection.
    */
    public static double getLineRectangleIntersectionDistance(final @NotNull Vector3f lineOrigin,final @NotNull Vector3f lineDirection,final @NotNull Vector3f[] corners) {
        assert Require.nonNull(lineOrigin, "line origin");
        assert Require.nonNull(lineDirection, "line direction");
        assert Require.nonNull(corners, "corners");
        assert Require.condition(corners.length == 4, "corner array must contain exactly 4 points");

        // Calculate the plane normal from the first three corners
        final Vector3f edge1 = new Vector3f(corners[1]).sub(corners[0]); // BottomRight - BottomLeft (right)
        final Vector3f edge2 = new Vector3f(corners[3]).sub(corners[0]); // TopLeft     - BottomLeft (up)
        final Vector3f planeNormal = new Vector3f(edge1).cross(edge2).normalize();

        // Check if line is parallel to plane
        final double denominator = planeNormal.dot(lineDirection);
        if(Math.abs(denominator) < 1e-6f) {
            return Double.MAX_VALUE; //! Line is parallel to plane
        }

        // Calculate distance to plane intersection
        final Vector3f toPlane = new Vector3f(corners[0]).sub(lineOrigin);
        final double distance = toPlane.dot(planeNormal) / denominator;

        if(findLineRectangleIntersection(lineOrigin, lineDirection, corners, false) != null) return distance;
        else return Double.MAX_VALUE;
    }




    /**
     * Checks whether a point is within the quadrilateral polygon defined by the list of vertices.
     * @param point The coordinates of the point.
     * @param corners A list of 4 vectors identifying the corners of the polygon.
     * @return True if the point is within the polygon, false otherwise.
     */
    static boolean isPointInQuad(final @NotNull Vector2f point, final @NotNull Vector2f[] corners) {
        assert Require.nonNull(point, "point");
        assert Require.nonNull(corners, "corners");
        assert Require.condition(corners.length == 4, "corner array must contain exactly 4 points");


        int sign = 0;
        for(int i = 0; i < 4; i++) {
            final Vector2f a = corners[i];
            final Vector2f b = corners[(i + 1) % 4];
            final float cross = (b.x - a.x) * (point.y - a.y) - (b.y - a.y) * (point.x - a.x);
            if(cross != 0) {
                final int newSign = cross > 0 ? 1 : -1;
                if(sign == 0) sign = newSign;
                else if(newSign != sign) return false;
            }
        }
        return true;
    }




    /**
     * Calculates the length to add to the end of the segments AB and CB in order to make their edges meet in a single point.
     * <p>
     * This takes into account the width of the segments.
     * @param a The first point (start of first segment).
     * @param b The second point (end of first segment, start of second segment).
     * @param c The third point (end of second segment).
     * @param w The width of the segments.
     * @return The calculated additional length
     */
    public static float calcJointLength(final @NotNull Vector2f a, final @NotNull Vector2f b, final @NotNull Vector2f c, final float w) {

        // Calculate direction vectors
        final Vector2f ab = new Vector2f(b.x - a.x, b.y - a.y).normalize();
        final Vector2f cb = new Vector2f(b.x - c.x, b.y - c.y).normalize();

        // Calculate the angle between the two segments
        //! Clamp dot product to avoid numerical errors with acos
        float dot = ab.x * cb.x + ab.y * cb.y;
        dot = Math.max(-1.0f, Math.min(1.0f, dot));

        // Calculate the angle
        final float angle = (float)Math.acos(dot);

        // Handle straight line case (no extension needed)
        if(Math.abs(angle) < 0.001f || Math.abs(angle - Math.PI) < 0.001f) {
            return 0.0f;
        }

        // Calculate half-width offset needed based on miter joint geometry
        final float halfAngle = angle / 2.0f;
        return (w / 2.0f) / (float)Math.tan(halfAngle);
    }
}