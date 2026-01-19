package com.snek.frameworklib.graphics.layout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.graphics.interfaces.Hoverable;

import net.minecraft.world.entity.player.Player;
















/**
 * A div that counts as a hoverable element.
 * This can be used to group interactive elements to improve performance.
 * @since v1.1.0
 */
public class HoverableDiv extends Div implements Hoverable {

    /**
     * Creates a new HoverableDiv.
     */
    public HoverableDiv() {
        super();
    }

    @Override
    public void onHoverEnter(@NotNull final Player player) {
        // Empty
    }

    @Override
    public void onHoverExit(@Nullable final Player player) {
        // Empty
    }
}
