package com.snek.frameworklib.graphics.designs;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.utils.UtilityClassBase;








@SuppressWarnings("java:S2386") //! Public mutable members
public final class ItemDesigns extends UtilityClassBase {
    public static final @NotNull PolylineData[] MagnifyingGlass = {
        new PolylineData(
            new Vector2f(0.7f, 0.3f),
            new Vector2f(1.0f, 0.0f)
        ),
        new PolylineData(true,
            DesignPrimitives.createCircle(0.4f)
        ).shift(0.4f, 1 - 0.4f)
    };




    public static final @NotNull PolylineData[] Drawer = {
        new PolylineData(
            new Vector2f(0.1f, 0.7f),
            new Vector2f(0.1f, 0.0f),
            new Vector2f(0.9f, 0.0f),
            new Vector2f(0.9f, 0.7f)
        ),
        new PolylineData(true,
            new Vector2f(0.0f, 1.0f),
            new Vector2f(0.0f, 0.7f),
            new Vector2f(1.0f, 0.7f),
            new Vector2f(1.0f, 1.0f)
        ),
        new PolylineData(
            new Vector2f(0.4f, 0.4f),
            new Vector2f(0.6f, 0.4f)
        )
    };




    public static final @NotNull PolylineData[] Coin = {
        new PolylineData(
            new Vector2f(0.5f, 0.7f),
            new Vector2f(0.5f, 0.3f)
        ),
        new PolylineData(true,
            DesignPrimitives.createCircle(0.5f)
        ).shift(0.5f, 0.5f)
    };




    public static final @NotNull PolylineData[] CoinPile = {
        new PolylineData(
            new Vector2f(0.8f, 0.3f),
            new Vector2f(0.8f, 0.0f),
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.0f, 0.3f),
            new Vector2f(1.0f, 0.3f)
        ),
        new PolylineData(
            new Vector2f(0.8f, 0.6f),
            new Vector2f(0.8f, 0.9f),
            new Vector2f(0.0f, 0.9f),
            new Vector2f(0.0f, 0.6f),
            new Vector2f(1.0f, 0.6f)
        ),
        new PolylineData(
            new Vector2f(0.2f, 0.3f),
            new Vector2f(0.2f, 0.6f)
        ),
        new PolylineData(
            new Vector2f(1.0f, 0.6f),
            new Vector2f(1.0f, 0.3f)
        )
    };




    public static final @NotNull PolylineData[] MinecraftChest = {
        new PolylineData(true,
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.0f, 1.0f),
            new Vector2f(1.0f, 1.0f),
            new Vector2f(1.0f, 0.0f)
        ),
        new PolylineData(
            new Vector2f(0.0f, 0.66f),
            new Vector2f(1.0f, 0.66f)
        ),
        new PolylineData(
            new Vector2f(0.5f, 0.76f),
            new Vector2f(0.5f, 0.46f)
        )
    };




    public static final @NotNull PolylineData[] PriceTag = {
        new PolylineData(true,
            new Vector2f(1.0f, 0.6f),
            new Vector2f(0.4f, 0.0f),
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.0f, 0.4f),
            new Vector2f(0.6f, 1.0f)
        )
    };




    public static final @NotNull PolylineData[] UserIcon = {
        new PolylineData(
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.0f, 0.2f),
            new Vector2f(0.2f, 0.4f),
            new Vector2f(0.8f, 0.4f),
            new Vector2f(1.0f, 0.2f),
            new Vector2f(1.0f, 0.0f)
        ),
        new PolylineData(true,
            DesignPrimitives.createCircle(0.2f)
        ).shift(0.5f, 0.85f)
    };
}
