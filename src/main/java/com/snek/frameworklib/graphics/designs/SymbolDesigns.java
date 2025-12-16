package com.snek.frameworklib.graphics.designs;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.utils.UtilityClassBase;








public final class SymbolDesigns extends UtilityClassBase {
    public static final @NotNull PolylineData[] Info = {
        new PolylineData(0.05f,
            new Vector2f(0.5f, 0.90f),
            new Vector2f(0.5f, 0.95f)
        ),
        new PolylineData(0.05f,
            new Vector2f(0.35f, 0.6f),
            new Vector2f(0.5f, 0.6f),
            new Vector2f(0.5f, 0.1f)
        ),
        new PolylineData(0.05f,
            new Vector2f(0.2f, 0.1f),
            new Vector2f(0.8f, 0.1f)
        )
    };




    public static final @NotNull PolylineData[] Settings = {
        new PolylineData(0.05f,
            new Vector2f(0.0f, 0.2f),
            new Vector2f(1.0f, 0.2f)
        ),
        new PolylineData(0.05f,
            new Vector2f(1.0f, 0.8f),
            new Vector2f(0.0f, 0.8f)
        ),
        new PolylineData(0.05f,
            new Vector2f(0.3f, 1.0f),
            new Vector2f(0.3f, 0.5f)
        ),
        new PolylineData(0.05f,
            new Vector2f(0.7f, 0.0f),
            new Vector2f(0.7f, 0.4f)
        )
    };




    public static final @NotNull PolylineData[] CircularArrowCCW = {
        new PolylineData(0.05f,
            new Vector2f(0.5f, 1.0f- 0.025f),
            new Vector2f(0.3f, 0.8f- 0.025f),
            new Vector2f(0.5f, 0.6f- 0.025f)
        ),
        new PolylineData(0.03f,
            DesignPrimitives.createCircle(0.4f, false)
        ).shift(0.5f, 0.4f)
    };
    public static final @NotNull PolylineData[] CircularArrowCW = {
        CircularArrowCCW[0].copy().flipX(),
        CircularArrowCCW[1].copy().flipX(),
    };




