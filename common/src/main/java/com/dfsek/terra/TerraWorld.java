package com.dfsek.terra;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.generator.GeneratorWrapper;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.config.base.ConfigPack;

public class TerraWorld {
    private final BiomeProvider provider;
    private final ConfigPack config;
    private final boolean safe;
    private final TerraProfiler profiler;
    private final World world;


    public TerraWorld(World w, ConfigPack c, TerraPlugin main) {
        config = c;
        profiler = new TerraProfiler(w);
        this.provider = config.getTemplate().getProviderBuilder().build(w.getSeed());
        this.world = w;
        safe = true;
    }

    public World getWorld() {
        return world;
    }

    public static boolean isTerraWorld(World w) {
        return w.getGenerator().getHandle() instanceof GeneratorWrapper;
    }

    public BiomeProvider getBiomeProvider() {
        return provider;
    }

    public ConfigPack getConfig() {
        return config;
    }

    public boolean isSafe() {
        return safe;
    }

    public TerraProfiler getProfiler() {
        return profiler;
    }
}
