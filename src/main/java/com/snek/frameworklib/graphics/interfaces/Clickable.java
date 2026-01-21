package com.snek.frameworklib.graphics.interfaces;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.utils.MinecraftUtils;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * An interface for elements that can be clicked.
 * <p>
 * It provides click handling and callback methods.
 * @since v1.1.0
 */
public interface Clickable {


    /**
     * Checks if a click event is accepted by the UI element.
     * <p>
     * Calling this method on an element that hasn't been spawned yet is allowed and has no effect.
     * <p>
     * Notice: Click detection is only available for elements with Fixed billboard mode.
     * Calling this function on elements with a different billboard mode is allowed and has no effect.
     * @param player The player.
     * @param click The type of click.
     * @return The 2d coordinates of the click if the element accepted it, null otherwise.
     */
    public Vector2f attemptClick(@NotNull Player player, @NotNull ClickAction click);


    /**
     * Processes a click event.
     * This method is called on elements that have previously accepted a click event.
     * @param player The player.
     * @param click The type of click.
     * @param coords The 2d coordinates of the click.
     */
    public void onClick(@NotNull Player player, @NotNull ClickAction click, final @NotNull Vector2f coords);


    /**
     * Plays a predefined sound to the specified player.
     * <p>
     * Only the specified player will hear this sound.
     * @param player The player to play the sound to.
     */
    public static void playSound(final @NotNull Player player) {
        assert Require.nonNull(player, "player");
        MinecraftUtils.playSoundClient(player, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, 2, 1.5f);
    }
}
