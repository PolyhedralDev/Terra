package com.dfsek.terra;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.generator.GeneratorWrapper;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.config.base.ConfigPack;

public class TerraWorld {
    private final TerraBiomeGrid grid;
    private final BiomeZone zone;
    private final ConfigPack config;
    private final boolean safe;
    private final TerraProfiler profiler;
    private final World world;


    public TerraWorld(World w, ConfigPack c, TerraPlugin main) {
        config = c;
        profiler = new TerraProfiler(w);
        this.grid = new TerraBiomeGrid.TerraBiomeGridBuilder(w.getSeed(), c, main).build();
        this.zone = grid.getZone();
        this.world = w;
        safe = true;
    }

    public World getWorld() {
        return world;
    }

    public static boolean isTerraWorld(World w) {
        return w.getGenerator().getHandle() instanceof GeneratorWrapper;
    }

    public TerraBiomeGrid getGrid() {
        return grid;
    }

    public ConfigPack getConfig() {
        return config;
    }

    public BiomeZone getZone() {
        return zone;
    }

    public boolean isSafe() {
        return safe;
    }

    public TerraProfiler getProfiler() {
        return profiler;
    }
}
