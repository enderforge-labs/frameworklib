package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.PanelTextElm;
import com.snek.frameworklib.graphics.core.styles.Style;
import com.snek.frameworklib.utils.Easings;








/**
 * The default style of the generic {@link PanelTextElm} element.
 * @since v1.1.0
 */
public class PanelTextStyle extends TextStyle {
    private @NotNull Flagged<@NotNull Vector3i>  bgColor     = null;
    private @NotNull Flagged<@NotNull Integer>   bgAlpha     = null;
    private @NotNull Flagged<@NotNull Transform> transformFg = null;
    private @NotNull Flagged<@NotNull Transform> transformBg = null;


    /**
     * Creates a new PanelTextStyle.
     */
    public PanelTextStyle(final boolean reset) {
        super(false);
        if(reset) resetAll();
    }

    /**
     * Creates a new PanelTextStyle.
     */
    public PanelTextStyle() {
        this(true);
    }




    @Override
    public @NotNull Transform getDefaultTransform() {
        return new Transform();
    }


    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition(Style.D_TIME, Easings.sineOut)
            .targetBgAlpha(0)
            .targetOpacity(0)
            .additiveTransformFg(new Transform().moveX(SPAWN_ANIMATION_SHIFT))
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(Style.S_TIME, Easings.sineOut)
            .targetBgAlpha(getDefaultBgAlpha())
            .targetOpacity(255)
            .additiveTransformFg(new Transform().moveX(-SPAWN_ANIMATION_SHIFT))
        );
    }


    @Override
    public @Nullable Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(Style.D_TIME, Easings.sineOut)
            .targetBgAlpha(0)
            .targetOpacity(0)
        );
    }


    @Override
    public void resetAll() {
        resetBgColor();
        resetBgAlpha();
        resetTransformFg();
        resetTransformBg();
        super.resetAll();
    }

    @Override
    public void flagAll() {
        editBgColor();
        editBgAlpha();
        editTransformFg();
        editTransformBg();
        super.flagAll();
    }





    // Default
    public @NotNull Vector3i  getDefaultBgColor    () { return new Vector3i(20, 2, 20); }
    public          int       getDefaultBgAlpha    () { return 130; }
    public @NotNull Transform getDefaultTransformFg() { return new Transform(); }
    public @NotNull Transform getDefaultTransformBg() { return new Transform(); }


    // Reset
    public void resetBgColor     () { bgColor     = Flagged.from(getDefaultBgColor    ()); }
    public void resetBgAlpha     () { bgAlpha     = Flagged.from(getDefaultBgAlpha    ()); }
    public void resetTransformFg () { transformFg = Flagged.from(getDefaultTransformFg()); }
    public void resetTransformBg () { transformBg = Flagged.from(getDefaultTransformBg()); }


    // Set
    public void setBgColor(final @NotNull Vector3i bgColor) {
        assert Require.nonNull(bgColor, "background color");
        assert Require.inRange(bgColor.x, 0, 255, "background color red");
        assert Require.inRange(bgColor.y, 0, 255, "background color green");
        assert Require.inRange(bgColor.z, 0, 255, "background color blue");
        this.bgColor.set(bgColor);
    }
    public void setBgAlpha(final int bgAlpha) {
        assert Require.inRange(bgAlpha, 0, 255, "background alpha");
        this.bgAlpha.set(bgAlpha);
    }
    public void setTransformFg(final @NotNull Transform transformFg) {
        assert Require.nonNull(transformFg, "foreground transform");
        this.transformFg.set(transformFg);
    }
    public void setTransformBg(final @NotNull Transform transformBg) {
        assert Require.nonNull(transformBg, "background transform");
        this.transformBg.set(transformBg);
    }


    // Get flagged
    public final @NotNull Flagged<@NotNull Vector3i>  getFlaggedBgColor    () { return bgColor; }
    public final @NotNull Flagged<@NotNull Integer>   getFlaggedBgAlpha    () { return bgAlpha; }
    public final @NotNull Flagged<@NotNull Transform> getFlaggedTransformFg() { return transformFg; }
    public final @NotNull Flagged<@NotNull Transform> getFlaggedTransformBg() { return transformBg; }


    // Get
    public final @NotNull Vector3i  getBgColor    () { return bgColor    .get(); }
    public final          int       getBgAlpha    () { return bgAlpha    .get(); }
    public final @NotNull Transform getTransformFg() { return transformFg.get(); }
    public final @NotNull Transform getTransformBg() { return transformBg.get(); }


    // Edit
    public @NotNull Vector3i  editBgColor    () { return bgColor    .edit(); }
    public          int       editBgAlpha    () { return bgAlpha    .edit(); }
    public @NotNull Transform editTransformFg() { return transformFg.edit(); }
    public @NotNull Transform editTransformBg() { return transformBg.edit(); }


    // With
    public @NotNull PanelTextStyle withBgColor    (final @NotNull Vector3i  bgColor    ) { setBgColor    (bgColor    ); return this; }
    public @NotNull PanelTextStyle withBgAlpha    (final          int       bgAlpha    ) { setBgAlpha    (bgAlpha    ); return this; }
    public @NotNull PanelTextStyle withTransformFg(final @NotNull Transform transformFg) { setTransformFg(transformFg); return this; }
    public @NotNull PanelTextStyle withTransformBg(final @NotNull Transform transformBg) { setTransformBg(transformBg); return this; }
}