package com.snek.frameworklib.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.utils.Easing;
import com.snek.frameworklib.utils.Easings;








/**
 * A single interpolated transition.
 * <p>
 * This specifies how fast and with what easing a transition should be applied to an element.
 */
public class Transition {

    // Transition data
    private final int duration;
    private final @NotNull Easing easing;
    private boolean additive;
    public final @NotNull InterpolatedData d;

    // Getters
    public          int     getDuration() { return duration; }
    public @NotNull Easing  getEasing  () { return easing;   }
    public          boolean isAdditive () { return additive; }




    /**
     * Creates a new Transition.
     * @param duration The total duration of the transition.
     * @param easing The type of easing to use.
     */
    public Transition(final int duration, final @NotNull Easing easing) {
        this.duration  = duration;
        this.easing    = easing;

        additive   = false;
        d = new InterpolatedData(null, null, null, null);
    }


    /**
     * Creates a new Transition with 0 duration and linear easing.
     */
    public Transition() {
        this(0, Easings.linear);
    }




    /**
     * Specifies that the transform of the element needs to reach a certain value.
     * @param transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition targetTransform(final @Nullable Transform transform) {
        d.setTransform(transform);
        additive = false;
        return this;
    }
    /**
     * Specifies that a certain transform value has to be applied to the transform of the element.
     * @param transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition additiveTransform(final @Nullable Transform transform) {
        d.setTransform(transform);
        additive = true;
        return this;
    }


    /**
     * Specifies that the foreground transform of the element needs to reach a certain value.
     * @param transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition targetTransformFg(final @Nullable Transform transform) {
        d.setTransformFg(transform);
        additive = false;
        return this;
    }
    /**
     * Specifies that a certain transform value has to be applied to the foreground transform of the element.
     * @param transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition additiveTransformFg(final @Nullable Transform transform) {
        d.setTransformFg(transform);
        additive = true;
        return this;
    }


    /**
     * Specifies that the background transform of the element needs to reach a certain value.
     * @param transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition targetTransformBg(final @Nullable Transform transform) {
        d.setTransformBg(transform);
        additive = false;
        return this;
    }
    /**
     * Specifies that a certain transform value has to be applied to the background transform of the element.
     * @param transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition additiveTransformBg(final @Nullable Transform transform) {
        d.setTransformBg(transform);
        additive = true;
        return this;
    }


    /**
     * Specifies that the background color of the element needs to reach a certain value.
     * @param color The background color value.
     * @return This transition.
     */
    public @NotNull Transition targetBgColor(final @Nullable Vector3i bgColor) {
        d.setBgColor(bgColor);
        return this;
    }


    /**
     * Specifies that the background transparency of the element needs to reach a certain value.
     * @param alpha The background transparency value.
     * @return This transition.
     */
    public @NotNull Transition targetBgAlpha(final @Nullable Integer bgAlpha) {
        d.setBgAlpha(bgAlpha);
        return this;
    }


    /**
     * Specifies that the text opacity of the element this transition is applied to needs to reach a certain value.
     * @param opacity The text opacity value.
     * @return This transition.
     */
    public @NotNull Transition targetOpacity(final @Nullable Integer opacity) {
        d.setOpacity(opacity);
        return this;
    }




    /**
     * Creates a transition step from this transition based on the interpolation factor.
     * @param factor The interpolation factor.
     * @return The transition step.
     */
    public @NotNull TransitionStep createStep(final float factor) {
        return new TransitionStep(factor, additive, d);
    }
}
