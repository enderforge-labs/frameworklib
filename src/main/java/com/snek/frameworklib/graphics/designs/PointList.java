package com.snek.frameworklib.graphics.designs;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.utils.GeometryUtils;






public class PointList {
    private final @NotNull List<@NotNull Vector2f> points;
    public @NotNull List<@NotNull Vector2f> get() { return points; }
    public PointList(final @NotNull List<@NotNull Vector2f> points) { this.points = points; }




    public static @NotNull PointList createCircle(final float radius, final boolean close) {
        final List<@NotNull Vector2f> r = new ArrayList<>(9);
        for(int i = 0; i < 8; ++i) {
            r.add(GeometryUtils.rotateVec2(
                new Vector2f(0, radius),
                (float)Math.toRadians(45) * (i + 0.5f)
            ));
        }
        r.add(r.get(0));
        return new PointList(r);
    }
    public static final @NotNull PointList createCircle(final float radius) {
        return createCircle(radius, true);
    }




    public @NotNull PointList transform(final float shiftX, float shiftY, final float stretchX, float stretchY) {
        for(final Vector2f p : points) {
            p.add(new Vector2f(
                (p.x + shiftX) * stretchX,
                (p.y + shiftY) * stretchY)
            );
        }
        return this;
    }




    public @NotNull PointList shift(final float amountX, final float amountY) {
        return transform(amountX, amountY, 1, 1);
    }
    public @NotNull PointList shiftX(final float amount) {
        return transform(amount, 0, 1, 1);
    }
    public @NotNull PointList shiftY(final float amount) {
        return transform(0, amount, 1, 1);
    }




    public @NotNull PointList stretch(final float amountX, final float amountY) {
        return transform(0, 0, amountX, amountY);
    }
    public @NotNull PointList stretchX(final float amount) {
        return transform(0, 0, amount, 1);
    }
    public @NotNull PointList stretchY(final float amount) {
        return transform(0, 0, 1, amount);
    }




    public @NotNull PointList mirrorXY() {
        return stretch(-1, -1);
    }
    public @NotNull PointList mirrorX() {
        return stretchX(-1);
    }
    public @NotNull PointList mirrorY() {
        return stretchY(-1);
    }
}
