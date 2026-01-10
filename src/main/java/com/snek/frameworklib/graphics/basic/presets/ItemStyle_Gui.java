package com.snek.frameworklib.graphics.basic.presets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.basic.styles.ItemStyle;
import com.snek.frameworklib.graphics.core.styles.Style;
import com.snek.frameworklib.utils.Easings;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;








public class ItemStyle_Gui extends ItemStyle {
    public static final float Z_SCALE = 0.001f;

    private final float size;
    private final ItemStack item;
    private final Vector3f shift;


    /**
     * Creates a new ItemStyle_Gui.
     */
    public ItemStyle_Gui(final @NotNull ItemStack item, final float size) {
        this(item, size, 0);
    }

    /**
     * Creates a new ItemStyle_Gui.
     */
    public ItemStyle_Gui(final @NotNull ItemStack item, final float size, final float shiftZ) {
        this(item, size, shiftZ, new Vector2f());
    }

    /**
     * Creates a new ItemStyle_Gui.
     */
    public ItemStyle_Gui(final @NotNull ItemStack item, final float size, final float shiftZ, final @NotNull Vector2f shift) {
        super(false);
        this.size = size;
        this.item = item;
        this.shift = new Vector3f(shift.x, shift.y, shiftZ);
        resetAll();
    }




    @Override
    public @NotNull ItemStack getDefaultItem() {
        return item;
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(size).scaleZ(Z_SCALE).move(shift).rotY((float)Math.PI);
    }


    @Override
    public @NotNull ItemDisplayContext getDefaultDisplayContext() {
        return ItemDisplayContext.GUI;
    }


    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scale(PRIMER_ANIMATION_SCALE, PRIMER_ANIMATION_SCALE, 1))
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(Style.S_TIME, Easings.sineOut)
            .additiveTransform(new Transform().scale(1f / PRIMER_ANIMATION_SCALE, 1f / PRIMER_ANIMATION_SCALE, 1))
        );
    }
}
