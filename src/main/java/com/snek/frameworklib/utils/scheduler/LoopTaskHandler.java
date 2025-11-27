package com.snek.frameworklib.utils.scheduler;

import org.jetbrains.annotations.NotNull;








/**
 * A class that lets you control scheduled loop tasks.
 * <p>
 * Instances of this class are returned by the Scheduler's methods.
 */
public class LoopTaskHandler extends TaskHandler {
    private final long interval;
    public long getInterval() { return interval; }


    /**
     * Creates a new LoopTaskHandler.
     * @param targetTick The tick the first iteration of this task is scheduled for, measures in ticks.
     * @param interval The interval between iterations, measures in ticks.
     * @param task The task to execute.
     */
    public LoopTaskHandler(final long targetTick, final long interval, final @NotNull Runnable task) {
        super(targetTick, task);
        this.interval = interval;
    }
}
