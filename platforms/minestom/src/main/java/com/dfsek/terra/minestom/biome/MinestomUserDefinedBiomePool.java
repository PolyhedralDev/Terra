package com.dfsek.terra.minestom.biome;

import java.util.HashSet;
import java.util.IdentityHashMap;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.minestom.api.BiomeFactory;


public class MinestomUserDefinedBiomePool {
    private final IdentityHashMap<Biome, UserDefinedBiome> biomes = new IdentityHashMap<>();
    private final HashSet<String> createdBiomes = new HashSet<>();
    private final BiomeFactory factory;

    public MinestomUserDefinedBiomePool(BiomeFactory factory) {
        this.factory = factory;
    }

    public UserDefinedBiome getBiome(ConfigPack configPack, Biome source) {
        UserDefinedBiome userDefinedBiome = biomes.get(source);
        if(userDefinedBiome != null) return userDefinedBiome;
        userDefinedBiome = factory.create(configPack, source);
        biomes.put(source, userDefinedBiome);
        createdBiomes.add(source.getID());
        return userDefinedBiome;
    }

    public void preloadBiomes(ConfigPack configPack, Iterable<Biome> biomesToLoad) {
        biomesToLoad
            .forEach(biome -> {
                if(!this.createdBiomes.contains(biome.getID())) {
                    UserDefinedBiome udf = factory.create(configPack, biome);
                    this.biomes.put(biome, udf);
                    this.createdBiomes.add(biome.getID());
                }
            });
    }

    public void invalidate() {
        biomes.clear();
    }
}
