package com.snek.frameworklib.utils.scheduler;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;








/**
 * A class that lets you control scheduled loop tasks.
 * <p>
 * Instances of this class are returned by the Scheduler's methods.
 */
public class LoopTaskHandler extends __base_TaskHandler {
    private final long interval;
    public long getInterval() { return interval; }


    /**
     * Creates a new LoopTaskHandler.
     * @param task The task to execute.
     * @param targetTick The tick the first iteration of this task is scheduled for, measures in ticks.
     * @param interval The interval between iterations, measures in ticks. Must be {@code > 0}.
     */
    public LoopTaskHandler(final @NotNull Runnable task, final long targetTick, final long interval) {
        super(task, targetTick);
        assert Require.positive(interval, "interval");

        this.interval = interval;
    }
}
