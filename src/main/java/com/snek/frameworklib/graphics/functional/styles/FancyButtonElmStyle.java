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
    private @Nullable Flagged<Animation> hoverPrimerAnimation = null;
    private @Nullable Flagged<Animation> hoverEnterAnimation  = null;
    private @Nullable Flagged<Animation> hoverLeaveAnimation  = null;




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




    // Reset functions
    public void resetHoverPrimerAnimation() { hoverPrimerAnimation = Flagged.from(getDefaultHoverPrimerAnimation()); }
    public void resetHoverEnterAnimation () { hoverEnterAnimation  = Flagged.from(getDefaultHoverEnterAnimation ()); }
    public void resetHoverLeaveAnimation () { hoverLeaveAnimation  = Flagged.from(getDefaultHoverLeaveAnimation ()); }


    // Setters
    public void setHoverPrimerAnimation(final @Nullable Animation _hoverPrimerAnimation) { hoverPrimerAnimation.set(_hoverPrimerAnimation); }
    public void setHoverEnterAnimation (final @Nullable Animation _hoverEnterAnimation ) { hoverEnterAnimation .set(_hoverEnterAnimation ); }
    public void setHoverLeaveAnimation (final @Nullable Animation _hoverLeaveAnimation ) { hoverLeaveAnimation .set(_hoverLeaveAnimation ); }


    // Flagged getters
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverPrimerAnimation() { return hoverPrimerAnimation;}
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverEnterAnimation () { return hoverEnterAnimation ;}
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverLeaveAnimation () { return hoverLeaveAnimation ;}


    // Getters
    public @Nullable Animation getHoverPrimerAnimation() { return hoverPrimerAnimation.get(); }
    public @Nullable Animation getHoverEnterAnimation () { return hoverEnterAnimation .get(); }
    public @Nullable Animation getHoverLeaveAnimation () { return hoverLeaveAnimation .get(); }


    // Edit getters
    public @Nullable Animation editHoverPrimerAnimation() { return hoverPrimerAnimation.edit(); }
    public @Nullable Animation editHoverEnterAnimation () { return hoverEnterAnimation .edit(); }
    public @Nullable Animation editHoverLeaveAnimation () { return hoverLeaveAnimation .edit(); }
}