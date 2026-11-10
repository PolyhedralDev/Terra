package com.dfsek.terra.bukkit.util;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;


public final class ServerThread {
    private static final int PENDING = 0;
    private static final int RUNNING = 1;
    private static final int ABANDONED = 2;

    private ServerThread() { }

    /**
     * The caller must not hold a lock the server thread could wait on.
     */
    public static void run(Runnable task, Executor serverThread, BooleanSupplier onServerThread, BooleanSupplier stopping,
                           long pollMillis) {
        if(onServerThread.getAsBoolean()) {
            task.run();
            return;
        }
        AtomicInteger state = new AtomicInteger(PENDING);
        CompletableFuture<Void> done = new CompletableFuture<>();
        serverThread.execute(() -> {
            if(!state.compareAndSet(PENDING, RUNNING)) return;
            try {
                task.run();
                done.complete(null);
            } catch(Throwable t) {
                done.completeExceptionally(t);
            }
        });

        boolean interrupted = false;
        try {
            while(true) {
                try {
                    done.get(pollMillis, TimeUnit.MILLISECONDS);
                    return;
                } catch(TimeoutException e) {
                    if(stopping.getAsBoolean() && state.compareAndSet(PENDING, ABANDONED)) {
                        throw new CancellationException("the server stopped before the task ran");
                    }
                } catch(InterruptedException e) {
                    interrupted = true;
                    if(state.compareAndSet(PENDING, ABANDONED)) throw new CancellationException("interrupted before the task ran");
                } catch(ExecutionException e) {
                    switch(e.getCause()) {
                        case RuntimeException runtime -> throw runtime;
                        case Error error -> throw error;
                        default -> throw new CompletionException(e.getCause());
                    }
                }
            }
        } finally {
            if(interrupted) Thread.currentThread().interrupt();
        }
    }
}
