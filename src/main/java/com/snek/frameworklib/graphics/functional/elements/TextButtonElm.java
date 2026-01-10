package com.snek.frameworklib.graphics.functional.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.PanelTextElm;
import com.snek.frameworklib.graphics.functional.styles.TextButtonStyle;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Hoverable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A generic button element with clicking and hovering capabilities and a configurable cooldown time.
 * <p>
 * This button can also display text.
 */
public abstract class TextButtonElm extends PanelTextElm implements Clickable, Hoverable {
    __base_ButtonElm base;




    /**
     * Creates a new TextButtonElm using a custom style.
     * @param level The level in which to place the element
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     * @param style The custom style.
     */
    protected TextButtonElm(
        final @NotNull ServerLevel level,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final int clickCooldown,
        final TextButtonStyle style
    ) {
        super(level, style);
        assert Require.nonNegative(clickCooldown, "click cooldown");
        base = new __base_ButtonElm(lmbActionName, rmbActionName, clickCooldown);
    }


    /**
     * Creates a new TextButtonElm using the default style.
     * @param level The level in which to place the element.
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param lickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected TextButtonElm(
        final @NotNull ServerLevel level,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final int clickCooldown
    ) {
        this(level, lmbActionName, rmbActionName, clickCooldown, new TextButtonStyle());
    }




    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        super.spawn(pos, animate);
        base.spawn(this, getStyle(TextButtonStyle.class).getHoverPrimerAnimation());
    }


    @Override
    public void finalizeDespawn() {
        base.finalizeDespawn(this, getStyle(TextButtonStyle.class).getHoverInversePrimerAnimation());
        super.finalizeDespawn();
    }




    @Override
    public void onHoverEnter(final @NotNull Player player) {
        base.onHoverEnter(this, player, getStyle(TextButtonStyle.class).getHoverEnterAnimation());
    }

    @Override
    public void onHoverTick(final @NotNull Player player) {
        base.onHoverTick(this, player);
    }

    @Override
    public void onHoverExit(final @Nullable Player player) {
        base.onHoverExit(this, getStyle(TextButtonStyle.class).getHoverLeaveAnimation());
    }




    @Override
    public @Nullable Vector2f checkIntersection(final @NotNull Player player, final boolean calculateIntersectionCoords) {
        if(base.checkIntersection(this, player)) return null;
        return super.checkIntersection(player, calculateIntersectionCoords);
    }




    @Override
    public @Nullable Vector2f attemptClick(final @NotNull Player player, final @NotNull ClickAction click) {
        return base.attemptClick(this, player, click);
    }

    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        base.onClick(this, click, coords);
    }
}
