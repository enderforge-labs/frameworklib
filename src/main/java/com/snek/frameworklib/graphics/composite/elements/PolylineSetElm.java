package com.snek.frameworklib.graphics.composite.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.GeometryUtils;

import net.minecraft.server.level.ServerLevel;








/**
 * A composite UI element that can display many multi-segment lines.
 * <p>
 * Each polyline is defined by a list of 2 or more points and has configurable color, opacity and width.
 */
public class PolylineSetElm extends Div {
    public static final float LINE_SPAWNING_SCALE  = 0.00001f;
    public static final int   SPAWN_ANIMATION_TIME = 10;




    /**
     * Creates a new PolylineSetElm.
     * @param polylines The list of polylines.
     */
    @SuppressWarnings("java:S2583")
    public PolylineSetElm(final @NotNull ServerLevel level, final @NotNull PolylineData... polylines) {
        super();
        assert Require.nonNull(level, "level");
        assert Require.notEmpty(polylines, "polyline list");

        // Create lines and add them to the children list
        for(final PolylineData l : polylines) {
            float previousLen = 0f;

            // Calculate actual list of points (account for cyclic parameter)
            final List<Vector2f> points = new ArrayList<>(l.getPoints());
            if(l.isCyclic()) points.add(points.get(0));

            // Draw each segment one by one
            for(int i = 0; i < points.size() - 1; ++i) {
                final Vector2f a = points.get(i);
                final Vector2f b = points.get(i + 1);
                final Vector2f prev = i > 0                 ? points.get(i - 1) : (l.isCyclic() ? points.get(points.size() - 2)  : null);
                final Vector2f next = i < points.size() - 2 ? points.get(i + 2) : (l.isCyclic() ? points.get(1)                  : null);
                createLine(level, l, a, b, prev, next, previousLen);
                previousLen += a.distance(b);
            }
        }
    }




    /**
     * Creates a new line and adds it to this element's children.
     * @param level The level to spawn the display entities in.
     * @param l The polyline data that specifies color, opacity and width.
     * @param a The first point of the line.
     * @param b The second point of the line.
     * @param prevPoint The point before 'a' in the polyline (use null if this is the first segment).
     * @param nextPoint The point after 'b' in the polyline (use null if this is the last segment).
     * @param previousLen The total length of the lines that precede this line.
     */

    private void createLine(
        final @NotNull ServerLevel level, final @NotNull PolylineData l,
        final @NotNull Vector2f a, final @NotNull Vector2f b,
        final @Nullable Vector2f prevPoint, final @Nullable Vector2f nextPoint,
        final float previousLen
    ) {
        assert Require.nonNull(level, "level");
        assert Require.nonNull(l, "polyline data");
        assert Require.nonNull(a, "first point");
        assert Require.nonNull(b, "second point");
        assert Require.nonNegative(previousLen, "previous length");


        // Calculate normal
        final Vector2f normal = b.sub(a, new Vector2f()).normalize();

        // Calculate adjusted segments (account for joint length)
        final float edgeA = prevPoint == null ? 0 : GeometryUtils.calcJointLength(prevPoint, a, b, l.getWidth());
        final float edgeB = nextPoint == null ? 0 : GeometryUtils.calcJointLength(a, b, nextPoint, l.getWidth());
        final Vector2f directionalEdgeA = normal.mul(-edgeA, new Vector2f());
        final Vector2f directionalEdgeB = normal.mul(edgeB, new Vector2f());
        final Vector2f _a = a.add(directionalEdgeA, new Vector2f());
        final Vector2f _b = b.add(directionalEdgeB, new Vector2f());


        // Calculate line direction, length and angle
        final Vector2f dir    = _b.sub(_a, new Vector2f());                 // The direction of the line
        final float    len    = dir.length();                               // The length of the line
        final float    angle  = (float)Math.atan2(dir.y, dir.x);            // The angle of the line


        // Create the panel and set its size and position
        final LinePanel e = (LinePanel)addChild(new LinePanel(level));      // Create the panel
        e.setSize(new Vector2f(len, l.getWidth()));                         // Set the size to match the line's length and width
        e.setPos(                                                           // Set the position
            new Vector2f(_a)                                                    // Start by moving the origin (center of lower edge) the first point
                .add(new Vector2f(-(1 - len) / 2f, 0f))                             // Move it horizontally to align the bottom left edge with the point
                .add(new Vector2f(normal.y, -normal.x).mul(l.getWidth() / 2f))      // Center the line to its width
        );


        // Change its color and rotate it by overwriting the primer animation and spawning animation
        e.getStyle().setPrimerAnimation(new Animation(
            new Transition()
                .targetBgColor(l.getColor())
                .targetBgAlpha(l.getAlpha())
                .additiveTransform(new Transform().rotZ(angle).scaleX(LINE_SPAWNING_SCALE))
        ));
        final float waitTime      = SPAWN_ANIMATION_TIME / (l.getTotLen() / previousLen);
        final float animationTime = SPAWN_ANIMATION_TIME / (l.getTotLen() / len);
        e.getStyle().setSpawnAnimation(new Animation(
            new Transition(            (int)(waitTime     ),  Easings.linear),
            new Transition(Math.max(1, (int)(animationTime)), Easings.sineOut)
                .additiveTransform(new Transform().scaleX(1f / LINE_SPAWNING_SCALE))
        ));
    }




    @Override
    public void updateAbsSizeSelf() {
        final Vector2f adjustedLocalSize = new Vector2f(Math.min(localSize.x, localSize.y));
        if(parent == null) {
            absSize.set(adjustedLocalSize);
        }
        else {
            final Vector2f adjustedParentAbsSize = new Vector2f(Math.min(parent.getAbsSize().x, parent.getAbsSize().y));
            absSize.set(new Vector2f(adjustedParentAbsSize).mul(adjustedLocalSize));
        }
        updateAbsPosSelf();
    }
}
