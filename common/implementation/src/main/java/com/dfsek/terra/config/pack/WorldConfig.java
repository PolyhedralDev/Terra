package com.dfsek.terra.config.pack;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.registry.OpenRegistry;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.math.SamplerCache;
import com.dfsek.terra.world.population.items.TerraStructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldConfig {
    private final SamplerCache samplerCache;

    private final BiomeProvider provider;

    private final TerraWorld world;
    private final ConfigPackImpl pack;

    private final Map<Class<?>, LockedRegistry<?>> registryMap = new HashMap<>();

    public WorldConfig(TerraWorld world, ConfigPackImpl pack, TerraPlugin main) {
        this.world = world;
        this.pack = pack;
        this.samplerCache = new SamplerCache(main, world);

        pack.getRegistryMap().forEach((clazz, pair) -> registryMap.put(clazz, new LockedRegistry<>(pair.getLeft())));

        OpenRegistry<TerraBiome> biomeOpenRegistry = new OpenRegistry<>();
        pack.getRegistry(BiomeBuilder.class).forEach((id, biome) -> biomeOpenRegistry.add(id, biome.apply(world.getWorld().getSeed())));
        registryMap.put(TerraBiome.class, new LockedRegistry<>(biomeOpenRegistry));

        this.provider = pack.getBiomeProviderBuilder().build(world.getWorld().getSeed());
    }

    @SuppressWarnings("unchecked")
    public <T> LockedRegistry<T> getRegistry(Class<T> clazz) {
        return (LockedRegistry<T>) registryMap.get(clazz);
    }

    public TerraWorld getWorld() {
        return world;
    }

    public SamplerCache getSamplerCache() {
        return samplerCache;
    }

    public Set<UserDefinedCarver> getCarvers() {
        return new HashSet<>(getRegistry(UserDefinedCarver.class).entries());
    }

    public BiomeProvider getProvider() {
        return provider;
    }

    public Set<TerraStructure> getStructures() {
        return new HashSet<>(getRegistry(TerraStructure.class).entries());
    }

    public ConfigPackTemplate getTemplate() {
        return pack.getTemplate();
    }
}
