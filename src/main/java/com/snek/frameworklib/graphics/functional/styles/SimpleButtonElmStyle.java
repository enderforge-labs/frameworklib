package com.snek.frameworklib.graphics.functional.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.utils.Easings;








/**
 * The default tyle of the generic {@Link SimpleButtonElm} element.
 */
public class SimpleButtonElmStyle extends PanelElmStyle {
    private @Nullable Flagged<Animation> hoverPrimerAnimation         = null;
    private @Nullable Flagged<Animation> hoverEnterAnimation          = null;
    private @Nullable Flagged<Animation> hoverLeaveAnimation          = null;
    private @Nullable Flagged<Animation> hoverInversePrimerAnimation  = null;



    /**
     * Creates a new SimpleButtonElmStyle.
     */
    public SimpleButtonElmStyle(final boolean reset) {
        super(false);
        if(reset) resetAll();
    }

    /**
     * Creates a new SimpleButtonElmStyle.
     */
    public SimpleButtonElmStyle() {
        this(true);
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
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(__base_ButtonElmStyle.HOVER_COLOR);
    }

    @Override
    public int getDefaultAlpha() {
        return __base_ButtonElmStyle.DEFAULT_BG_ALPHA;
    }




    // Default value providers
    public static final @Nullable Animation DEFAULT_HOVER_PRIMER_ANIMATION = new Animation(
        new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
        .additiveTransform(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W))
    );
    public @Nullable Animation getDefaultHoverPrimerAnimation       () { return new Animation(DEFAULT_HOVER_PRIMER_ANIMATION); }
    public @Nullable Animation getDefaultHoverEnterAnimation        () { return new Animation(DEFAULT_HOVER_PRIMER_ANIMATION).invert(); }
    public @Nullable Animation getDefaultHoverLeaveAnimation        () { return new Animation(DEFAULT_HOVER_PRIMER_ANIMATION); }
    public @Nullable Animation getDefaultHoverInversePrimerAnimation() { return new Animation(DEFAULT_HOVER_PRIMER_ANIMATION).invert(); }




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
    public void resetHoverPrimerAnimation       () {        hoverPrimerAnimation = Flagged.from(getDefaultHoverPrimerAnimation       ()); }
    public void resetHoverEnterAnimation        () {        hoverEnterAnimation  = Flagged.from(getDefaultHoverEnterAnimation        ()); }
    public void resetHoverLeaveAnimation        () {        hoverLeaveAnimation  = Flagged.from(getDefaultHoverLeaveAnimation        ()); }
    public void resetHoverInversePrimerAnimation() { hoverInversePrimerAnimation = Flagged.from(getDefaultHoverInversePrimerAnimation()); }


    // Setters
    public void setHoverPrimerAnimation       (final @Nullable Animation hoverPrimerAnimation       ) { this.hoverPrimerAnimation       .set(hoverPrimerAnimation       ); }
    public void setHoverEnterAnimation        (final @Nullable Animation hoverEnterAnimation        ) { this.hoverEnterAnimation        .set(hoverEnterAnimation        ); }
    public void setHoverLeaveAnimation        (final @Nullable Animation hoverLeaveAnimation        ) { this.hoverLeaveAnimation        .set(hoverLeaveAnimation        ); }
    public void setHoverInversePrimerAnimation(final @Nullable Animation hoverInversePrimerAnimation) { this.hoverInversePrimerAnimation.set(hoverInversePrimerAnimation); }


    // Flagged getters
    public final @Nullable Flagged<@NotNull Animation> getFlaggedHoverPrimerAnimation       () { return hoverPrimerAnimation       ;}
    public final @Nullable Flagged<@NotNull Animation> getFlaggedHoverEnterAnimation        () { return hoverEnterAnimation        ;}
    public final @Nullable Flagged<@NotNull Animation> getFlaggedHoverLeaveAnimation        () { return hoverLeaveAnimation        ;}
    public final @Nullable Flagged<@NotNull Animation> getFlaggedHoverInversePrimerAnimation() { return hoverInversePrimerAnimation;}


    // Getters
    public final @Nullable Animation getHoverPrimerAnimation       () { return hoverPrimerAnimation       .get(); }
    public final @Nullable Animation getHoverEnterAnimation        () { return hoverEnterAnimation        .get(); }
    public final @Nullable Animation getHoverLeaveAnimation        () { return hoverLeaveAnimation        .get(); }
    public final @Nullable Animation getHoverInversePrimerAnimation() { return hoverInversePrimerAnimation.get(); }


    // Edit getters
    public @Nullable Animation editHoverPrimerAnimation       () { return hoverPrimerAnimation       .edit(); }
    public @Nullable Animation editHoverEnterAnimation        () { return hoverEnterAnimation        .edit(); }
    public @Nullable Animation editHoverLeaveAnimation        () { return hoverLeaveAnimation        .edit(); }
    public @Nullable Animation editHoverInversePrimerAnimation() { return hoverInversePrimerAnimation.edit(); }


    // With setters
    public @NotNull SimpleButtonElmStyle withHoverPrimerAnimation       (final @Nullable Animation hoverPrimerAnimation       ) { setHoverPrimerAnimation       (hoverPrimerAnimation       ); return this; }
    public @NotNull SimpleButtonElmStyle withHoverEnterAnimation        (final @Nullable Animation hoverEnterAnimation        ) { setHoverEnterAnimation        (hoverEnterAnimation        ); return this; }
    public @NotNull SimpleButtonElmStyle withHoverLeaveAnimation        (final @Nullable Animation hoverLeaveAnimation        ) { setHoverLeaveAnimation        (hoverLeaveAnimation        ); return this; }
    public @NotNull SimpleButtonElmStyle withHoverInversePrimerAnimation(final @Nullable Animation hoverInversePrimerAnimation) { setHoverInversePrimerAnimation(hoverInversePrimerAnimation); return this; }
}