package com.snek.frameworklib.utils.scheduler;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.utils.UtilityClassBase;








/**
 * A utility class that can store tasks and execute them after a specified number of server ticks.
 * <p>
 * This scheduler also supports looping and cancelling tasks.
 * @since v1.1.0
 */
public final class Scheduler extends UtilityClassBase {

    // ! Outer queue orders by target tick, but doesn't guarantee an order between entries with the same priority.
    // ! This is solved by adding an additional index value to the ordering data.

    // ! For ordering, the index uses the 31 least significan bits, while the tick value uses the remaining 32 (1 is sign).
    // ! This leaves a max of ~2bil tasks per tick and a max uninterrupted uptime for the server of ~6.8 years.

    private static long tickNum = 0;
    private static long tickTaskIndex = 0;
    private static final @NotNull PriorityQueue<@NotNull __base_TaskHandler> taskQueue = new PriorityQueue<>(
        Comparator.comparingLong(e -> (e.getTargetTick() << 31) | e.getTaskIndex())
    );


    /**
     * Returns the current tick number.
     * @return The tick number.
     */
    public static long getTickNum() {
        return tickNum;
    }




    /*
     * The tick function of the scheduler.
     * <p>
     * Must be called exactly one time at the end of every server tick.
     */
    public static void tick() {

        // For each task that has to be executed
        while(taskQueue.peek().getTargetTick() <= tickNum) {

            // Execute it
            final __base_TaskHandler handler = taskQueue.poll();
            handler.compute();

            // Renew task handler if it's a LoopTaskHadler and has not been cancelled
            if(handler instanceof final LoopTaskHandler h && !h.isCancelled()) {
                h.setTargetTick(tickNum + h.getInterval());
                h.setTargetTaskIndex(tickTaskIndex++);
                taskQueue.add(h);
            }
        }

        tickNum++;
        tickTaskIndex = 0;
    }




    /**
     * Runs a task on the main thread every <interval> ticks after a specified delay.
     * @param delay The initial delay, expressed in server ticks. Must be {@code > 0}.
     * @param interval The time interval between calls, expressed in server ticks.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static @NotNull LoopTaskHandler loop(final long delay, final long interval, final @NotNull Runnable task) {
        assert Require.nonNegative(delay, "delay");
        assert Require.positive(interval, "interval");
        assert Require.nonNull(task, "task");

        final @NotNull LoopTaskHandler handler = new LoopTaskHandler(task, tickNum + delay, tickTaskIndex, interval);
        taskQueue.add(handler);
        ++tickTaskIndex;
        return handler;
    }




    /**
     * Runs a task on the main thread after a specified delay.
     * @param delay The delay, expressed in server ticks.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static @NotNull TaskHandler schedule(final long delay, final @NotNull Runnable task) {
        assert Require.nonNull(task, "task");
        assert Require.nonNegative(delay, "delay");

        final @NotNull TaskHandler handler = new TaskHandler(task, tickNum + delay, tickTaskIndex);
        taskQueue.add(handler);
        ++tickTaskIndex;
        return handler;
    }




    /**
     * Runs a task on the main thread at the end of the current tick. Equivalent to calling TaskHandler.schedule with delay 0.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static @NotNull TaskHandler run(final @NotNull Runnable task) {
        assert Require.nonNull(task, "task");
        return schedule(0, task);
    }
}
