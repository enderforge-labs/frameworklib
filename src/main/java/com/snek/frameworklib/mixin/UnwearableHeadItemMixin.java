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
    private UnwearableHeadItemMixin() {}

    /**
     * Inject getEquipmentSlotForItem in order to return MAINHAND as the intended slot for unwearable heads.
     * This makes Minecraft cancel the equip action, avoiding duplication issues.
     * The client is sent update packets automatically.
     * <p>
     * Notice: This doesn't work in creative mode. Creative mode players can wear any head they want.
     */
    @Inject(method = "getEquipmentSlotForItem", at = @At("HEAD"), cancellable = true)
    private static void _getEquipmentSlotForItem(ItemStack item, CallbackInfoReturnable<EquipmentSlot> cir) {
        if(item.is(Items.PLAYER_HEAD) && MinecraftUtils.hasTag(item, MinecraftUtils.UNWEARABLE_TAG)) {
            cir.setReturnValue(EquipmentSlot.MAINHAND);
        }
    }
}