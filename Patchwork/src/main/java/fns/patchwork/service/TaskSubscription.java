/*
 * This file is part of Freedom-Network-Suite - https://github.com/AtlasMediaGroup/Freedom-Network-Suite
 * Copyright (C) 2023 Total Freedom Server Network and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fns.patchwork.service;

import fns.patchwork.utils.container.Pair;
import java.util.concurrent.Executor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

/**
 * Represents a subscription to a task. Task subscriptions offer a nice wrapper for managing tasks, which are inevitably
 * just bukkit runnables with a bit more lenience in terms of instantiation modification and execution. It also offers a
 * more intuitive way to manage our tasks; rather than having to keep track of task ids for each {@link BukkitTask}
 * object that gets returned by the {@link BukkitScheduler}.
 *
 * @param <T> The type of task.
 */
public final class TaskSubscription<T extends Task>
{
    /**
     * The task that is being subscribed to.
     */
    private final T task;
    /**
     * The task id of the task.
     */
    private final int taskId;
    /**
     * True if the task is async, false otherwise.
     */
    private final boolean async;
    /**
     * The executor that will execute the task.
     */
    private final Executor executor;

    /**
     * True if the task is active, false otherwise. By default, this is set to false, and will be marked as true when
     * the task is started.
     */
    private boolean isActive = false;

    /**
     * Creates a new task subscription.
     *
     * @param plugin The plugin which owns the task.
     * @param task   The task that is being subscribed to.
     * @param async  True if the task is async, false otherwise.
     */
    TaskSubscription(final JavaPlugin plugin, final T task, final boolean async)
    {
        this.task = task;
        this.async = async;

        final long delay = (task.isDelayed()
                ? task.getDelay()
                : 0);
        final long period = (task.isRepeating()
                ? task.getInterval()
                : 0);

        final Pair<Integer, Executor> integerExecutorPair = async
                ? getAsync(plugin, delay, period)
                : getSync(plugin, delay, period);

        this.executor = integerExecutorPair.value();
        this.taskId = integerExecutorPair.key();
    }

    /**
     * Gets the executor and task id for an async task, wrapped in a {@link Pair}&lt;{@link Integer},
     * {@link Executor}&gt;.
     * <br>
     * This will return a Pair where {@link Pair#value()} is an asynchronous executor.
     *
     * @param plugin The plugin which owns the task.
     * @param delay  The delay of the task.
     * @param period The period of the task.
     * @return The executor and task id for an asynchronous task.
     */
    private Pair<Integer, Executor> getAsync(final JavaPlugin plugin, final long delay, final long period)
    {
        final Executor executor1;
        final int[] tempId = new int[1];
        if (period != 0)
        {
            executor1 = r ->
            {
                final BukkitTask task1 = Bukkit.getScheduler()
                                               .runTaskTimerAsynchronously(plugin, r, delay, period);
                tempId[0] = task1.getTaskId();
            };
        } else if (delay != 0)
        {
            executor1 = r ->
            {
                final BukkitTask task1 = Bukkit.getScheduler()
                                               .runTaskLaterAsynchronously(plugin, r, delay);
                tempId[0] = task1.getTaskId();
            };
        } else
        {
            executor1 = r ->
            {
                final BukkitTask task1 = Bukkit.getScheduler()
                                               .runTaskAsynchronously(plugin, r);
                tempId[0] = task1.getTaskId();
            };
        }

        return new Pair<>(tempId[0], executor1);
    }

    /**
     * Gets the executor and task id for a sync task, wrapped in a {@link Pair}&lt;{@link Integer},
     * {@link Executor}&gt;.
     * <br>
     * This will return a Pair where {@link Pair#value()} is a synchronous executor.
     *
     * @param plugin The plugin which owns the task.
     * @param delay  The delay of the task.
     * @param period The period of the task.
     * @return The executor and task id for a synchronous task.
     */
    private Pair<Integer, Executor> getSync(final JavaPlugin plugin, final long delay, final long period)
    {
        final Executor executor1;
        final int[] tempId = new int[1];

        if (period != 0)
        {
            executor1 = r ->
            {
                final BukkitTask task1 = Bukkit.getScheduler()
                                               .runTaskTimer(plugin, r, delay, period);
                tempId[0] = task1.getTaskId();
            };
        } else if (delay != 0)
        {
            executor1 = r ->
            {
                final BukkitTask task1 = Bukkit.getScheduler()
                                               .runTaskLater(plugin, r, delay);
                tempId[0] = task1.getTaskId();
            };
        } else
        {
            executor1 = r ->
            {
                final BukkitTask task1 = Bukkit.getScheduler()
                                               .runTask(plugin, r);
                tempId[0] = task1.getTaskId();
            };
        }

        return new Pair<>(tempId[0], executor1);
    }

    /**
     * Starts the task.
     */
    public void start()
    {
        this.isActive = true;
        executor.execute(task);
    }

    /**
     * Stops the task.
     */
    public void stop()
    {
        this.isActive = false;
        Bukkit.getScheduler()
              .cancelTask(this.getTaskId());
    }

    /**
     * @return The task id of the task.
     */
    public int getTaskId()
    {
        return taskId;
    }

    /**
     * @return The task that is being subscribed to.
     */
    public T getTask()
    {
        return task;
    }

    /**
     * @return True if the task is async, false otherwise.
     */
    public boolean isAsync()
    {
        return async;
    }

    /**
     * @return The executor that will execute the task.
     */
    public Executor getExecutor()
    {
        return executor;
    }

    /**
     * @return True if the task is active, false otherwise.
     */
    public boolean isActive()
    {
        return isActive;
    }
}
