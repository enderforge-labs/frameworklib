package com.snek.frameworklib.utils.scheduler;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;








/**
 * A class that lets you control scheduled tasks.
 * <p>
 * Instances of this class are returned by the Scheduler's methods.
 */
public class TaskHandler {
    private long targetTick;
    protected final @NotNull Runnable task;
    protected boolean cancelled = false;


    /**
     * Retrieves the tick this task was scheduled for.
     * @return The tick number.
     */
    public long getTargetTick() {
        return targetTick;
    }

    /**
     * Sets a new target tick for this task.
     * @param targetTick the new targe tick.
     */
    public void setTargetTick(final long targetTick) {
        assert Require.nonNegative(targetTick, "target tick");
        this.targetTick = targetTick;
    }




    /**
     * Creates a new TaskHandler.
     * @param targetTick The tick the task is scheduled for.
     * @param task The task to execute.
     */
    public TaskHandler(final @NotNull Runnable task, final long targetTick) {
        assert Require.nonNull(task, "task");
        assert Require.nonNegative(targetTick, "target tick");

        this.targetTick = targetTick;
        this.task = task;
    }


    /**
     * Marks the task as cancelled.
     * <p>
     * Calling .exec() on cancelled tasks doesn't run them.
     */
    public void cancel() {
        cancelled = true;
    }


    /**
     * Marks the task as scheduled.
     * <p>
     * This undos any previous calls to .cancel()
     */
    public void schedule() {
        cancelled = false;
    }


    /**
     * Immediately runs the task if it hasn't been cancelled.
     */
    public void compute() {
        if(!cancelled) task.run();
    }
}
