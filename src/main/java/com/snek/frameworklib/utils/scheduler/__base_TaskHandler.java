package com.snek.frameworklib.utils.scheduler;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;








/**
 * The base class of task handlers.
 */
public class __base_TaskHandler {
    private long targetTick;
    private final @NotNull Runnable task;
    private boolean cancelled = false;

    /**
     * Checks if the task was cancelled.
     * @return Whether the task has been cancelled.
     */
    public boolean isCancelled() {
        return this.cancelled;
    }




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
     * Creates a new __base_TaskHandler.
     * @param targetTick The tick the task is scheduled for.
     * @param task The task to execute.
     */
    public __base_TaskHandler(final @NotNull Runnable task, final long targetTick) {
        assert Require.nonNull(task, "task");
        assert Require.nonNegative(targetTick, "target tick");

        this.targetTick = targetTick;
        this.task = task;
    }


    /**
     * Marks the task as cancelled.
     * <p>
     * Calling .exec() on cancelled tasks doesn't run them.
     * <p>
     * This method doesn't affect the task's completion state.
     * Tasks that were cancelled but never executed are considered not completed.
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
     * <p>
     * This flags the task as completed (unless this is a LoopTaskHandler, which doesn't support completion tracking).
     * <p>
     * Completed tasks cannot be ran again.
     */
    public void compute() {
        if(!cancelled) {
            task.run();
        }
    }
}
