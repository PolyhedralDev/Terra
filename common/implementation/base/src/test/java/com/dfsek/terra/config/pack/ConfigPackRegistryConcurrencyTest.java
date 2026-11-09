package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.api.loader.AbstractConfigLoader;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.ShortcutHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;


class ConfigPackRegistryConcurrencyTest {
    private static final int ITERATIONS = 60;
    private static final int THREADS = 16;
    private static final int TYPES = 96;
    private static final int SHORTCUT_TYPES = 8;

    @TempDir
    Path packDir;

    @Test
    void concurrentRegistryCreation() throws Exception {
        Files.writeString(packDir.resolve("pack.yml"), "id: TEST\n");
        List<TypeKey<Object>> keys = typeKeys();
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        try {
            for(int iteration = 0; iteration < ITERATIONS; iteration++) {
                hammer(newPack(), keys, executor, iteration);
            }
        } finally {
            executor.shutdownNow();
        }
    }

    private void hammer(ConfigPackImpl pack, List<TypeKey<Object>> keys, ExecutorService executor, int iteration) throws Exception {
        CountDownLatch gate = new CountDownLatch(1);
        List<Future<List<CheckedRegistry<Object>>>> futures = new ArrayList<>();
        for(int thread = 0; thread < THREADS; thread++) {
            int id = thread;
            futures.add(executor.submit(() -> {
                List<Integer> order = new ArrayList<>();
                for(int i = 0; i < TYPES; i++) order.add(i);
                Collections.shuffle(order, new Random(iteration * 31L + id));
                List<CheckedRegistry<Object>> seen = new ArrayList<>(Collections.nCopies(TYPES, null));
                gate.await();
                for(int i : order) {
                    TypeKey<Object> key = keys.get(i);
                    if(i < SHORTCUT_TYPES) pack.registerShortcut(key, "T" + id, (loader, input, tracker) -> input);
                    seen.set(i, pack.getOrCreateRegistry(key));
                }
                return seen;
            }));
        }
        gate.countDown();

        List<List<CheckedRegistry<Object>>> results = new ArrayList<>();
        for(Future<List<CheckedRegistry<Object>>> future : futures) {
            try {
                results.add(future.get(30, TimeUnit.SECONDS));
            } catch(java.util.concurrent.ExecutionException e) {
                fail("Iteration " + iteration + ": concurrent registry creation threw", e.getCause());
            }
        }

        Map<Type, TypeLoader<?>> selfLoaders = loaders(field(pack, "selfLoader", ConfigLoader.class));
        Map<Type, TypeLoader<?>> abstractLoaders = loaders(
            field(field(pack, "abstractConfigLoader", AbstractConfigLoader.class), "delegate", ConfigLoader.class));

        for(int i = 0; i < TYPES; i++) {
            Type type = keys.get(i).getType();
            CheckedRegistry<Object> registry = pack.getRegistry(type);
            assertNotNull(registry, "Iteration " + iteration + ": registry lost for " + type.getTypeName());
            for(List<CheckedRegistry<Object>> seen : results) {
                assertSame(registry, seen.get(i), "Iteration " + iteration + ": registry created twice for " + type.getTypeName());
            }
            TypeLoader<?> selfLoader = selfLoaders.get(type);
            if(i < SHORTCUT_TYPES) {
                ShortcutHolder<?> holder = assertInstanceOf(ShortcutHolder.class, selfLoader,
                    "Iteration " + iteration + ": shortcut loader missing for " + type.getTypeName());
                assertSame(holder, abstractLoaders.get(type));
                Map<?, ?> shortcuts = field(holder, "shortcuts", Map.class);
                assertEquals(THREADS, shortcuts.size(), "Iteration " + iteration + ": shortcuts lost for " + type.getTypeName());
            } else {
                Object open = ((CheckedRegistryImpl<Object>) registry).getRegistry();
                assertSame(open, selfLoader, "Iteration " + iteration + ": self loader not the registry for " + type.getTypeName());
                assertSame(open, abstractLoaders.get(type),
                    "Iteration " + iteration + ": abstract loader not the registry for " + type.getTypeName());
            }
        }
    }

    private ConfigPackImpl newPack() {
        ConfigPackImpl[] captured = new ConfigPackImpl[1];
        EventManager events = (EventManager) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ EventManager.class },
            (proxy, method, args) -> {
                if(method.getName().equals("callEvent")) {
                    if(args[0] instanceof ConfigPackPreLoadEvent preLoad) {
                        captured[0] = (ConfigPackImpl) preLoad.getPack();
                        throw new StopLoad();
                    }
                    return args[0];
                }
                return null;
            });
        Platform platform = (Platform) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{ Platform.class },
            (proxy, method, args) -> method.getName().equals("getEventManager") ? events : null);
        try {
            new ConfigPackImpl(packDir, platform);
            fail("pack load should stop at pre-load");
        } catch(StopLoad expected) {
        } catch(Exception e) {
            throw new AssertionError(e);
        }
        return captured[0];
    }

    @SuppressWarnings("unchecked")
    private static List<TypeKey<Object>> typeKeys() throws ClassNotFoundException {
        List<TypeKey<Object>> keys = new ArrayList<>();
        String[] components = { "Ljava.lang.Object;", "Ljava.lang.String;", "I", "J" };
        for(int depth = 1; keys.size() < TYPES; depth++) {
            for(String component : components) {
                if(keys.size() < TYPES) keys.add(TypeKey.of((Class<Object>) Class.forName("[".repeat(depth) + component)));
            }
        }
        return keys;
    }

    @SuppressWarnings("unchecked")
    private static Map<Type, TypeLoader<?>> loaders(ConfigLoader loader) throws ReflectiveOperationException {
        return field(loader, "loaders", Map.class);
    }

    private static <T> T field(Object owner, String name, Class<T> type) throws ReflectiveOperationException {
        Field field = owner.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return type.cast(field.get(owner));
    }

    private static final class StopLoad extends RuntimeException {
        StopLoad() {
            super(null, null, false, false);
        }
    }
}
