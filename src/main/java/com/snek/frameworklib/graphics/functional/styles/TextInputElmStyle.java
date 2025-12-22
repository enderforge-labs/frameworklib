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


    /**
     * Creates a new TextInputElmStyle.
     */
    public TextInputElmStyle() {
        super();
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W))
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            getDefaultHoverPrimerAnimation()
        ).invert();
    }

    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            getDefaultHoverPrimerAnimation()
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverInversePrimerAnimation() {
        return new Animation(
            getDefaultHoverPrimerAnimation()
        ).invert();
    }
}
