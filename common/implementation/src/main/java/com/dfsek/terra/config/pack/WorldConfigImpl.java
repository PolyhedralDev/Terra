package com.dfsek.terra.config.pack;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.SamplerCache;
import com.dfsek.terra.registry.LockedRegistryImpl;
import com.dfsek.terra.world.SamplerCacheImpl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class WorldConfigImpl implements WorldConfig {
    private final SamplerCache samplerCache;

    private final BiomeProvider provider;

    private final World world;
    private final ConfigPackImpl pack;

    private final Map<Type, Registry<?>> registryMap = new HashMap<>();

    public WorldConfigImpl(World world, ConfigPackImpl pack, TerraPlugin main) {
        this.world = world;
        this.pack = pack;
        this.samplerCache = new SamplerCacheImpl(main, world);

        pack.getRegistryMap().forEach((clazz, pair) -> registryMap.put(clazz, new LockedRegistryImpl<>(pair.getLeft())));

        this.provider = pack.getBiomeProviderBuilder();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Registry<T> getRegistry(Class<T> clazz) {
        return (LockedRegistryImpl<T>) registryMap.get(clazz);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public SamplerCache getSamplerCache() {
        return samplerCache;
    }

    @Override
    public BiomeProvider getProvider() {
        return provider;
    }

    @Override
    public int elevationBlend() {
        return pack.getTemplate().getElevationBlend();
    }

    @Override
    public boolean disableTrees() {
        return pack.getTemplate().disableTrees();
    }

    @Override
    public boolean disableCarving() {
        return pack.getTemplate().disableCarvers();
    }

    @Override
    public boolean disableOres() {
        return pack.getTemplate().disableOres();
    }

    @Override
    public boolean disableFlora() {
        return pack.getTemplate().disableFlora();
    }

    @Override
    public boolean disableStructures() {
        return pack.getTemplate().disableStructures();
    }

    @Override
    public String getID() {
        return pack.getID();
    }

    @Override
    public String getAuthor() {
        return pack.getAuthor();
    }

    @Override
    public String getVersion() {
        return pack.getVersion();
    }

    @Override
    public Map<String, String> getLocatable() {
        return pack.getLocatable();
    }

    @Override
    public boolean isDisableSaplings() {
        return getTemplate().isDisableSaplings();
    }

    public ConfigPackTemplate getTemplate() {
        return pack.getTemplate();
    }
}
