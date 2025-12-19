package com.snek.frameworklib.data_types.displays;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Assert;
import com.snek.frameworklib.mixin.ItemDisplayAccessorMixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;







/**
 * A wrapper for Minecraft's ItemDisplayEntity.
 * <p>
 * This class allows for better customization and more readable code.
 */
public class CustomItemDisplay extends CustomDisplay {
    public @NotNull ItemDisplay getRawDisplay() { return (ItemDisplay)heldEntity; }
    private @NotNull ItemDisplayAccessorMixin getAccessibleDisplay() { return (ItemDisplayAccessorMixin)heldEntity; }




    /**
     * Creates a new CustomItemDisplay using an existing ItemDisplayEntity.
     * @param rawDisplay The display entity.
     */
    public CustomItemDisplay(final @NotNull ItemDisplay rawDisplay) {
        super(rawDisplay);
    }

    /**
     * Creates a new CustomItemDisplay in the specified level.
     * @param level The level.
     */
    public CustomItemDisplay(final @NotNull Level level) {
        super(new ItemDisplay(EntityType.ITEM_DISPLAY, level));
    }




    /**
     * Sets a new item stack value to the entity.
     * <p>
     * This is equivalent to changing the entity's "item" NBT.
     * @param itemStack The new value.
     */
    public void setItemStack(final @NotNull ItemStack itemStack) {
        Assert.requireNonNull(itemStack, "item stack");
        getAccessibleDisplay().invokeSetItemStack(itemStack);
    }


    /**
     * Sets a new display type value to the entity.
     * <p>
     * This is equivalent to changing the entity's "item_display" NBT.
     * @param displayType The new value.
     */
    public void setDisplayType(final @NotNull ItemDisplayContext displayType) {
        Assert.requireNonNull(displayType, "item display type");
        getAccessibleDisplay().invokeSetDisplayType(displayType);
    }


    /**
     * Retrieves the entity's display type value.
     * @return The current display type.
     */
    public @NotNull ItemDisplayContext getDisplayType() {
        return (ItemDisplayContext)getAccessibleDisplay().invokeGetDisplayType();
    }
}
