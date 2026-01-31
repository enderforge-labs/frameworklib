package com.snek.frameworklib.graphics.interfaces;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.utils.common.NetworkUtils;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;








/**
 * An interface for elements that can be scrolled using the mouse wheel.
 * <p>
 * It provides a scroll handling and callback methods.
 * @since v1.1.0
 */
public interface Scrollable {

    /**
     * Called when a player scrolls while looking at this element.
     * @param player The player that scrolled.
     * @param amount The amount of scrolling, in parent's size multiples.
     *     Each 1 scroll means "scroll by 100% of the parent's height/width".
     *     Negative values mean that the player scrolled down instead of up.
     */
    public void onScroll(@NotNull Player player, float amount);


    /**
     * Plays a predefined sound to the specified player.
     * <p>
     * Only the specified player will hear this sound.
     * @param player The player to play the sound to.
     */
    public static void playSound(final @NotNull Player player) {
        assert Require.nonNull(player, "player");
        NetworkUtils.playSoundClient(player, SoundEvents.STONE_BUTTON_CLICK_ON, 1, 1.5f);
    }
}
