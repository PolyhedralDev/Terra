package com.dfsek.terra.population;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.config.base.ConfigPack;
import org.polydev.gaea.math.Range;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.OreConfig;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.Map;
import java.util.Random;

public class OrePopulator extends GaeaBlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try (ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("OreTime")) {
            ConfigPack config = TerraWorld.getWorld(world).getConfig();
            Biome b = TerraWorld.getWorld(world).getGrid().getBiome((chunk.getX() << 4)+8, (chunk.getZ() << 4) + 8, GenerationPhase.POPULATE);
            for(Map.Entry<OreConfig, Range> e : config.getBiome((UserDefinedBiome) b).getOres().entrySet()) {
                int num = e.getValue().get(random);
                for(int i = 0; i < num; i++) {
                    int x = random.nextInt(16);
                    int z = random.nextInt(16);
                    int y = config.getBiome((UserDefinedBiome) b).getOreHeight(e.getKey()).get(random);
                    e.getKey().doVein(chunk.getBlock(x, y, z).getLocation(), random);
                }
            }
        }
    }
}
