package com.snek.frameworklib.data_types.displays;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.mojang.math.Transformation;
import com.snek.frameworklib.debug.Assert;
import com.snek.frameworklib.mixin.DisplayAccessorMixin;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.BillboardConstraints;
import net.minecraft.world.entity.Display.BlockDisplay;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.Display.TextDisplay;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.Level;







/**
 * An abstract wrapper for Minecraft's Display Entities.
 * <p>
 * This class allows for better customization and more readable code.
 */
public abstract class CustomDisplay {
    protected @NotNull Display heldEntity;
    protected boolean spawned = false;
    private @NotNull DisplayAccessorMixin getAccessibleDisplay() { return (DisplayAccessorMixin)heldEntity; }




    public boolean isRemoved() {
        return heldEntity.isRemoved();
    }




    /**
     * Creates a new CustomDisplay using an existing DisplayEntity as in-world entity.
     * @param _heldEntity The display entity.
     */
    protected CustomDisplay(final @NotNull Display _heldEntity) {
        Assert.requireNonNull(_heldEntity, "held entity");
        heldEntity = _heldEntity;
        setBrightness(new Brightness(15, 15));
    }




    /**
     * Returns the UUID of the raw display entity.
     * @return The UUID.
     */
    public UUID getUuid() {
        return heldEntity.getUUID();
    }




    /**
     * Spawns the entity into the level.
     * @param level The level to spawn the entity in.
     * @param pos The position of the spawned entity.
     */
    public void spawn(final @NotNull Level level, final @NotNull Vector3d pos) {
        Assert.requireNonNull(level, "level");
        Assert.requireNonNull(pos, "position");

        if(!spawned) {
            spawned = true;

            // Set position and add entity to the level
            heldEntity.setPos(pos.x, pos.y, pos.z);
            level.addFreshEntity(heldEntity);
        }
    }




    /**
     * Replaces the held entity with a fresh one, but only if it was despawned at some point in the past.
     * <p>
     * Notice: This method does NOT copy NBT data to the new entity.
     * @param level The level to spawn the new entity in.
     */
    public void renewEntity(final Level level) {
        Assert.requireNonNull(level, "level");

        if(heldEntity.isRemoved()) {
            if(heldEntity instanceof TextDisplay) {
                heldEntity = new TextDisplay(EntityType.TEXT_DISPLAY, level);
            }
            else if(heldEntity instanceof ItemDisplay) {
                heldEntity = new ItemDisplay(EntityType.ITEM_DISPLAY, level);
            }
            else if(heldEntity instanceof BlockDisplay) {
                heldEntity = new BlockDisplay(EntityType.BLOCK_DISPLAY, level);
            }
        }
    }


    /**
     * Removes the entity from the level.
     */
    public void despawn() {
        if(spawned) {
            spawned = false;

            if(!heldEntity.isRemoved()) {
                heldEntity.remove(RemovalReason.KILLED);
            }
        }
    }








    /**
     * Sets a new transformation value to the entity.
     * <p>
     * This is equivalent to changing the entity's "transformation" NBT.
     * @param transformation The new value.
     */
    public void setTransformation(final @NotNull Transformation transformation) {
        Assert.requireNonNull(transformation, "transformation");
        getAccessibleDisplay().invokeSetTransformation(transformation);
    }


    /**
     * Sets a new interpolation duration value to the entity.
     * <p>
     * This is equivalent to changing the entity's "interpolation_duration" NBT.
     * @param duration The new value, measured in ticks
     */
    public void setInterpolationDuration(final int duration) {
        Assert.requireNonNegative(duration, "interpolation duration");
        getAccessibleDisplay().invokeSetInterpolationDuration(duration);
    }


    /**
     * Starts the interpolation at the end of the current tick.
     * <p>
     * This is equivalent to setting the entity's "start_interpolation" NBT to {@code 0}.
     */
    public void setStartInterpolation() {
        getAccessibleDisplay().invokeSetStartInterpolation(0);
    }


    /**
     * Sets a new billboard mode value to the entity.
     * <p>
     * This is equivalent to changing the entity's "billboard" NBT.
     * @param billboardMode The new value.
     */
    public void setBillboardMode(final @NotNull BillboardConstraints billboardMode) {
        Assert.requireNonNull(billboardMode, "billboard mode");
        getAccessibleDisplay().invokeSetBillboardMode(billboardMode);
    }


    /**
     * Retrieves the entity's billboard mode.
     * @return The current billboard mode.
     */
    public @NotNull BillboardConstraints getBillboardMode() {
        return getAccessibleDisplay().invokeGetBillboardMode();
    }


