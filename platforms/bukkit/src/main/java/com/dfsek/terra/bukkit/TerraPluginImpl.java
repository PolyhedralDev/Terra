package com.dfsek.terra.bukkit;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.AbstractTerraPlugin;
import com.dfsek.terra.api.Logger;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.handles.BukkitItemHandle;
import com.dfsek.terra.bukkit.handles.BukkitWorldHandle;
import com.dfsek.terra.bukkit.world.BukkitBiome;
import com.dfsek.terra.util.logging.JavaLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.Locale;
import java.util.Optional;

public class TerraPluginImpl extends AbstractTerraPlugin {
    private final ItemHandle itemHandle = new BukkitItemHandle();
    private final TerraBukkitPlugin plugin;
    private WorldHandle handle = new BukkitWorldHandle();

    public TerraPluginImpl(TerraBukkitPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public TerraBukkitPlugin getPlugin() {
        return plugin;
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
    public boolean reload() {
        return false;
    }

    @Override
    protected Optional<TerraAddon> getPlatformAddon() {
        return Optional.of(new BukkitAddon(this));
    }

    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }

    @Override
    public String platformName() {
        return "Bukkit";
    }

    @Override
    protected Logger createLogger() {
        return new JavaLogger(plugin.getLogger());
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

    @Override
    public void runPossiblyUnsafeTask(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }
}
