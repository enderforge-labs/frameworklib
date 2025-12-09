package com.snek.frameworklib.utils;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.snek.frameworklib.debug.DebugCheck;
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

        // Calculate the direction vector from lineOrigin to targetCenter
        final Vector3f toCenter = new Vector3f(targetCenter).sub(lineOrigin);

        // Find the point on the line that is closest to targetCenter
        final float t = toCenter.dot(lineDirection);
        final Vector3f closestPoint = new Vector3f(lineDirection).mul(t).add(lineOrigin);

        // Calculate its distance from targetCenter and return true if it's smaller than the radiud
        return targetCenter.distanceSquared(closestPoint) <= targetRadius * targetRadius;
    }




    /**
     * Checks whether a line intersects a rectangle in a 3D space.
     * <p>
     * The line is assumed to be infinite in both directions, regardless of its length.
     * @param lineOrigin The starting point of the line.
     * @param lineDirection The direction of the line. Must be normalized.
     * @param corners The four corners of the rectangle.
     * @return True if the line intersects the rectangle, false otherwise.
     */
    public static boolean checkLineRectangleIntersection(final @NotNull Vector3f lineOrigin, final @NotNull Vector3f lineDirection, final @NotNull Vector3f[] corners) {

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

        final Vector2f p1 = new Vector2f(c1.dot(right), c1.dot(up));
        final Vector2f p2 = new Vector2f(c2.dot(right), c2.dot(up));
        final Vector2f p3 = new Vector2f(c3.dot(right), c3.dot(up));
        final Vector2f p4 = new Vector2f(c4.dot(right), c4.dot(up));

        //! Debug draw calls
        if(DebugCheck.isDebug()) {
            UiDebugWindow.getW().add(p1);
            UiDebugWindow.getW().add(p2);
            UiDebugWindow.getW().add(p3);
            UiDebugWindow.getW().add(p4);
        }

        return isPointInQuad(new Vector2f(0, 0), new Vector2f[]{p1, p2, p3, p4});
    }




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

        // Calculate the plane normal from the first three corners
        Vector3f edge1 = new Vector3f(corners[1]).sub(corners[0]);
        Vector3f edge2 = new Vector3f(corners[2]).sub(corners[0]);
        Vector3f planeNormal = new Vector3f(edge1).cross(edge2).normalize();

        // Check if line is parallel to plane
        float denominator = planeNormal.dot(lineDirection);
        if(Math.abs(denominator) < 1e-6f) {
            return Double.MAX_VALUE; //! Line is parallel to plane
        }

        // Calculate distance to plane intersection
        Vector3f toPlane = new Vector3f(corners[0]).sub(lineOrigin);
        float distance = toPlane.dot(planeNormal) / denominator;

        if(checkLineRectangleIntersection(lineOrigin, lineDirection, corners)) return distance;
        else return Double.MAX_VALUE;
    }




    /**
     * Checks whether a point is within the quadrilateral polygon defined by the list of vertices.
     * @param point The coordinates of  the point.
     * @param quad A list of 4 vectors identifying the corners of the polygon.
     * @return True if the point is within the polygon, false otherwise.
     */
    static boolean isPointInQuad(final @NotNull Vector2f point, final @NotNull Vector2f[] quad) {
        int sign = 0;
        for(int i = 0; i < 4; i++) {
            final Vector2f a = quad[i];
            final Vector2f b = quad[(i + 1) % 4];
            final float cross = (b.x - a.x) * (point.y - a.y) - (b.y - a.y) * (point.x - a.x);
            if(cross != 0) {
                final int newSign = cross > 0 ? 1 : -1;
                if(sign == 0) sign = newSign;
                else if(newSign != sign) return false;
            }
        }
        return true;
    }
}
