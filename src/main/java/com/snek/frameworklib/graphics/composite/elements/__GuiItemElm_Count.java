package com.snek.frameworklib.graphics.composite.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.basic.styles.TextStyle;

import net.minecraft.server.level.ServerLevel;



/**
 * A {@link TextElm} used to display the count of a {@link GuiItemElm}.
 * @since v1.1.0
 */
public class __GuiItemElm_Count extends TextElm {

    public __GuiItemElm_Count(final @NotNull ServerLevel level) {
        super(level);
    }

    public __GuiItemElm_Count(final @NotNull ServerLevel level, final @NotNull TextStyle style) {
        super(level, style);
    }

    //! Scale the item to match the element's size
    @Override
    public @NotNull Transform __calcTransform() {
        final float size = Math.min(getAbsSize().x, getAbsSize().y);
        return super.__calcTransform().scale(size, size, 1f).move(size / 3f, -size / 4f, 0f);
    }


    //! Enforce a 1:1 aspect ratio
    //TODO move to Elm. Make this automatic
    //TODO AspectRatio.FREE
    //TODO AspectRatio.1_1
    //TODO AspectRatio.3_4
    //TODO AspectRatio.16_9
    @Override
    public void updateAbsSizeSelf() {
        if(parent == null) {
            final float minDim = Math.min(localSize.x, localSize.y);
            absSize.set(minDim, minDim);
        }
        else {
            final Vector2f unconstrained = new Vector2f(parent.getAbsSize()).mul(localSize);
            final float minDim = Math.min(unconstrained.x, unconstrained.y);
            absSize.set(minDim, minDim);
        }
        updateAbsPosSelf();
    }
}
