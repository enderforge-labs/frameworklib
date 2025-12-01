package com.snek.frameworklib.graphics.functional.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.graphics.core.HudCanvas;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;








/**
 * This acts as the base class of button elements, even though it is never inherited from.
 * This is because Java doesn't allow multiple inheritance.
 */
public final class __base_ButtonElm {
    public static final int INITIAL_COOLDOWN = 10;

    // Data
    protected final RateLimiter clickRateLimiter       = new RateLimiter();
    protected final RateLimiter initialCooldownLimiter = new RateLimiter();
    private   final int clickCooldown;



    /**
     * Creates a new __base_ButtonElm.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected __base_ButtonElm(final int clickCooldown) {
        this.clickCooldown = clickCooldown;
    }



    /**
     * Shared override of spawn from Elm
     */
    protected void spawn(final @NotNull Elm _this, final @Nullable Animation animation) {
        initialCooldownLimiter.renewCooldown(INITIAL_COOLDOWN);
        if(animation != null) {
            _this.applyAnimationNow(animation);
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
        // Empty
    }


    /**
     * Shared override of onHoverExit from Hoverable
     */
    protected void onHoverExit(final @NotNull Elm _this, final @Nullable Animation animation) {
        if(animation != null) {
            _this.applyAnimation(animation);
            _this.hoverRateLimiter.renewCooldown(animation.getTotalDuration());
        }
    }


    /**
     * Shared override of attemptClick from Clickable
     */
    protected boolean attemptClick(final @NotNull Elm _this, final @NotNull Player player) {
        if(!initialCooldownLimiter.attempt()) return false;
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


    /**
     * Plays the button click sound to the specified player.
     * @param player The player to play the sound to.
     */
    public static void playButtonSound(final @NotNull Player player) {
        MinecraftUtils.playSoundClient(player, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, 2, 1.5f);
    }
}
