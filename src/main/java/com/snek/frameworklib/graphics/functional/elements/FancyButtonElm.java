package com.snek.frameworklib.graphics.functional.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.graphics.basic.elements.FancyTextElm;
import com.snek.frameworklib.graphics.functional.styles.FancyButtonElmStyle;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Hoverable;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A generic button class with clicking and hovering capabilities and a configurable cooldown time.
 */
public abstract class FancyButtonElm extends FancyTextElm implements Clickable, Hoverable {
    __base_ButtonElm base;




    /**
     * Creates a new FancyButtonElm using a custom style.
     * @param world The world in which to place the element
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     * @param style The custom style.
     */
    protected FancyButtonElm(
        final @NotNull ServerLevel world,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final int clickCooldown,
        final FancyButtonElmStyle style
    ) {
        super(world, style);
        base = new __base_ButtonElm(lmbActionName, rmbActionName, clickCooldown);
    }


    /**
     * Creates a new FancyButtonElm using the default style.
     * @param world The world in which to place the element.
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param lickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected FancyButtonElm(
        final @NotNull ServerLevel world,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final int clickCooldown
    ) {
        this(world, lmbActionName, rmbActionName, clickCooldown, new FancyButtonElmStyle());
    }




    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        super.spawn(pos, animate);
        base.spawn(this, getStyle(FancyButtonElmStyle.class).getHoverPrimerAnimation());
    }




    @Override
    public void onHoverEnter(final @NotNull Player player) {
        base.onHoverEnter(this, getStyle(FancyButtonElmStyle.class).getHoverEnterAnimation());
    }

    @Override
    public void onHoverTick(final @NotNull Player player) {
        base.onHoverTick(this);
    }

    @Override
    public void onHoverExit(final @Nullable Player player) {
        base.onHoverExit(this, getStyle(FancyButtonElmStyle.class).getHoverLeaveAnimation());
    }




    @Override
    public boolean attemptClick(final @NotNull Player player, final @NotNull ClickAction click) {
        return base.attemptClick(this, player);
    }

    @Override
    public void onClick(@NotNull Player player, @NotNull ClickAction click) {
        base.onClick(this);
    }




    /**
     * Updates the displayed text.
     * @param textOverride If not null, it replaces the displayed text.
     */
    public abstract void updateDisplay(@Nullable Component textOverride);
}
