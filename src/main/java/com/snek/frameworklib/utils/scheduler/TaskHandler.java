package com.snek.frameworklib.utils.scheduler;

import org.jetbrains.annotations.NotNull;








/**
 * A class that lets you control scheduled tasks.
 * <p>
 * Instances of this class are returned by the Scheduler's methods.
 * @since v1.1.0
 */
public class TaskHandler extends __base_TaskHandler {
    private boolean complete = false;



    /**
     * Checks if the task was completed.
     * @return Whether the task has been completed.
     */
    public boolean isComplete() {
        return this.complete;
    }


    /**
     * Creates a new TaskHandler.
     * @param targetTick The tick the task is scheduled for.
     * @param taskIndex The index of this task within its scheduled tick.
     * @param task The task to execute.
     */
    public TaskHandler(final @NotNull Runnable task, final long targetTick, final long taskIndex) {
        super(task, targetTick, taskIndex);
    }


    @Override
    public void compute() {
        if(!isCancelled() && !complete) {
            super.compute();
            complete = true;
        }
    }
}
