package com.snek.frameworklib.graphics.composite.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.graphics.basic.styles.PanelStyle;
import com.snek.frameworklib.graphics.composite.elements.LinePanel;
import com.snek.frameworklib.utils.Easings;




/**
 * The default style of the {@link LinePanel} element.
 */
public class LinePanel_S extends PanelStyle {
    public static final float LINE_SPAWNING_SCALE  = 0.00001f;
    public static final int   SPAWN_ANIMATION_TIME = 10;


    final PolylineData data;
    final float angle;
    final float prevLen;
    final float len;
    final Animation primerAnimation;
    final Animation spawnAnimation;


    public LinePanel_S(final @NotNull PolylineData data, final float angle, final float prevLen, final float len) {
        super(false);
        this.data = data;
        this.angle = angle;
        this.prevLen = prevLen;
        this.len = len;

        primerAnimation = new Animation(new Transition()
            .targetBgColor(data.getColor())
            .targetBgAlpha(data.getAlpha())
            .additiveTransform(new Transform().rotZ(angle).scaleX(LINE_SPAWNING_SCALE))
        );

        final float waitTime      = SPAWN_ANIMATION_TIME / (data.getTotLen() / prevLen);
        final float animationTime = SPAWN_ANIMATION_TIME / (data.getTotLen() / len);
        spawnAnimation = new Animation(
            new Transition(            (int)(waitTime     ),  Easings.linear),
            new Transition(Math.max(1, (int)(animationTime)), Easings.sineOut)
                .additiveTransform(new Transform().scaleX(1f / LINE_SPAWNING_SCALE))
        );

        resetAll();
    }




    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(primerAnimation);
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(spawnAnimation);
    }


    @Override
    public @Nullable Animation getDefaultInversePrimerAnimation() {
        return new Animation(
            new Transition(spawnAnimation .getTransitions().get(1)).invert(),
            new Transition(primerAnimation.getTransitions().get(0)).invert()
        );
    }
}
