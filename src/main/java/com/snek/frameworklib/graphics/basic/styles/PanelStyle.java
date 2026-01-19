package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.core.styles.Style;
import com.snek.frameworklib.utils.Easings;








/**
 * The default style of the generic {@link PanelElm} element.
 * @since v1.1.0
 */
public class PanelStyle extends Style {
    private @NotNull Flagged<@NotNull Vector3i> color = null;
    private @NotNull Flagged<@NotNull Integer>  alpha = null;




    /**
     * Creates a new PanelStyle.
     */
    public PanelStyle(final boolean reset) {
        super(false);
        if(reset) resetAll();
    }

    /**
     * Creates a new PanelStyle.
     */
    public PanelStyle() {
        this(true);
    }


    @Override
    public void resetAll() {
        resetColor();
        resetAlpha();
        super.resetAll();
    }

    @Override
    public void flagAll() {
        editColor();
        editAlpha();
        super.flagAll();
    }




    @Override
    public Transform getDefaultTransform() {
        return new Transform()
        ;
    }


    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition(Style.S_TIME, Easings.sineOut)
            .targetBgAlpha(0)
            .targetBgColor(getDefaultColor())
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(Style.S_TIME, Easings.sineOut)
            .targetBgAlpha(getDefaultAlpha())
        );
    }


    @Override
    public @Nullable Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(Style.D_TIME, Easings.sineOut)
            .targetBgAlpha(0)
        );
    }




    // Default
    public @NotNull Vector3i getDefaultColor() { return new Vector3i(20, 2, 20); }
    public          int      getDefaultAlpha() { return 255; }


    // Reset
    public void resetColor() { color = Flagged.from(getDefaultColor()); }
    public void resetAlpha() { alpha = Flagged.from(getDefaultAlpha()); }


    // Set
    public void setColor(final @NotNull Vector3i color ) {
        assert Require.nonNull(color, "color");
        assert Require.inRange(color.x, 0, 255, "color red");
        assert Require.inRange(color.y, 0, 255, "color green");
        assert Require.inRange(color.z, 0, 255, "color blue");
        this.color.set(color);
    }
    public void setAlpha(final int alpha ) {
        assert Require.inRange(alpha, 0, 255, "alpha");
        this.alpha.set(alpha);
    }


    // Get flagged
    public final @NotNull Flagged<@NotNull Vector3i> getFlaggedColor() { return color; }
    public final @NotNull Flagged<@NotNull Integer>  getFlaggedAlpha() { return alpha; }


    // Get
    public final @NotNull Vector3i getColor() { return color.get(); }
    public final          int      getAlpha() { return alpha.get(); }


    // Edit
    public @NotNull Vector3i editColor() { return color.edit(); }
    public          int      editAlpha() { return alpha.edit(); }


    // With
    public @NotNull PanelStyle withColor(final @NotNull Vector3i color) { setColor(color); return this; }
    public @NotNull PanelStyle withAlpha(final          int      alpha) { setAlpha(alpha); return this; }
}