    public static final @NotNull PolylineData[] GraphUp = {
        new PolylineData(0.04f,
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.3f, 0.4f),
            new Vector2f(0.7f, 0.4f),
            new Vector2f(1.0f, 0.9f).sub(0.02f, 0.05f)
        ),
        new PolylineData(0.06f,
            new Vector2f(-0.25f, -0.00f),
            new Vector2f(+0.00f, -0.00f),
            new Vector2f(+0.00f, -0.25f)
        ).rotate((float)Math.toRadians(15)).shift(1, 0.9f)
    };
    public static final @NotNull PolylineData[] GraphDown = {
        GraphUp[0].copy().flipY(),
        GraphUp[1].copy().flipY()
    };




    public static final @NotNull PolylineData[] Cross = {
        new PolylineData(0.05f,
            new Vector2f(0.5f, 0.1f),
            new Vector2f(0.5f, 0.9f)
        ),
        new PolylineData(0.05f,
            new Vector2f(0.1f, 0.5f),
            new Vector2f(0.9f, 0.5f)
        )
    };
    public static final @NotNull PolylineData[] CrossSmall = {
        new PolylineData(0.05f,
            new Vector2f(0.5f, 0.2f),
            new Vector2f(0.5f, 0.8f)
        ),
        new PolylineData(0.05f,
            new Vector2f(0.2f, 0.5f),
            new Vector2f(0.8f, 0.5f)
        )
    };




    public static final @NotNull PolylineData[] DiagonalCross = {
        new PolylineData(0.05f,
            new Vector2f(0.1f, 0.1f),
            new Vector2f(0.9f, 0.9f)
        ),
        new PolylineData(0.05f,
            new Vector2f(0.1f, 0.9f),
            new Vector2f(0.9f, 0.1f)
        )
    };
    public static final @NotNull PolylineData[] DiagonalCrossSmall = {
        new PolylineData(0.05f,
            new Vector2f(0.2f, 0.2f),
            new Vector2f(0.8f, 0.8f)
        ),
        new PolylineData(0.05f,
            new Vector2f(0.2f, 0.8f),
            new Vector2f(0.8f, 0.2f)
        )
    };




    public static final @NotNull PolylineData[] CurvedArrowPointingLeft = {
        new PolylineData(0.06f,
            new Vector2f(0.6f,  0.3f),
            new Vector2f(1.0f,  0.3f),
            new Vector2f(1.0f,  0.7f),
            new Vector2f(0.05f, 0.7f)
        ),
        new PolylineData(0.06f,
            new Vector2f(0.2f, 0.9f),
            new Vector2f(0.0f, 0.7f),
            new Vector2f(0.2f, 0.5f)
        )
    };
    public static final @NotNull PolylineData[] CurvedArrowPointingRight = {
        CurvedArrowPointingLeft[0].copy().flipX(),
        CurvedArrowPointingLeft[0].copy().flipX()
    };




    private static final @NotNull PolylineData __ArrowHeadSmall = new PolylineData(0.06f,
        new Vector2f(-0.15f, 0.4f),
        new Vector2f(+0.0f,  0.5f),
        new Vector2f(+0.15f, 0.4f)
    );
    public static final @NotNull PolylineData[] ArrowHeadsPointingOut = {
        __ArrowHeadSmall.copy().shift(0.5f, 0.5f).rotate((float)Math.toRadians(  0)),
        __ArrowHeadSmall.copy().shift(0.5f, 0.5f).rotate((float)Math.toRadians( 90)),
        __ArrowHeadSmall.copy().shift(0.5f, 0.5f).rotate((float)Math.toRadians(180)),
        __ArrowHeadSmall.copy().shift(0.5f, 0.5f).rotate((float)Math.toRadians(270))
    };
    public static final @NotNull PolylineData[] ArrowHeadsPointingIn = {
        __ArrowHeadSmall.copy().rotate((float)(Math.PI)).shift(0.5f, 0.5f).rotate((float)Math.toRadians(  0)),
        __ArrowHeadSmall.copy().rotate((float)(Math.PI)).shift(0.5f, 0.5f).rotate((float)Math.toRadians( 90)),
        __ArrowHeadSmall.copy().rotate((float)(Math.PI)).shift(0.5f, 0.5f).rotate((float)Math.toRadians(180)),
        __ArrowHeadSmall.copy().rotate((float)(Math.PI)).shift(0.5f, 0.5f).rotate((float)Math.toRadians(270))
    };
    public static final @NotNull PolylineData[] DiagonalArrowHeadsPointingOut = {
        ArrowHeadsPointingOut[0].copy().rotate((float)Math.toRadians(45)),
        ArrowHeadsPointingOut[1].copy().rotate((float)Math.toRadians(45)),
        ArrowHeadsPointingOut[2].copy().rotate((float)Math.toRadians(45)),
        ArrowHeadsPointingOut[3].copy().rotate((float)Math.toRadians(45))
    };
    public static final @NotNull PolylineData[] DiagonalArrowHeadsPointingIn = {
        ArrowHeadsPointingIn[0].copy().rotate((float)Math.toRadians(45)),
        ArrowHeadsPointingIn[1].copy().rotate((float)Math.toRadians(45)),
        ArrowHeadsPointingIn[2].copy().rotate((float)Math.toRadians(45)),
        ArrowHeadsPointingIn[3].copy().rotate((float)Math.toRadians(45))
    };



    private static final @NotNull PolylineData __ArrowHeadLarge = new PolylineData(0.06f,
        new Vector2f(-0.15f, 0.2f),
        new Vector2f(+0.15f, 0.5f),
        new Vector2f(-0.15f, 0.8f)
    );
    public static final @NotNull PolylineData[] ArrowHeadPointingLeft = {
        __ArrowHeadLarge.copy().shift(0.5f, 0.5f)
    };
    public static final @NotNull PolylineData[] ArrowHeadPointingRight = {
        ArrowHeadPointingLeft[0].copy().flipX()
    };
}
