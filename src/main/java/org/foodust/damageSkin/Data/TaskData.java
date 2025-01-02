package org.foodust.damageSkin.Data;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class TaskData {
    public static List<BukkitTask> TASKS = new ArrayList<>();
    public static void release(){
        TASKS.forEach(bukkitTask -> {
            Bukkit.getScheduler().cancelTask(bukkitTask.getTaskId());
        });
    }
}
