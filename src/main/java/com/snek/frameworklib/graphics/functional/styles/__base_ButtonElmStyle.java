package com.snek.frameworklib.graphics.functional.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.utils.Easings;




/**
 * A class that contains static data shared by {@link FancyButtonElmStyle} and {@link SimpleButtonElmStyle}.
 */
public final class __base_ButtonElmStyle {
    private __base_ButtonElmStyle() {}

    public static final @NotNull Vector3i HOVER_COLOR          = new Vector3i(220, 220, 220);
    public static final          float    HIDDEN_W             = 0.00001f;
    public static final          int      HOVER_ANIMATION_TIME = 10;

    public static final int DEFAULT_BG_ALPHA = 255;


    public static final @Nullable Animation DEFAULT_HOVER_PRIMER_ANIMATION= new Animation(
        new Transition()
        .additiveTransformBg(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W))
    );

    public static final @Nullable Animation DEFAULT_HOVER_ENTER_ANIMATION = new Animation(
        new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
        .additiveTransformBg(new Transform().scaleX(1f / __base_ButtonElmStyle.HIDDEN_W))
    );

    public static final @Nullable Animation DEFAULT_HOVER_LEAVE_ANIMATION = new Animation(
        new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
        .additiveTransformBg(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W))
    );
}