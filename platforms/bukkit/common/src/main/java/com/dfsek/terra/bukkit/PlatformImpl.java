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
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Locale;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.handles.BukkitItemHandle;
import com.dfsek.terra.bukkit.handles.BukkitWorldHandle;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;


public class PlatformImpl extends AbstractPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformImpl.class);
    
    private final ItemHandle itemHandle = new BukkitItemHandle();
    
    private final WorldHandle handle = new BukkitWorldHandle();
    
    private final TerraBukkitPlugin plugin;
    
    public PlatformImpl(TerraBukkitPlugin plugin) {
        this.plugin = plugin;
        load();
    }
    
    public TerraBukkitPlugin getPlugin() {
        return plugin;
    }
    
    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        getRawConfigRegistry().clear();
        boolean succeed = getRawConfigRegistry().loadAll(this);
        
        Bukkit.getWorlds().forEach(world -> {
            if(world.getGenerator() instanceof BukkitChunkGeneratorWrapper wrapper) {
                getConfigRegistry().get(wrapper.getPack().getRegistryKey()).ifPresent(pack -> {
                    wrapper.setPack(pack);
                    LOGGER.info("Replaced pack in chunk generator for world {}", world);
                });
            }
        });
        
        return succeed;
    }
    
    @Override
    public @NotNull String platformName() {
        return "Bukkit";
    }
    
    @Override
    public void runPossiblyUnsafeTask(@NotNull Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }
    
    @Override
    protected Iterable<BaseAddon> platformAddon() {
        return List.of(new BukkitAddon(this));
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
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry.registerLoader(BlockState.class, (type, o, loader, depthTracker) -> handle.createBlockState((String) o))
                .registerLoader(PlatformBiome.class, (type, o, loader, depthTracker) -> parseBiome((String) o, depthTracker))
                .registerLoader(EntityType.class, (type, o, loader, depthTracker) -> EntityType.valueOf((String) o));
        
    }
    
    private BukkitPlatformBiome parseBiome(String id, DepthTracker depthTracker) throws LoadException {
        if(!id.startsWith("minecraft:")) throw new LoadException("Invalid biome identifier " + id, depthTracker);
        return new BukkitPlatformBiome(org.bukkit.block.Biome.valueOf(id.toUpperCase(Locale.ROOT).substring(10)));
    }
}
