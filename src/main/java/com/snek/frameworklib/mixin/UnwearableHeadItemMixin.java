package com.snek.frameworklib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.snek.frameworklib.utils.MinecraftUtils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.level.gameevent.GameEvent;








/**
 * This mixin stops living entities from equipping player heads if they have the {@link MinecraftUtils#UNWEARABLE_TAG} tag.
 */
@Mixin(LivingEntity.class)
public class UnwearableHeadItemMixin {

    @Inject(method = "onEquipItem", at = @At("HEAD"), cancellable = true)
    private void preventHeadEquip(EquipmentSlot slot, ItemStack oldItem, ItemStack newItem, CallbackInfo ci) {
        if(slot == EquipmentSlot.HEAD && newItem.is(Items.PLAYER_HEAD) && MinecraftUtils.hasTag(newItem, MinecraftUtils.UNWEARABLE_TAG)) {
            ci.cancel();
        }
    }
}