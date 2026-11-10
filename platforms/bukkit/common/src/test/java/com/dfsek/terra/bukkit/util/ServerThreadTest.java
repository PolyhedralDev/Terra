package com.dfsek.terra.bukkit.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Timeout(10)
class ServerThreadTest {
    private final ExecutorService server = Executors.newSingleThreadExecutor(r -> new Thread(r, "server"));

    @AfterEach
    void stopServer() {
        server.shutdownNow();
    }

    @Test
    void runsOnServerThread() {
        AtomicReference<String> ranOn = new AtomicReference<>();

        ServerThread.run(() -> ranOn.set(Thread.currentThread().getName()), server, () -> false, () -> false, 10);

        assertEquals("server", ranOn.get());
    }

    @Test
    void runsDirectlyOnServerThread() {
        List<String> ran = new ArrayList<>();

        ServerThread.run(() -> ran.add(Thread.currentThread().getName()), task -> {
            throw new AssertionError("scheduled from the server thread");
        }, () -> true, () -> false, 10);

        assertEquals(List.of(Thread.currentThread().getName()), ran);
    }

    @Test
    void rethrowsTaskException() {
        IllegalStateException boom = new IllegalStateException("boom");

        assertSame(boom, assertThrows(IllegalStateException.class, () -> ServerThread.run(() -> {
            throw boom;
        }, server, () -> false, () -> false, 10)));
    }

    @Test
    void dropsPendingTaskOnStop() {
        List<Runnable> scheduled = new ArrayList<>();
        AtomicBoolean ran = new AtomicBoolean();

        assertThrows(CancellationException.class, () -> ServerThread.run(() -> ran.set(true), scheduled::add, () -> false, () -> true,
            10));
        scheduled.forEach(Runnable::run);

        assertFalse(ran.get());
    }

    @Test
    void waitsForStartedTaskOnStop() throws InterruptedException {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        AtomicBoolean finished = new AtomicBoolean();
        AtomicBoolean stopping = new AtomicBoolean();
        Thread caller = new Thread(() -> ServerThread.run(() -> {
            started.countDown();
            try {
                release.await();
            } catch(InterruptedException e) {
                throw new IllegalStateException(e);
            }
            finished.set(true);
        }, server, () -> false, stopping::get, 10));
        caller.start();

        assertTrue(started.await(5, TimeUnit.SECONDS));
        stopping.set(true);
        caller.interrupt();
        Thread.sleep(50);
        assertTrue(caller.isAlive(), "the caller waits for the running task");
        release.countDown();
        caller.join();

        assertTrue(finished.get());
    }
}
