package org.foodust.damageSkin.BaseModule;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.Data.TaskData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TaskModule {
    private final DamageSkin plugin;

    public BukkitTask runBukkitTask(Runnable task) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTask(plugin, task);
        TaskData.TASKS.add(bukkitTask);
        return bukkitTask;
    }

    public BukkitTask runBukkitTaskLater(Runnable task, Long delay) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        TaskData.TASKS.add(bukkitTask);
        return bukkitTask;
    }

    public BukkitTask runBukkitTaskLater(Runnable task, double delay) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, task, (long) delay);
        TaskData.TASKS.add(bukkitTask);
        return bukkitTask;
    }

    public BukkitTask runBukkitTaskLater(Runnable task, float delay) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, task, (long) delay);
        TaskData.TASKS.add(bukkitTask);
        return bukkitTask;
    }

    public BukkitTask runBukkitTaskTimer(Runnable task, Long delay, Long tick) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, task, delay, tick);
        TaskData.TASKS.add(bukkitTask);
        return bukkitTask;
    }

    public void cancelBukkitTask(BukkitTask bukkitTask) {
        if (bukkitTask != null)
            Bukkit.getScheduler().cancelTask(bukkitTask.getTaskId());
    }

    public void runBukkitTaskLater(Runnable task, Long delay, TimeUnit timeUnit) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(task, 0, delay, timeUnit);
    }
}
