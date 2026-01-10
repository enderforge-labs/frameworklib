package com.snek.frameworklib.utils.scheduler;

import com.snek.frameworklib.debug.Require;

/**
 * An object that can be used to add a variable tick cooldown to arbitrary code.
 */
public class RateLimiter {
    private long cooldownEnd;


    /**
     * Creates a new RateLimiter.
     */
    public RateLimiter() {
        cooldownEnd = 0;
    }


    /**
     * Sets the remaining cooldown time since the current tick.
     * <p>
     * If the previous remaining cooldown is shorter than the new one, the call will have no effect.
     * @param cooldown The cooldown, expressed in ticks. Must be {@code >= 0}.
     */
    public void renewCooldown(final long cooldown) {
        assert Require.nonNegative(cooldown, "cooldown");

        final long cooldownEndNew = Scheduler.getTickNum() + cooldown;
        if(cooldownEnd < cooldownEndNew) {
            cooldownEnd = cooldownEndNew;
        }
    }


    /**
     * Checks whether the cooldown has expired.
     * @return True if the cooldown has expired, false otherwise.
     */
    public boolean attempt() {
        return Scheduler.getTickNum() >= cooldownEnd;
    }
}
