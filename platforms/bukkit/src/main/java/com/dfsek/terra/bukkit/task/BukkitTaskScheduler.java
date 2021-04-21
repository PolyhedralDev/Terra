package com.dfsek.terra.bukkit.task;

import com.dfsek.terra.api.task.TaskScheduler;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;
import org.bukkit.Bukkit;

public class BukkitTaskScheduler implements TaskScheduler {
    private final TerraBukkitPlugin plugin;

    public BukkitTaskScheduler(TerraBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runTaskAsynchronously(Runnable task, long ticks) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, ticks);
    }

    @Override
    public void runTask(Runnable task, long ticks) {
        Bukkit.getScheduler().runTaskLater(plugin, task, ticks);
    }

    @Override
    public void runTask(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }

    @Override
    public void runTaskAsynchronously(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }
}
