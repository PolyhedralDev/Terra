package com.dfsek.terra.registry.master;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.registry.key.RegistryKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ConfigRegistryConcurrencyTest {
    private static final int ITERATIONS = 200;
    private static final int THREADS = 16;

    @Test
    void concurrentDuplicatePacks() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        List<String> failures = new ArrayList<>();
        try {
            for(int iteration = 0; iteration < ITERATIONS; iteration++) {
                ConfigRegistry registry = new ConfigRegistry();
                RegistryKey key = RegistryKey.of("terra", "PACK");
                List<ConfigPack> packs = new ArrayList<>();
                for(int thread = 0; thread < THREADS; thread++) packs.add(pack());
                List<Object> results = race(executor, THREADS, thread -> registry.registerChecked(key, packs.get(thread)));

                List<ConfigPack> winners = new ArrayList<>();
                int duplicates = 0;
                for(int thread = 0; thread < THREADS; thread++) {
                    Object result = results.get(thread);
                    if(result == null) {
                        winners.add(packs.get(thread));
                    } else if(result instanceof DuplicateEntryException e) {
                        assertEquals("Value with identifier \"terra:PACK\" is already defined in registry.", e.getMessage());
                        duplicates++;
                    } else {
                        throw new AssertionError("unexpected failure", (Throwable) result);
                    }
                }
                if(winners.size() != 1 || duplicates != THREADS - 1) {
                    failures.add("iteration " + iteration + ": " + winners.size() + " registered, " + duplicates + " duplicates");
                    continue;
                }
                assertSame(winners.getFirst(), registry.get(key).orElseThrow(), "iteration " + iteration);
                assertSame(winners.getFirst(), registry.getByID("PACK").orElseThrow(), "iteration " + iteration);
                assertEquals(1, registry.getMatches("PACK").size(), "iteration " + iteration);
            }
        } finally {
            executor.shutdownNow();
        }
        assertTrue(failures.isEmpty(), failures.size() + "/" + ITERATIONS + " iterations raced: " + failures);
    }

    @Test
    void concurrentPacksSharingId() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        List<String> failures = new ArrayList<>();
        try {
            for(int iteration = 0; iteration < ITERATIONS; iteration++) {
                ConfigRegistry registry = new ConfigRegistry();
                List<ConfigPack> packs = new ArrayList<>();
                for(int thread = 0; thread < THREADS; thread++) packs.add(pack());
                List<Object> results = race(executor, THREADS,
                    thread -> registry.registerChecked(RegistryKey.of("ns" + thread, "PACK"), packs.get(thread)));
                for(Object result : results) {
                    if(result != null) throw new AssertionError("unexpected failure", (Throwable) result);
                }

                Map<RegistryKey, ConfigPack> matches = registry.getMatches("PACK");
                boolean complete = matches.size() == THREADS;
                for(int thread = 0; thread < THREADS && complete; thread++) {
                    complete = matches.get(RegistryKey.of("ns" + thread, "PACK")) == packs.get(thread);
                }
                if(!complete) failures.add("iteration " + iteration + ": " + matches.size() + "/" + THREADS + " found by ID");
                assertEquals(THREADS, registry.keys().size(), "iteration " + iteration);
            }
        } finally {
            executor.shutdownNow();
        }
        assertTrue(failures.isEmpty(), failures.size() + "/" + ITERATIONS + " iterations lost entries: " + failures);
    }

    private static List<Object> race(ExecutorService executor, int threads, ThreadAction action) throws Exception {
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch gate = new CountDownLatch(1);
        List<Future<Object>> futures = new ArrayList<>();
        for(int thread = 0; thread < threads; thread++) {
            int id = thread;
            futures.add(executor.submit(() -> {
                ready.countDown();
                gate.await();
                try {
                    action.run(id);
                    return null;
                } catch(RuntimeException e) {
                    return e;
                }
            }));
        }
        ready.await();
        gate.countDown();
        List<Object> results = new ArrayList<>();
        for(Future<Object> future : futures) {
            try {
                results.add(future.get(30, TimeUnit.SECONDS));
            } catch(ExecutionException e) {
                throw new AssertionError(e.getCause());
            }
        }
        return results;
    }

    private static ConfigPack pack() {
        return (ConfigPack) Proxy.newProxyInstance(ConfigRegistryConcurrencyTest.class.getClassLoader(), new Class<?>[]{ ConfigPack.class },
            (proxy, method, args) -> switch(method.getName()) {
                case "hashCode" -> System.identityHashCode(proxy);
                case "equals" -> proxy == args[0];
                case "toString" -> "pack@" + Integer.toHexString(System.identityHashCode(proxy));
                default -> null;
            });
    }

    @FunctionalInterface
    private interface ThreadAction {
        void run(int thread);
    }
}
