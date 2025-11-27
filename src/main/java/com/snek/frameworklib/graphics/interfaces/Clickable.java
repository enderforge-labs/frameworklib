package com.snek.frameworklib.graphics.interfaces;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * An interface for elements that can be clicked.
 * <p>
 * It provides click handling and callback methods.
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
     * @return Whether the function accepted the click.
     */
    public boolean attemptClick(@NotNull Player player, @NotNull ClickAction click);


    /**
     * Processes a click event.
     * This method is called on elements that have previously accepted a click event.
     * @param player The player.
     * @param click The type of click.
     */
    public void onClick(@NotNull Player player, @NotNull ClickAction click);
}
