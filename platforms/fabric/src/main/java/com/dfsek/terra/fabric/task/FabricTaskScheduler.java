package com.dfsek.terra.fabric.task;

import com.dfsek.terra.api.task.TaskScheduler;
import com.dfsek.terra.fabric.FabricUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.util.Util;

public class FabricTaskScheduler implements TaskScheduler {
    @Override
    public void runTaskAsynchronously(Runnable task, long ticks) {
        runTask(() -> Util.getMainWorkerExecutor().execute(task), ticks);
    }

    @Override
    public void runTaskAsynchronously(Runnable task) {
        Util.getMainWorkerExecutor().execute(task);
    }

    @Override
    public void runTask(Runnable task, long ticks) {
        MinecraftServer server = FabricUtil.getServer();
        server.send(new ServerTask(server.getTicks() + (int) ticks, task));
    }
}
