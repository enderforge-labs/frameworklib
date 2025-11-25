package com.snek.frameworklib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method = "getEquipmentSlotForItem", at = @At("HEAD"), cancellable = true)
    private static void _getEquipmentSlotForItem(ItemStack item, CallbackInfoReturnable<EquipmentSlot> cir) {

        // If the item is not wearable, reset the head item
        if(item.is(Items.PLAYER_HEAD) && MinecraftUtils.hasTag(item, MinecraftUtils.UNWEARABLE_TAG)) {
            cir.setReturnValue(EquipmentSlot.MAINHAND);
        }
    }
}