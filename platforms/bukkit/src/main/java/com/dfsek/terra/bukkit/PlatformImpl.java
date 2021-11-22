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

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.Locale;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.handles.BukkitItemHandle;
import com.dfsek.terra.bukkit.handles.BukkitWorldHandle;
import com.dfsek.terra.bukkit.world.BukkitBiome;


public class PlatformImpl extends AbstractPlatform {
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
        return false;
    }
    
    @Override
    public String platformName() {
        return "Bukkit";
    }
    
    @Override
    public void runPossiblyUnsafeTask(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }
    
    @Override
    public WorldHandle getWorldHandle() {
        return handle;
    }
    
    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }
    
    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }
    
    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry
                .registerLoader(BlockState.class, (t, o, l) -> handle.createBlockData((String) o))
                .registerLoader(Biome.class, (t, o, l) -> parseBiome((String) o))
                .registerLoader(EntityType.class, (t, o, l) -> EntityType.valueOf((String) o));
        
    }
    
    private BukkitBiome parseBiome(String id) throws LoadException {
        if(!id.startsWith("minecraft:")) throw new LoadException("Invalid biome identifier " + id);
        return new BukkitBiome(org.bukkit.block.Biome.valueOf(id.toUpperCase(Locale.ROOT).substring(10)));
    }
}
