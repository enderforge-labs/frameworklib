package com.snek.frameworklib.data_types.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;




/**
 * An enum representing an axis of the 2D cartesian coordinate system.
 * @since v1.1.0
 */
public enum Axis2 {
    X, Y;

    /**
     * Retrieves the index of the element that represents this axis in a standard vector.
     * @return 0 for X, 1 for Y.
     */
    public long toIndex() {
        return switch(this) {
            case X -> 0;
            case Y -> 1;
        };
    }

    /**
     * Calculates the name of this axis.
     * @return The name of this axis. "X" or "Y".
     */
    public @NotNull String getName() {
        return switch(this) {
            case X -> "X";
            case Y -> "Y";
        };
    }

    /**
     * Converts this Axis2 to an Axis3.
     * @return This axis as an Axis3.
     */
    public @NotNull Axis3 toAxis3() {
        return switch(this) {
            case X -> Axis3.X;
            case Y -> Axis3.Y;
        };
    }

    /**
     * Retrieves the value associated with this axis from the provided vector.
     * @param v The vector.
     * @return The value associated with this axis.
     */
    public float get(final @NotNull Vector2f v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    /**
     * Retrieves the value associated with this axis from the provided vector.
     * @param v The vector.
     * @return The value associated with this axis.
     */
    public double get(final @NotNull Vector2d v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    /**
     * Retrieves the value associated with this axis from the provided vector.
     * @param v The vector.
     * @return The value associated with this axis.
     */
    public int get(final @NotNull Vector2i v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    /**
     * Retrieves the value associated with this axis from the provided vector.
     * @param v The vector.
     * @return The value associated with this axis.
     */
    public float get(final @NotNull Vector3f v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    /**
     * Retrieves the value associated with this axis from the provided vector.
     * @param v The vector.
     * @return The value associated with this axis.
     */
    public double get(final @NotNull Vector3d v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    /**
     * Retrieves the value associated with this axis from the provided vector.
     * @param v The vector.
     * @return The value associated with this axis.
     */
    public int get(final @NotNull Vector3i v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    /**
     * Sets the component of the provided vector corresponding to this axis.
     * @param v The vector.
     * @param n The new value.
     */
    @SuppressWarnings("java:S1301") //! Switch can be replaced with if-else. This is kept as a switch for consistency with Axis3
    public void set(final @NotNull Vector2f v, final float n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    /**
     * Sets the component of the provided vector corresponding to this axis.
     * @param v The vector.
     * @param n The new value.
     */
    @SuppressWarnings("java:S1301") //! Switch can be replaced with if-else. This is kept as a switch for consistency with Axis3
    public void set(final @NotNull Vector2d v, final double n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    /**
     * Sets the component of the provided vector corresponding to this axis.
     * @param v The vector.
     * @param n The new value.
     */
    @SuppressWarnings("java:S1301") //! Switch can be replaced with if-else. This is kept as a switch for consistency with Axis3
    public void set(final @NotNull Vector2i v, final int n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    /**
     * Sets the component of the provided vector corresponding to this axis.
     * @param v The vector.
     * @param n The new value.
     */
    @SuppressWarnings("java:S1301") //! Switch can be replaced with if-else. This is kept as a switch for consistency with Axis3
    public void set(final @NotNull Vector3f v, final float n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    /**
     * Sets the component of the provided vector corresponding to this axis.
     * @param v The vector.
     * @param n The new value.
     */
    @SuppressWarnings("java:S1301") //! Switch can be replaced with if-else. This is kept as a switch for consistency with Axis3
    public void set(final @NotNull Vector3d v, final double n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    /**
     * Sets the component of the provided vector corresponding to this axis.
     * @param v The vector.
     * @param n The new value.
     */
    @SuppressWarnings("java:S1301")
    public void set(final @NotNull Vector3i v, final int n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    /**
     * Creates a 2D vector having 1 for this axis and 0 for the other axis.
     * @return The 2D vector that represents this axis.
     */
    public @NotNull Vector2f toVector2f() {
        return switch(this) {
            case X -> new Vector2f(1f, 0f);
            case Y -> new Vector2f(0f, 1f);
        };
    }

    /**
     * Creates a 2D vector having 1 for this axis and 0 for the other axis.
     * @return The 2D vector that represents this axis.
     */
    public @NotNull Vector2d toVector2d() {
        return switch(this) {
            case X -> new Vector2d(1d, 0d);
            case Y -> new Vector2d(0d, 1d);
        };
    }

    /**
     * Creates a 2D vector having 1 for this axis and 0 for the other axis.
     * @return The 2D vector that represents this axis.
     */
    public @NotNull Vector2i toVector2i() {
        return switch(this) {
            case X -> new Vector2i(1, 0);
            case Y -> new Vector2i(0, 1);
        };
    }

    /**
     * Creates a 3D vector having 1 for this axis and 0 for the other axes.
     * @return The 3D vector that represents this axis.
     */
    public @NotNull Vector3f toVector3f() {
        return toAxis3().toVector3f();
    }

    /**
     * Creates a 3D vector having 1 for this axis and 0 for the other axes.
     * @return The 3D vector that represents this axis.
     */
    public @NotNull Vector3d toVector3d() {
        return toAxis3().toVector3d();
    }

    /**
     * Creates a 3D vector having 1 for this axis and 0 for the other axes.
     * @return The 3D vector that represents this axis.
     */
    public @NotNull Vector3i toVector3i() {
        return toAxis3().toVector3i();
    }
}
