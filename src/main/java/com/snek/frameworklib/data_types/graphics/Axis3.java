package com.snek.frameworklib.data_types.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;




public enum Axis3 {
    X, Y, Z;

    /**
     * Retrieves the index of the element that represents this axis in a standard vector.
     * @return 0 for X, 1 for Y, and 2 for Z.
     */
    public long toIndex() {
        return switch(this) {
            case X -> 0;
            case Y -> 1;
            case Z -> 2;
        };
    }

    /**
     * Calculates the name of this axis.
     * @return The name of this axis. "X", "Y", or "Z".
     */
    public @NotNull String getName() {
        return switch(this) {
            case X -> "X";
            case Y -> "Y";
            case Z -> "Z";
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
            case Z -> v.z;
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
            case Z -> v.z;
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
            case Z -> v.z;
        };
    }

    /**
     * Sets the component of the provided vector corresponding to this axis.
     * @param v The vector.
     * @param n The new value.
     */
    public void set(final @NotNull Vector3f v, final float n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
            case Z: v.z = n; break;
        }
    }

    /**
     * Sets the component of the provided vector corresponding to this axis.
     * @param v The vector.
     * @param n The new value.
     */
    public void set(final @NotNull Vector3d v, final double n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
            case Z: v.z = n; break;
        }
    }

    /**
     * Sets the component of the provided vector corresponding to this axis.
     * @param v The vector.
     * @param n The new value.
     */
    public void set(final @NotNull Vector3i v, final int n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
            case Z: v.z = n; break;
        }
    }

    /**
     * Creates a 3D vector having 1 for this axis and 0 for the other axes.
     * @return The 3D vector that represents this axis.
     */
    public @NotNull Vector3f toVector3f() {
        return switch(this) {
            case X -> new Vector3f(1f, 0f, 0f);
            case Y -> new Vector3f(0f, 1f, 0f);
            case Z -> new Vector3f(0f, 0f, 1f);
        };
    }

    /**
     * Creates a 3D vector having 1 for this axis and 0 for the other axes.
     * @return The 3D vector that represents this axis.
     */
    public @NotNull Vector3d toVector3d() {
        return switch(this) {
            case X -> new Vector3d(1d, 0d, 0d);
            case Y -> new Vector3d(0d, 1d, 0d);
            case Z -> new Vector3d(0d, 0d, 1d);
        };
    }

    /**
     * Creates a 3D vector having 1 for this axis and 0 for the other axes.
     * @return The 3D vector that represents this axis.
     */
    public @NotNull Vector3i toVector3i() {
        return switch(this) {
            case X -> new Vector3i(1, 0, 0);
            case Y -> new Vector3i(0, 1, 0);
            case Z -> new Vector3i(0, 0, 1);
        };
    }
}
