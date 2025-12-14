package com.snek.frameworklib.graphics.functional.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.functional.styles.SimpleButtonElmStyle;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Hoverable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A generic button class with clicking and hovering capabilities and a configurable cooldown time.
 */
public abstract class SimpleButtonElm extends PanelElm implements Clickable, Hoverable {
    __base_ButtonElm base;




    /**
     * Creates a new SimpleButtonElm using a custom style.
     * @param level The level in which to place the element.
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     * @param style The custom style.
     */
    protected SimpleButtonElm(
        final @NotNull ServerLevel level,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final int clickCooldown,
        final SimpleButtonElmStyle style
    ) {
        super(level, style);
        base = new __base_ButtonElm(lmbActionName, rmbActionName, clickCooldown);
    }


    /**
     * Creates a new SimpleButtonElm using the default style.
     * @param level The level in which to place the element.
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param lickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected SimpleButtonElm(
        final @NotNull ServerLevel level,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final int clickCooldown
    ) {
        this(level, lmbActionName, rmbActionName, clickCooldown, new SimpleButtonElmStyle());
    }




    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        super.spawn(pos, animate);
        base.spawn(this, getStyle(SimpleButtonElmStyle.class).getHoverPrimerAnimation());
    }




    @Override
    public void onHoverEnter(final @NotNull Player player) {
        base.onHoverEnter(this, getStyle(SimpleButtonElmStyle.class).getHoverEnterAnimation());
    }

    @Override
    public void onHoverTick(final @NotNull Player player) {
        base.onHoverTick(this);
    }

    @Override
    public void onHoverExit(final @Nullable Player player) {
        base.onHoverExit(this, getStyle(SimpleButtonElmStyle.class).getHoverLeaveAnimation());
    }




    @Override
    public boolean attemptClick(final @NotNull Player player, final @NotNull ClickAction click) {
        return base.attemptClick(this, player);
    }

    @Override
    public void onClick(@NotNull Player player, @NotNull ClickAction click) {
        base.onClick(this);
    }
}
