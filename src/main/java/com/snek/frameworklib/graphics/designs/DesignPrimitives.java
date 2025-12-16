package com.snek.frameworklib.graphics.designs;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.utils.GeometryUtils;
import com.snek.frameworklib.utils.UtilityClassBase;






public final class DesignPrimitives extends UtilityClassBase {
    public static @NotNull List<@NotNull Vector2f> createCircle(final float radius, final boolean close) {
        final @NotNull List<@NotNull Vector2f> r = new ArrayList<>(close ? 9 : 8);
        for(int i = 0; i < 8; ++i) {
            r.add(GeometryUtils.rotateVec2(
                new Vector2f(0, radius),
                (float)Math.toRadians(45) * (i + 0.5f)
            ));
        }
        r.add(r.get(0));
        return r;
    }
    public static final @NotNull List<@NotNull Vector2f> createCircle(final float radius) {
        return createCircle(radius, true);
    }
}
