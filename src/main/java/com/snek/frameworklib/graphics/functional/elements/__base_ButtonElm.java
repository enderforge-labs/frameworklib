package com.snek.frameworklib.graphics.functional.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.world.entity.player.Player;








/**
 * This acts as the base class of button elements, even though it is never inherited from.
 * This is because Java doesn't allow multiple inheritance.
 */
public final class __base_ButtonElm {
    public static final int INITIAL_COOLDOWN = 10;

    // Data
    protected final RateLimiter clickRateLimiter = new RateLimiter();
    private   final int clickCooldown;
    protected final @Nullable String lmbActionName;
    protected final @Nullable String rmbActionName;



    /**
     * Creates a new __base_ButtonElm.
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected __base_ButtonElm(final @Nullable String lmbActionName, final @Nullable String rmbActionName, final int clickCooldown) {
        this.lmbActionName = lmbActionName;
        this.rmbActionName = rmbActionName;
        this.clickCooldown = clickCooldown;
    }



    /**
     * Shared override of spawn from Elm
     */
    protected void spawn(final @NotNull Elm _this, final @Nullable Animation hoverPrimerAanimation) {
        clickRateLimiter.renewCooldown(INITIAL_COOLDOWN);
        if(hoverPrimerAanimation != null) {
            _this.applyAnimationNow(hoverPrimerAanimation);
        }
    }


    /**
     * Shared override of finalizeDespawn from Elm
     */
    protected void finalizeDespawn(final @NotNull Elm _this, final @Nullable Animation hoverPrimerAanimation) {
        if(hoverPrimerAanimation != null) {
            _this.applyAnimationNow(new Animation(hoverPrimerAanimation).invert());
        }
    }


    /**
     * Shared override of onHoverEnter from Hoverable
     */
    protected void onHoverEnter(final @NotNull Elm _this, final @Nullable Animation animation) {
        if(animation != null) {
            _this.applyAnimation(animation);
        }
    }


    /**
     * Shared override of onHoverTick from Hoverable
     */
    protected void onHoverTick(final @NotNull Elm _this) {

        // Update input displays if present
        if(_this.getCanvas() instanceof InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(lmbActionName);
            c.getRmbIndicator().updateDisplay(rmbActionName);
        }
    }


    /**
     * Shared override of onHoverExit from Hoverable
     */
    protected void onHoverExit(final @NotNull Elm _this, final @Nullable Animation animation) {

        // Start hover exit animation
        if(animation != null) {
            _this.applyAnimation(animation);
            _this.hoverRateLimiter.renewCooldown(animation.getTotalDuration());
        }

        // Update input displays if present
        if(_this.getCanvas() instanceof InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(null);
            c.getRmbIndicator().updateDisplay(null);
        }
    }


    /**
     * Shared override of attemptClick from Clickable
     */
    protected boolean attemptClick(final @NotNull Elm _this, final @NotNull Player player) {
        if(!clickRateLimiter.attempt()) return false;
        clickRateLimiter.renewCooldown(clickCooldown);
        return _this.checkIntersection(player);
    }


    /**
     * Shared override of onClick from Clickable
     */
    public void onClick(final @NotNull Elm _this) {
        //Empty
    }
}
