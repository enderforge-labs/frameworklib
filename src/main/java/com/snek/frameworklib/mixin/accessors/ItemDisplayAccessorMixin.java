package com.snek.frameworklib.mixin.accessors;

import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;




/**
 * An accessor mixin for {@link ItemDisplay}.
 * @since v1.1.0
 */
@Mixin(ItemDisplay.class)
public interface ItemDisplayAccessorMixin {
    @Invoker("setItemStack")
    void invokeSetItemStack(ItemStack stack);

    @Invoker("setItemTransform")
    void invokeSetDisplayType(ItemDisplayContext displayType);

    @Invoker("getItemTransform")
    ItemDisplayContext invokeGetDisplayType();
}