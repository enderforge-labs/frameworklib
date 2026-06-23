package com.snek.frameworklib.utils.scheduler;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;








/**
 * The base class of task handlers.
 * @since v1.1.0
 */
public class __base_TaskHandler {
    private long targetTick;
    private long taskIndex; //! The index of this task within the tick
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
     * Retrieves the index of this task within the tick it was scheduled for.
     * @return The index.
     */
    public long getTaskIndex() {
        return taskIndex;
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
     * Sets a new task index to this task.
     * @param taskIndex the new task index.
     */
    public void setTargetTaskIndex(final long taskIndex) {
        assert Require.nonNegative(taskIndex, "task index");
        this.taskIndex = taskIndex;
    }




    /**
     * Creates a new __base_TaskHandler.
     * @param targetTick The tick the task is scheduled for.
     * @param taskIndex The index of this task within its scheduled tick.
     * @param task The task to execute.
     */
    public __base_TaskHandler(final @NotNull Runnable task, final long targetTick, final long taskIndex) {
        assert Require.nonNull(task, "task");
        assert Require.nonNegative(targetTick, "target tick");
        assert Require.nonNegative(taskIndex, "task index");

        this.targetTick = targetTick;
        this.taskIndex = taskIndex;
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
