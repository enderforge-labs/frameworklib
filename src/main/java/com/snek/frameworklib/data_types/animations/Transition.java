package com.snek.frameworklib.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.utils.Easing;
import com.snek.frameworklib.utils.Easings;








/**
 * A single interpolated transition.
 * <p>
 * This specifies how fast and with what easing a transition should be applied to an element.
 * @since v1.1.0
 */
public class Transition {

    // Transition data
    private final int duration;
    private @NotNull Easing easing;
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
        assert Require.nonNegative(duration, "duration");
        assert Require.nonNull(easing, "easing function");

        this.duration  = duration;
        this.easing    = easing;
        additive   = false;
        d = new InterpolatedData(null, null, null, null);
    }


    /**
     * Creates a copy of the provided Transition.
     * @param t The transition to copy.
     */
    public Transition(final @NotNull Transition t) {
        assert Require.nonNull(t, "transition");
        assert Require.nonNegative(t.getDuration(), "transition duration");
        assert Require.nonNull(t.getEasing(), "transition easing function");

        this.duration  = t.getDuration();
        this.easing    = t.getEasing();
        additive       = t.isAdditive();
        d = new InterpolatedData(t.d);
    }


    /**
     * Creates a new Transition with 0 duration and linear easing.
     */
    public Transition() {
        this(0, Easings.linear);
    }



    /**
     * Inverts this transition, including its easing function.
     * <p>
     * This makes the transition look like it's being played backwards.
     * <p>
     * Notice: Background color, Background alpha, and opacity values are not affected.
     * @return {@code this}.
     */
    public @NotNull Transition invertWithEasing() {
        assert Require.nonNull(easing, "easing function");

        // Invert transforms
        invert();

        // Invert easing
        final Easing original = easing;
        easing = new Easing(n -> { return 1f - original.compute(1f - n); });

        // Return
        return this;
    }



    /**
     * Inverts this transition without changing its easing function.
     * <p>
     * This makes the transition look like it's being played backwards, but keeping the same rate of change over time as the original one.
     * <p>
     * Notice: Background color, Background alpha, and opacity values are not affected.
     * @return {@code this}.
     */
    public @NotNull Transition invert() {

        // Invert transforms
        if(d.hasTransform  ()) d.getTransform  ().invert();
        if(d.hasTransformFg()) d.getTransformFg().invert();
        if(d.hasTransformBg()) d.getTransformBg().invert();

        // Return
        return this;
    }




    /**
     * Specifies that the transform of the element needs to reach a certain value.
     * @param transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition targetTransform(final @Nullable Transform transform) {
        assert Require.condition(transform == null || transform.getScale() != null,     "transform scale");
        assert Require.condition(transform == null || transform.getRot() != null,       "transform local rotation");
        assert Require.condition(transform == null || transform.getPos() != null,       "transform position");
        assert Require.condition(transform == null || transform.getGlobalRot() != null, "transform global rotation");

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
        assert Require.condition(transform == null || transform.getScale() != null,     "transform scale");
        assert Require.condition(transform == null || transform.getRot() != null,       "transform local rotation");
        assert Require.condition(transform == null || transform.getPos() != null,       "transform position");
        assert Require.condition(transform == null || transform.getGlobalRot() != null, "transform global rotation");

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
        assert Require.condition(transform == null || transform.getScale() != null,     "transform scale");
        assert Require.condition(transform == null || transform.getRot() != null,       "transform local rotation");
        assert Require.condition(transform == null || transform.getPos() != null,       "transform position");
        assert Require.condition(transform == null || transform.getGlobalRot() != null, "transform global rotation");

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
        assert Require.condition(transform == null || transform.getScale() != null,     "transform scale");
        assert Require.condition(transform == null || transform.getRot() != null,       "transform local rotation");
        assert Require.condition(transform == null || transform.getPos() != null,       "transform position");
        assert Require.condition(transform == null || transform.getGlobalRot() != null, "transform global rotation");

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
        assert Require.condition(transform == null || transform.getScale() != null,     "transform scale");
        assert Require.condition(transform == null || transform.getRot() != null,       "transform local rotation");
        assert Require.condition(transform == null || transform.getPos() != null,       "transform position");
        assert Require.condition(transform == null || transform.getGlobalRot() != null, "transform global rotation");

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
        assert Require.condition(transform == null || transform.getScale() != null,     "transform scale");
        assert Require.condition(transform == null || transform.getRot() != null,       "transform local rotation");
        assert Require.condition(transform == null || transform.getPos() != null,       "transform position");
        assert Require.condition(transform == null || transform.getGlobalRot() != null, "transform global rotation");

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
