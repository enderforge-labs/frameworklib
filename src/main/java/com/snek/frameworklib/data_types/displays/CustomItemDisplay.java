package com.snek.frameworklib.data_types.displays;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.mixin.accessors.DisplayAccessorMixin;
import com.snek.frameworklib.mixin.accessors.ItemDisplayAccessorMixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;







/**
 * A wrapper for Minecraft's ItemDisplayEntity.
 * <p>
 * This class allows for better customization and more readable code.
 * @since v1.1.0
 */
public class CustomItemDisplay extends CustomDisplay {

    /**
     * Retrieves the display entity held by this {@link CustomItemDisplay}.
     * @return The held {@link ItemDisplay}.
     */
    public @NotNull ItemDisplay getRawDisplay() {
        assert Require.nonNull(heldEntity, "held entity");
        return (ItemDisplay)heldEntity;
    }

    /**
     * Retrieves the display entity held by this {@link CustomItemDisplay} as a {@link ItemDisplayAccessorMixin} to allow access to private methods.
     * @return The held {@link ItemDisplay} as a {@link DisplayAccessorMixin}.
     */
    private @NotNull ItemDisplayAccessorMixin getAccessibleItemDisplay() {
        assert Require.nonNull(heldEntity, "held entity");
        return (ItemDisplayAccessorMixin)heldEntity;
    }




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
        assert Require.nonNull(itemStack, "item stack");
        getAccessibleItemDisplay().invokeSetItemStack(itemStack);
    }


    /**
     * Sets a new display type value to the entity.
     * <p>
     * This is equivalent to changing the entity's "item_display" NBT.
     * @param displayType The new value.
     */
    public void setDisplayType(final @NotNull ItemDisplayContext displayType) {
        assert Require.nonNull(displayType, "item display type");
        getAccessibleItemDisplay().invokeSetDisplayType(displayType);
    }


    /**
     * Retrieves the entity's display type value.
     * @return The current display type.
     */
    public @NotNull ItemDisplayContext getDisplayType() {
        return getAccessibleItemDisplay().invokeGetDisplayType();
    }
}
