package com.dfsek.terra.population;

import com.dfsek.terra.MaxMin;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import com.dfsek.terra.config.genconfig.OreConfig;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.Map;
import java.util.Random;

public class OrePopulator extends GaeaBlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try (ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("OreTime")) {
            Location l = chunk.getBlock(8, 0, 0).getLocation();
            Biome b = TerraBiomeGrid.fromWorld(world).getBiome(l.getBlockX(), l.getBlockZ());
            for(Map.Entry<OreConfig, MaxMin> e : BiomeConfig.fromBiome((UserDefinedBiome) b).getOres().entrySet()) {
                int num = e.getValue().get(random);
                for(int i = 0; i < num; i++) {
                    int x = random.nextInt(16);
                    int z = random.nextInt(16);
                    int y = BiomeConfig.fromBiome((UserDefinedBiome) b).getOreHeight(e.getKey()).get(random);
                    e.getKey().doVein(chunk.getBlock(x, y, z).getLocation(), random);
                }
            }
        }
    }
}
