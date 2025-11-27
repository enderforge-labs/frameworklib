package com.snek.frameworklib.graphics.functional.styles;

import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.utils.Easings;








/**
 * The style of the generic TextInputElm UI element.
 */
public class TextInputElmStyle extends FancyButtonElmStyle {
    public static final float UNHOVERED_W = 0.05f;


    /**
     * Creates a new TextInputElmStyle.
     */
    public TextInputElmStyle() {
        super();
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation () {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / UNHOVERED_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation () {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
        );
    }
}
