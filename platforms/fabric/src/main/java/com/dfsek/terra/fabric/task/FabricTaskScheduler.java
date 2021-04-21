package com.dfsek.terra.fabric.task;

import com.dfsek.terra.api.task.TaskScheduler;
import net.minecraft.server.MinecraftServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FabricTaskScheduler implements TaskScheduler {
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-1);
    @Override
    public void runTaskAsynchronously(Runnable task, long ticks) {
        runTask(() -> executorService.execute(task), ticks);
    }

    @Override
    public void runTask(Runnable task, long ticks) {
        // TODO implementation
    }
}
