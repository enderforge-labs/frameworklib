package com.snek.frameworklib.graphics.functional.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.graphics.basic.styles.FancyTextElmStyle;








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
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return __base_ButtonElmStyle.DEFAULT_HOVER_PRIMER_ANIMATION;
    }
    public @Nullable Animation getDefaultHoverEnterAnimation () {
        return __base_ButtonElmStyle.DEFAULT_HOVER_ENTER_ANIMATION;
    }
    public @Nullable Animation getDefaultHoverLeaveAnimation () {
        return __base_ButtonElmStyle.DEFAULT_HOVER_LEAVE_ANIMATION;
    }
    public @Nullable Animation getDefaultHoverInversePrimerAnimation() {
        return __base_ButtonElmStyle.DEFAULT_HOVER_INVERSE_PRIMER_ANIMATION;
    }




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