    /**
     * Sets a new view range value to the entity.
     * <p>
     * This is equivalent to changing the entity's "view_range" NBT.
     * <p>
     * The maximum distance the entity is visible at is {@code (view_range * 64)} blocks.
     * @param viewRange The new value.
     */
    public void setViewRange(final float viewRange) {
        Assert.requireNonNegative(viewRange, "view range");
        getAccessibleDisplay().invokeSetViewRange(viewRange);
    }


    /**
     * Retrieves the entity's view range.
     * <p>
     * The maximum distance the entity is visible at is {@code (view_range * 64)} blocks.
     * @return The current view range.
     */
    public float getViewRange() {
        return getAccessibleDisplay().invokeGetViewRange();
    }


    /**
     * Sets a new brightness value to the entity.
     * <p>
     * This is equivalent to changing the entity's "brightness" NBT.
     * @param brightness
     */
    public void setBrightness(final @NotNull Brightness brightness) {
        Assert.requireNonNull(brightness, "brightness");
        getAccessibleDisplay().invokeSetBrightness(brightness);
    }


    /**
     * Retrieves the entity's brightness.
     * @return The current brightness.
     */
    public @NotNull Brightness getBrightness() {
        return getAccessibleDisplay().invokeGetBrightness();
    }


    /**
     * Sets a new custom name value to the entity.
     * <p>
     * This is equivalent to changing the entity's "custom_name" NBT.
     * @param name The new value.
     */
    public void setCustomName(final @NotNull Component name) {
        Assert.requireNonNull(name, "name");
        heldEntity.setCustomName(name);
    }


    /**
     * Sets a new custom name visible value to the entity.
     * <p>
     * This is equivalent to changing the entity's "custom_name_visible" NBT.
     * @param name The new value.
     */
    public void setCustomNameVisible(final boolean nameVisible) {
        heldEntity.setCustomNameVisible(nameVisible);
    }


    /**
     * Sets a new glowing value to the entity.
     * @param name The new value. True makes the entity glow. False makes it stop glowing.
     */
    public void setGlowing(final boolean glowing) {
        heldEntity.setGlowingTag(glowing);
    }


    /**
     * Sets a new position to the entity.
     * @param pos The new position.
     */
    public void setPos(final @NotNull Vector3d pos) {
        Assert.requireNonNull(pos, "position");
        heldEntity.setPos(pos.x, pos.y, pos.z);
    }


    /**
     * Retrieves the entity's position and returns a copy of it.
     * @return A copy of the current position.
     */
    public @NotNull Vector3f getPosCopy() {
        return heldEntity.getPosition(1f).toVector3f();
    }




    /**
     * Sets a new maximum render width to the entity.
     * <p>
     * This is equivalent to changing the entity's "width" NBT.
     * @param width The new value.
     */
    public void setFrustumCullingBoundingBoxWidth(final float width) {
        Assert.requireNonNegative(width, "width");
        getAccessibleDisplay().invokeSetFrustumCullingBoundingBoxWidth(width);
    }


    /**
     * Retrieves the entity's maximum render width.
     * @return The current maximum render width.
     */
    public float getFrustumCullingBoundingBoxWidth() {
        return getAccessibleDisplay().invokeGetFrustumCullingBoundingBoxWidth();
    }


    /**
     * Sets a new maximum render height to the entity.
     * <p>
     * This is equivalent to changing the entity's "height" NBT.
     * @param height The new value.
     */
    public void setFrustumCullingBoundingBoxHeight(final float height) {
        Assert.requireNonNegative(height, "height");
        getAccessibleDisplay().invokeSetFrustumCullingBoundingBoxHeight(height);
    }


    /**
     * Retrieves the entity's maximum render height.
     * @return The current maximum render height.
     */
    public float getFrustumCullingBoundingBoxHeight() {
        return getAccessibleDisplay().invokeGetFrustumCullingBoundingBoxHeight();
    }


    /**
     * Makes the display ride the specified entity.
     * @param e The entity to ride.
     * @return Whether the display could successfully ride the entity.
     */
    public boolean startRiding(final @NotNull Entity e) {
        Assert.requireNonNull(e, "entity");
        return heldEntity.startRiding(e, true);
    }


    /**
     * Teleports the display to the specified position.
     * @param pos The target position.
     */
    public void teleport(final @NotNull Vector3d pos) {
        Assert.requireNonNull(pos, "position");
        heldEntity.teleportTo(pos.x, pos.y, pos.z);
    }
}
