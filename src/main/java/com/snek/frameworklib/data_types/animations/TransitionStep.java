package com.snek.frameworklib.data_types.animations;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Assert;








/**
 * A class that represets a single step of a transition.
 */
public class TransitionStep {
    private final          float            factor;
    private final          boolean          additive;
    public  final @NotNull InterpolatedData d;


    // Getters
    public float   getFactor () { return factor; }
    public boolean isAdditive() { return additive; }


    /**
     * Creates a new TransitionStep.
     * @param factor The interpolation factor.
     * @param additive Whether the transform is additive.
     * @param d The interpolated data.
     */
    public TransitionStep(final float factor, final boolean additive, final @NotNull InterpolatedData d) {
        Assert.requireInRange(factor, 0, 1, "factor");
        Assert.requireNonNull(d, "interpolated data");

        this.factor   = factor;
        this.additive = additive;
        this.d        = d;
    }
}
