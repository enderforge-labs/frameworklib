package com.snek.frameworklib.graphics.functional.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * This acts as the base class of button elements, even though it is never inherited from.
 * This is because Java doesn't allow multiple inheritance.
 * @since v1.1.0
 */
public final class __base_ButtonElm {
    public static final int INITIAL_COOLDOWN = 10;

    // Data
    protected final RateLimiter clickRateLimiter = new RateLimiter();
    private   final int clickCooldown;
    protected final @Nullable String lmbActionName;
    protected final @Nullable String rmbActionName;

    // A limiter that stops the element from being hoverable while exiting the hover state
    public final RateLimiter hoverCooldownLimiter = new RateLimiter();



    /**
     * Creates a new __base_ButtonElm.
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected __base_ButtonElm(final @Nullable String lmbActionName, final @Nullable String rmbActionName, final int clickCooldown) {
        assert Require.nonNegative(clickCooldown, "click cooldown");

        this.lmbActionName = lmbActionName;
        this.rmbActionName = rmbActionName;
        this.clickCooldown = clickCooldown;
    }



    /**
     * Shared override of spawn from Elm.
     */
    protected void spawn(final @NotNull Elm _this, final @Nullable Animation hoverPrimerAanimation) {
        assert Require.nonNull(_this, "_this");

        clickRateLimiter.renewCooldown(INITIAL_COOLDOWN);
        if(hoverPrimerAanimation != null) {
            _this.applyAnimation(hoverPrimerAanimation, false, false);
        }
    }


    /**
     * Shared override of finalizeDespawn from Elm.
     */
    protected void finalizeDespawn(final @NotNull Elm _this, final @Nullable Animation inverseHoverPrimerAanimation) {
        assert Require.nonNull(_this, "_this");

        // Start inverse hover primer aniamtion
        if(inverseHoverPrimerAanimation != null) {
            _this.applyAnimation(inverseHoverPrimerAanimation, false, false);
        }
    }




    /**
     * Shared override of checkIntersection from Elm.
     * ! Using boolean return instead of Vector2f as the calculation part is done by _this.super.checkIntersection.
     * ! This method returns true if checkIntersection should return null.
     */
    public boolean checkIntersection(final @NotNull Elm _this, final @NotNull Player player) {
        assert Require.nonNull(_this, "_this");
        assert Require.nonNull(player, "player");
        return !hoverCooldownLimiter.attempt();
    }




    /**
     * Shared override of onHoverEnter from Hoverable.
     */
    protected void onHoverEnter(final @NotNull Elm _this, final @NotNull Player player, final @Nullable Animation animation) {
        assert Require.nonNull(_this, "_this");
        assert Require.nonNull(player, "player");

        if(animation != null) {
            _this.applyAnimation(animation, false, true);
        }
    }


    /**
     * Shared override of onHoverTick from Hoverable.
     */
    protected void onHoverTick(final @NotNull Elm _this, final @NotNull Player player) {
        assert Require.nonNull(_this, "_this");
        assert Require.nonNull(player, "player");

        // Update input displays if present
        if(_this.getCanvas() instanceof final InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(lmbActionName);
            c.getRmbIndicator().updateDisplay(rmbActionName);
        }
    }


    /**
     * Shared override of onHoverExit from Hoverable.
     */
    protected void onHoverExit(final @NotNull Elm _this, final @Nullable Animation animation) {
        assert Require.nonNull(_this, "_this");

        // Start hover exit animation and reset the hover cooldown
        if(animation != null) {
            _this.applyAnimation(animation, false, true);
            hoverCooldownLimiter.renewCooldown(animation.getTotalDuration());
        }

        // Update input displays if present
        if(_this.getCanvas() instanceof final InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(null);
            c.getRmbIndicator().updateDisplay(null);
        }
    }


    /**
     * Shared override of attemptClick from Clickable.
     */
    protected @Nullable Vector2f attemptClick(final @NotNull Elm _this, final @NotNull Player player, final @NotNull ClickAction click) {
        assert Require.nonNull(_this, "_this");
        assert Require.nonNull(player, "player");
        assert Require.nonNull(click, "click");

        if(!clickRateLimiter.attempt()) return null;
        clickRateLimiter.renewCooldown(clickCooldown);
        return _this.checkIntersection(player, true);
    }


    /**
     * Shared override of onClick from Clickable.
     */
    public void onClick(final @NotNull Elm _this, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        assert Require.nonNull(_this, "_this");
        assert Require.nonNull(click, "click");
        assert Require.nonNull(coords, "click coords");
        assert Require.inRange(coords.x, 0, 1, "click coords x");
        assert Require.inRange(coords.y, 0, 1, "click coords y");
        //Empty
    }
}
