/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.bukkit;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;

import com.dfsek.terra.api.addon.BaseAddon;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.CancellationException;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.attribute.AttributeParents;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.bukkit.attribute.PaperDimensions;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.handles.BukkitItemHandle;
import com.dfsek.terra.bukkit.handles.BukkitWorldHandle;
import com.dfsek.terra.bukkit.util.ServerThread;
import com.dfsek.terra.bukkit.world.BiomeHandler;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.minecraft.attribute.MinecraftAttributeParents;


public class BukkitPlatform extends AbstractPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(BukkitPlatform.class);
    private static final long SERVER_THREAD_POLL_MILLIS = 50;

    private final ItemHandle itemHandle = new BukkitItemHandle();

    private final WorldHandle handle = new BukkitWorldHandle();

    private final MinecraftAttributeParents attributeParents = new MinecraftAttributeParents(getAttributeHandle());

    private final PaperDimensions dimensions = new PaperDimensions(getAttributeHandle(), attributeParents,
        () -> !Bukkit.getOnlinePlayers().isEmpty());

    private final TerraBukkitPlugin plugin;

    private int generationThreads;

    public BukkitPlatform(TerraBukkitPlugin plugin) {
        generationThreads = getMoonriseGenerationThreadsWithReflection();
        if(generationThreads == 0) {
            generationThreads = 1;
        }
        this.plugin = plugin;
        load();
    }

    public TerraBukkitPlugin getPlugin() {
        return plugin;
    }

    @Override
    public AttributeParents getAttributeParents() {
        return attributeParents;
    }

    public MinecraftAttributeParents attributeParents() {
        return attributeParents;
    }

    public PaperDimensions dimensions() {
        return dimensions;
    }

    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        attributeParents.beginPackReload();
        try {
            boolean succeed = loadConfigPacks();
            // Packs load off the server thread; registries, tags and generators change on it.
            ServerThread.run(this::applyReloadedPacks, task -> plugin.getGlobalRegionScheduler().execute(plugin, task),
                Bukkit::isGlobalTickThread, () -> Bukkit.isStopping() || !plugin.isEnabled(), SERVER_THREAD_POLL_MILLIS);
            if(succeed) {
                LOGGER.info("Packs reloaded; dimension types, generation settings, timelines and existing biomes need a restart.");
            }

            return succeed;
        } catch(CancellationException e) {
            LOGGER.warn("Packs not reloaded: the server stopped before they could be applied");
            return false;
        } finally {
            attributeParents.endPackReload();
        }
    }

    private void applyReloadedPacks() {
        BiomeHandler.registerBiomes(this);
        BiomeHandler.addToVanillaTags(this);

        Bukkit.getWorlds().forEach(world -> {
            if(world.getGenerator() instanceof BukkitChunkGeneratorWrapper wrapper) {
                getConfigRegistry().get(wrapper.getPack().getRegistryKey()).ifPresent(pack -> {
                    wrapper.setPack(pack);
                    LOGGER.info("Replaced pack in chunk generator for world {}", world);
                });
            }
        });
    }

    @Override
    public @NotNull String platformName() {
        return "Bukkit";
    }

    @Override
    protected Iterable<BaseAddon> platformAddon() {
        return List.of(new BukkitAddon(this));
    }

    @Override
    public void runPossiblyUnsafeTask(@NotNull Runnable runnable) {
        plugin.getGlobalRegionScheduler().run(plugin, task -> runnable.run());
    }

    @Override
    public @NotNull WorldHandle getWorldHandle() {
        return handle;
    }

    @Override
    public @NotNull File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public @NotNull ItemHandle getItemHandle() {
        return itemHandle;
    }

    @Override
    public int getGenerationThreads() {
        return generationThreads;
    }

    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry.registerLoader(BlockState.class, (type, o, loader, depthTracker) -> handle.createBlockState((String) o))
            .registerLoader(PlatformBiome.class, (type, o, loader, depthTracker) -> parseBiome((String) o, depthTracker));
    }

    protected BukkitPlatformBiome parseBiome(String id, DepthTracker depthTracker) throws LoadException {
        NamespacedKey key = NamespacedKey.fromString(id);
        if(key == null || !key.namespace().equals("minecraft")) throw new LoadException("Invalid biome identifier " + id, depthTracker);
        org.bukkit.block.Biome biome = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).get(key);
        if(biome == null) throw new LoadException("No such biome " + id, depthTracker);
        return new BukkitPlatformBiome(biome);
    }
}
