package com.snek.frameworklib.data_types.graphics;

import org.jetbrains.annotations.NotNull;




public enum Direction {
    NORTH    (0, "North    "),
    NORTHWEST(1, "Northwest"),
    WEST     (2, "West     "),
    SOUTHWEST(3, "Southwest"),
    SOUTH    (4, "South    "),
    SOUTHEAST(5, "Southeast"),
    EAST     (6, "East     "),
    NORTHEAST(7, "Northeast");




    private final int eighths;
    private final double radians;
    private final double degrees;
    private final @NotNull String name;

    private Direction(final int eighths, final @NotNull String name) {
        this.eighths = eighths;
        this.degrees = 360d / 8d * eighths;
        this.radians = Math.PI / 4d * eighths;
        this.name = name;
    }




    /**
     * Retrieves the cardinal or intercardinal name of this direction.
     * @return The direction name (e.g., "North", "Northeast", "East", "Southeast", etc.).
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Retrieves the angle that represents this direction, in eighths of a full rotation, starting from North and increasing counter-clockwise.
     * @return The angle (0 for North, 1 for Northwest, etc.).
     */
    public int getEighths() {
        return eighths;
    }

    /**
     * Retrieves the angle that represents this direction, in radians, starting from North and increasing counter-clockwise.
     * @return The angle (0d for North, π/4d for Northwest, etc.).
     */
    public double getRadians() {
        return radians;
    }

    /**
     * Retrieves the angle that represents this direction, in degrees, starting from North and increasing counter-clockwise.
     * @return The angle (0d for North, 45d for Northwest, etc.).
     */
    public double getDegrees() {
        return degrees;
    }




    /**
     * Calculates the direction from a rotation expressed in eighths of a full rotation.
     * @param eighths The rotation, where 0 is North and positive values rotate clockwise.
     *     This can safely exceed the [0, 7] range.
     * @return The direction represented by the provided rotation.
     */
    public static @NotNull Direction fromEighths(final int eighths) {

        //! Reduce range to [-7, 7], then add 8 to shift range to [1, 15], then reduce to [0, 7]
        final int _eighths = (eighths % 8 + 8) % 8;

        // Return corresponding enum value
        return switch(_eighths) {
            case 0  -> NORTH;
            case 1  -> NORTHWEST;
            case 2  -> WEST;
            case 3  -> SOUTHWEST;
            case 4  -> SOUTH;
            case 5  -> SOUTHEAST;
            case 6  -> EAST;
            default -> NORTHEAST;
        };
    }


    /**
    * Calculates the direction from a rotation expressed in radians.
    * @param radians The rotation in radians, where 0d is North and positive values rotate clockwise.
    *     This can safely exceed the [0d, 2π] range.
    * @return The direction closest to the provided rotation.
    */
    public static @NotNull Direction fromRadians(final double radians) {
        return fromEighths((int)Math.round((radians / (2d * Math.PI)) * 8d));
    }


    /**
     * Calculates the direction from a rotation expressed in degrees.
     * @param degrees The rotation in degrees, where 0d is North and positive values rotate clockwise.
     *     This can safely exceed the [0d, 360d] range.
     * @return The direction closest to the provided rotation.
     */
    public static @NotNull Direction fromDegrees(final double degrees) {
        return fromEighths((int)Math.round((degrees / 360d) * 8d));
    }
}
