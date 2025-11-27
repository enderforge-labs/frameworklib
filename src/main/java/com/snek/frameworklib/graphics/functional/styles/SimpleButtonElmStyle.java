package com.snek.frameworklib.graphics.functional.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.utils.Easings;








/**
 * The default tyle of the generic {@Link SimpleButtonElm} element.
 */
public class SimpleButtonElmStyle extends PanelElmStyle {
    private @Nullable Flagged<Animation> hoverPrimerAnimation = null;
    private @Nullable Flagged<Animation> hoverEnterAnimation  = null;
    private @Nullable Flagged<Animation> hoverLeaveAnimation  = null;




    /**
     * Creates a new SimpleButtonElmStyle.
     */
    public SimpleButtonElmStyle() {
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
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(__base_ButtonElmStyle.HOVER_COLOR);
    }

    @Override
    public int getDefaultAlpha() {
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




    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBgAlpha(0)
            .targetOpacity(0)
        );
    }
    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBgAlpha(getDefaultAlpha())
            .targetOpacity(255)
        );
    }
    @Override
    public @Nullable Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBgAlpha(0)
            .targetOpacity(0)
        );
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