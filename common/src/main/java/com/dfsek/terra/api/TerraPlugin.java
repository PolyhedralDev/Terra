package com.dfsek.terra.api;

import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.task.TaskScheduler;
import com.dfsek.terra.api.util.JarUtil;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.api.util.logging.Logger;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.world.TerraWorld;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarFile;

/**
 * Represents a Terra mod/plugin instance.
 */
public interface TerraPlugin extends LoaderRegistrar {
    WorldHandle getWorldHandle();

    TerraWorld getWorld(World world);

    Logger logger();

    PluginConfig getTerraConfig();

    File getDataFolder();

    boolean isDebug();

    Language getLanguage();

    CheckedRegistry<ConfigPack> getConfigRegistry();

    LockedRegistry<TerraAddon> getAddons();

    boolean reload();

    ItemHandle getItemHandle();

    void saveDefaultConfig();

    String platformName();

    DebugLogger getDebugLogger();

    EventManager getEventManager();

    default String getVersion() {
        return "@VERSION@";
    }

    /**
     * Runs a task that may or may not be thread safe, depending on platform.
     * <p>
     * Allows platforms to define what code is safe to be run asynchronously.
     *
     * @param task Task to be run.
     */
    default void runPossiblyUnsafeTask(Runnable task) {
        task.run();
    }

    Profiler getProfiler();

    default JarFile getModJar() throws URISyntaxException, IOException {
        return JarUtil.getJarFile();
    }

    TaskScheduler getScheduler();
}
