package com.snek.frameworklib.graphics.basic.presets;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.basic.styles.ItemStyle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;








public class ItemElm_Gui extends ItemElm {
    public static final float Z_SCALE = 0.001f;




    public ItemElm_Gui(@NotNull ServerLevel level) {
        this(level, new ItemStyle());
    }

    public ItemElm_Gui(@NotNull ServerLevel level, @NotNull ItemStyle style) {
        super(level, style.withDisplayContext(ItemDisplayContext.GUI));
    }

    public ItemElm_Gui(@NotNull ServerLevel level, final @NotNull ItemStack item) {
        this(level, new ItemStyle().withItem(item));
    }

    public ItemElm_Gui(@NotNull ServerLevel level, @NotNull ItemStyle style, final @NotNull ItemStack item) {
        this(level, style.withItem(item));
    }




    //! Scale the item to match the element's size
    @Override
    public @NotNull Transform __calcTransform() {
        final float size = Math.min(getAbsSize().x, getAbsSize().y);
        return super.__calcTransform().scale(size, size, Z_SCALE).rotY((float)Math.PI);
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
