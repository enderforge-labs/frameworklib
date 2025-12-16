package com.snek.frameworklib.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.utils.Utils;








/**
 * A collection of data that can be interpolated by Minecraft's client.
 * <p>
 * This is used to pre-calculate animations.
 */
public class InterpolatedData {

    // Data
    private @Nullable Transform transform;
    private @Nullable Transform transformFg;
    private @Nullable Transform transformBg;
    private @Nullable Vector3i  bgColor;
    private @Nullable Integer   bgAlpha;
    private @Nullable Integer   opacity;

    // Checks
    public boolean hasTransformFg() { return transformFg != null; }
    public boolean hasTransformBg() { return transformBg != null; }
    public boolean hasTransform  () { return transform   != null; }
    public boolean hasBgColor    () { return bgColor     != null; }
    public boolean hasBgAlpha    () { return bgAlpha     != null; }
    public boolean hasOpacity    () { return opacity     != null; }

    // Setters
    public void setTransformFg(final @Nullable Transform _transformFg) { transformFg = _transformFg; }
    public void setTransformBg(final @Nullable Transform _transformBg) { transformBg = _transformBg; }
    public void setTransform  (final @Nullable Transform _transform  ) { transform   = _transform;   }
    public void setBgColor    (final @Nullable Vector3i  _bgColor    ) { bgColor     = _bgColor;     }
    public void setBgAlpha    (final @Nullable Integer   _bgAlpha    ) { bgAlpha     = _bgAlpha;     }
    public void setOpacity    (final @Nullable Integer   _opacity    ) { opacity     = _opacity;     }

    // Getters
    public @Nullable Transform getTransformFg() { return transformFg; }
    public @Nullable Transform getTransformBg() { return transformBg; }
    public @Nullable Transform getTransform  () { return transform;   }
    public @Nullable Vector3i  getBgColor    () { return bgColor;     }
    public @Nullable Integer   getBgAlpha    () { return bgAlpha;     }
    public @Nullable Integer   getOpacity    () { return opacity;     }




    /**
     * Creates a copy of the provided InterpolatedData.
     * @param d The InterpolatedData to copy.
     */
    public InterpolatedData(final @NotNull InterpolatedData d) {
        this(d.transform, d.bgColor, d.bgAlpha, d.opacity, d.transformFg, d.transformBg);
    }



    /**
     * Creates a new InterpolatedData.
     * <p>
     * Notice: {@code transformFg} and {@code transformBg} are only used by elements that have distinct background and foreground.
     * Single-layer element use only {@code transform}.
     * @param transform The transform.
     * @param bgColor The background color.
     * @param bgAlpha The background transparency.
     * @param opacity The foreground text opacity.
     * @param transformFg The transform applied exclusively to the foreground of the element.
     * @param transformBg The transform applied exclusively to the background of the element.
     */
    public InterpolatedData(
        final @Nullable Transform transform,
        final @Nullable Vector3i  bgColor,
        final @Nullable Integer   bgAlpha,
        final @Nullable Integer   opacity,
        final @Nullable Transform transformFg,
        final @Nullable Transform transformBg
    ) {
        this.transform   = transform   == null ? null : new Transform(transform);
        this.bgColor     = bgColor     == null ? null : new Vector3i(bgColor);
        this.bgAlpha     = bgAlpha;
        this.opacity     = opacity;
        this.transformFg = transformFg == null ? null : new Transform(transformFg);
        this.transformBg = transformBg == null ? null : new Transform(transformBg);
    }




    /**
     * Creates a new InterpolatedData.
     * @param transform The transform.
     * @param bgColor The background color.
     * @param bgAlpha The background transparency.
     * @param opacity The foreground text opacity.
     */
    public InterpolatedData(
        final @Nullable Transform transform,
        final @Nullable Vector3i  bgColor,
        final @Nullable Integer   bgAlpha,
        final @Nullable Integer   opacity
    ) {
        this(transform, bgColor, bgAlpha, opacity, null, null);
    }




    /**
     * Applies a transition step to this InterpolatedData.
     * @param s The step to apply.
     */
    public void apply(final @NotNull TransitionStep s) {
        if(s.d.hasTransform() && hasTransform()) {
            if(s.isAdditive()) transform.interpolate(transform.copy().apply(s.d.getTransform()), s.getFactor());
            else               transform.interpolate(                       s.d.getTransform(),  s.getFactor());
        }
        if(s.d.hasTransformFg() && hasTransformFg()) {
            if(s.isAdditive()) transformFg.interpolate(transformFg.copy().apply(s.d.getTransformFg()), s.getFactor());
            else               transformFg.interpolate(                         s.d.getTransformFg(),  s.getFactor());
        }
        if(s.d.hasTransformBg() && hasTransformBg()) {
            if(s.isAdditive()) transformBg.interpolate(transformBg.copy().apply(s.d.getTransformBg()), s.getFactor());
            else               transformBg.interpolate(                         s.d.getTransformBg(),  s.getFactor());
        }
        if(s.d.hasBgColor() && hasBgColor()) {
            bgColor.set(Utils.interpolateRGB(bgColor, s.d.getBgColor(), s.getFactor()));
        }
        if(s.d.hasBgAlpha() && hasBgAlpha()) {
            bgAlpha = Utils.interpolateI(bgAlpha, s.d.getBgAlpha(), s.getFactor());
        }
        if(s.d.hasOpacity() && hasOpacity()) {
            opacity = Utils.interpolateI(opacity, s.d.getOpacity(), s.getFactor());
        }
    }
}
