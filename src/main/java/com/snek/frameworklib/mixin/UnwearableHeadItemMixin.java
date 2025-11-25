package com.snek.frameworklib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.frameworklib.utils.MinecraftUtils;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * This mixin stops living entities from equipping player heads if they have the {@link MinecraftUtils#UNWEARABLE_TAG} tag.
 */
@Mixin(LivingEntity.class)
public class UnwearableHeadItemMixin {

    @Inject(method = "setItemSlot", at = @At("HEAD"), cancellable = true)
    private void preventEquip(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        if(slot == EquipmentSlot.HEAD && stack.is(Items.PLAYER_HEAD) && MinecraftUtils.hasTag(stack, MinecraftUtils.UNWEARABLE_TAG)) {
            ci.cancel();
        }
    }
}