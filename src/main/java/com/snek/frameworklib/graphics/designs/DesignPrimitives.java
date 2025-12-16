package com.snek.frameworklib.graphics.designs;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.utils.GeometryUtils;
import com.snek.frameworklib.utils.UtilityClassBase;





public final class DesignPrimitives extends UtilityClassBase {
    public static @NotNull List<@NotNull Vector2f> createCircle(final float radius, final boolean close, final int segments) {
        final int _segments = Math.min(8, Math.max(segments, 1));
        final @NotNull List<@NotNull Vector2f> r = new ArrayList<>(_segments + (close ? 1 : 0));
        for(int i = 0; i <= _segments; ++i) {
            r.add(GeometryUtils.rotateVec2(
                new Vector2f(0, radius),
                (float)Math.toRadians(-45) * (i + 0.5f)
            ));
        }
        if(close) {
            r.add(new Vector2f(r.get(r.size() - 1)));
        }
        return r;
    }
    public static @NotNull List<@NotNull Vector2f> createCircle(final float radius, final boolean close) {
        return createCircle(radius, close, 8);
    }
    public static final @NotNull List<@NotNull Vector2f> createCircle(final float radius) {
        return createCircle(radius, true);
    }
}
