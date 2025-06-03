package com.dfsek.terra.minestom.biome;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.HashMap;


public class MinestomCustomBiomePool {
    private final HashMap<String, NativeBiome> biomes = new HashMap<>();
    private final MinestomCustomBiomeFactory factory;
    private final ConfigPack configPack;

    public MinestomCustomBiomePool(ConfigPack configPack, MinestomCustomBiomeFactory factory) {
        this.configPack = configPack;
        this.factory = factory;
    }

    public NativeBiome getBiome(Biome source) {
        NativeBiome nativeBiome = biomes.get(source.getID());
        if(nativeBiome != null) return nativeBiome;
        nativeBiome = factory.create(configPack, source);
        biomes.put(source.getID(), nativeBiome);
        return nativeBiome;
    }

    public void preloadBiomes(Iterable<Biome> biomesToLoad) {
        biomesToLoad
            .forEach(biome -> {
                if(!this.biomes.containsKey(biome.getID())) {
                    this.biomes.put(biome.getID(), factory.create(configPack, biome));
                }
            });
    }

    public void invalidate() {
        biomes.clear();
    }
}
