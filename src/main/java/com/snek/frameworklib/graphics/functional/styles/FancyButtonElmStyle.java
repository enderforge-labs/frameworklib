package com.snek.frameworklib.graphics.functional.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.graphics.basic.styles.FancyTextElmStyle;
import com.snek.frameworklib.utils.Easings;








/**
 * The default style of the generic {@Link ButtonElm} element.
 */
public class FancyButtonElmStyle extends FancyTextElmStyle {
    private @Nullable Flagged<Animation> hoverPrimerAnimation         = null;
    private @Nullable Flagged<Animation> hoverEnterAnimation          = null;
    private @Nullable Flagged<Animation> hoverLeaveAnimation          = null;
    private @Nullable Flagged<Animation> hoverInversePrimerAnimation  = null;




    /**
     * Creates a new ButtonElmStyle.
     */
    public FancyButtonElmStyle() {
        super();
    }




    @Override
    public void resetAll() {
        super.resetAll();
        resetHoverPrimerAnimation();
        resetHoverEnterAnimation();
        resetHoverLeaveAnimation();
        resetHoverInversePrimerAnimation();
    }

    @Override
    public void flagAll() {
        super.flagAll();
        editHoverPrimerAnimation();
        editHoverEnterAnimation();
        editHoverLeaveAnimation();
        editHoverInversePrimerAnimation();
    }




    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return new Vector3i(__base_ButtonElmStyle.HOVER_COLOR);
    }

    @Override
    public int getDefaultBgAlpha() {
        return __base_ButtonElmStyle.DEFAULT_BG_ALPHA;
    }




    // Default value providers
    public static final @Nullable Animation DEFAULT_HOVER_PRIMER_ANIMATION = new Animation(
        new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
        .additiveTransformBg(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W))
    );
    public @Nullable Animation getDefaultHoverPrimerAnimation       () { return new Animation(DEFAULT_HOVER_PRIMER_ANIMATION); }
    public @Nullable Animation getDefaultHoverEnterAnimation        () { return new Animation(DEFAULT_HOVER_PRIMER_ANIMATION).invert(); }
    public @Nullable Animation getDefaultHoverLeaveAnimation        () { return new Animation(DEFAULT_HOVER_PRIMER_ANIMATION); }
    public @Nullable Animation getDefaultHoverInversePrimerAnimation() { return new Animation(DEFAULT_HOVER_PRIMER_ANIMATION).invert(); }




    // Reset functions
    public void resetHoverPrimerAnimation       () { hoverPrimerAnimation        = Flagged.from(getDefaultHoverPrimerAnimation       ()); }
    public void resetHoverEnterAnimation        () { hoverEnterAnimation         = Flagged.from(getDefaultHoverEnterAnimation        ()); }
    public void resetHoverLeaveAnimation        () { hoverLeaveAnimation         = Flagged.from(getDefaultHoverLeaveAnimation        ()); }
    public void resetHoverInversePrimerAnimation() { hoverInversePrimerAnimation = Flagged.from(getDefaultHoverInversePrimerAnimation()); }


    // Setters
    public void setHoverPrimerAnimation       (final @Nullable Animation hoverPrimerAnimation       ) { this.hoverPrimerAnimation       .set(hoverPrimerAnimation       ); }
    public void setHoverEnterAnimation        (final @Nullable Animation hoverEnterAnimation        ) { this.hoverEnterAnimation        .set(hoverEnterAnimation        ); }
    public void setHoverLeaveAnimation        (final @Nullable Animation hoverLeaveAnimation        ) { this.hoverLeaveAnimation        .set(hoverLeaveAnimation        ); }
    public void setHoverInversePrimerAnimation(final @Nullable Animation hoverInversePrimerAnimation) { this.hoverInversePrimerAnimation.set(hoverInversePrimerAnimation); }


    // Flagged getters
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverPrimerAnimation       () { return hoverPrimerAnimation       ;}
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverEnterAnimation        () { return hoverEnterAnimation        ;}
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverLeaveAnimation        () { return hoverLeaveAnimation        ;}
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverInversePrimerAnimation() { return hoverInversePrimerAnimation;}


    // Getters
    public @Nullable Animation getHoverPrimerAnimation       () { return hoverPrimerAnimation       .get(); }
    public @Nullable Animation getHoverEnterAnimation        () { return hoverEnterAnimation        .get(); }
    public @Nullable Animation getHoverLeaveAnimation        () { return hoverLeaveAnimation        .get(); }
    public @Nullable Animation getHoverInversePrimerAnimation() { return hoverInversePrimerAnimation.get(); }


    // Edit getters
    public @Nullable Animation editHoverPrimerAnimation       () { return hoverPrimerAnimation       .edit(); }
    public @Nullable Animation editHoverEnterAnimation        () { return hoverEnterAnimation        .edit(); }
    public @Nullable Animation editHoverLeaveAnimation        () { return hoverLeaveAnimation        .edit(); }
    public @Nullable Animation editHoverInversePrimerAnimation() { return hoverInversePrimerAnimation.edit(); }
}