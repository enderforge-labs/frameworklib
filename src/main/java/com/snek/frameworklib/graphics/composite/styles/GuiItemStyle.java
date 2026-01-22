package com.snek.frameworklib.graphics.composite.styles;

import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.basic.styles.ItemStyle;
import com.snek.frameworklib.graphics.core.styles.Style;
import com.snek.frameworklib.utils.Easings;




/**
 * The default style of GuiItemElm.
 */
public class GuiItemStyle extends ItemStyle {
    public GuiItemStyle() {
        super();
    }


    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scale(PRIMER_ANIMATION_SCALE, PRIMER_ANIMATION_SCALE, 1))
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(Style.S_TIME, Easings.sineOut)
            .additiveTransform(new Transform().scale(1f / PRIMER_ANIMATION_SCALE, 1f / PRIMER_ANIMATION_SCALE, 1))
        );
    }
}
