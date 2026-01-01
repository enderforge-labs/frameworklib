package com.snek.frameworklib.graphics.composite.elements;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Easings;

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
    public PolylineSetElm(final @NotNull ServerLevel level, final @NotNull PolylineData... polylines) {
        super();
        assert Require.nonNull(level, "level");
        assert Require.notEmpty(polylines, "polyline list");

        // Create lines and add them to the children list
        for(final PolylineData l : polylines) {
            float previousLen = 0f;
            final List<Vector2f> points = l.getPoints();
            for(int i = 0; i < points.size() - 1; ++i) {
                final Vector2f a = points.get(i);
                final Vector2f b = points.get(i + 1);
                createLine(level, l, a, b, previousLen);
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
     * @param previousLen The total length of the lines that precede this line.
     */
    private void createLine(final @NotNull ServerLevel level, final @NotNull PolylineData l, final @NotNull Vector2f a, final @NotNull Vector2f b, final float previousLen) {
        assert Require.nonNull(level, "level");
        assert Require.nonNull(l, "polyline data");
        assert Require.nonNull(a, "first point");
        assert Require.nonNull(b, "second point");
        assert Require.nonNegative(previousLen, "previous length");


        // Calculate the normalized direction of the line and add the new point positions taking into account the edge value
        final Vector2f normal = b.sub(a, new Vector2f()).normalize(new Vector2f());
        final Vector2f directionalEdge = normal.mul(l.getEdge(), new Vector2f());
        final Vector2f _a = a.sub(directionalEdge, new Vector2f());
        final Vector2f _b = b.add(directionalEdge, new Vector2f());


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
