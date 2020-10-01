package com.dfsek.terra.population;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.Flora;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FloraPopulator extends GaeaBlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try (ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("FloraTime")) {
            TerraWorld tw = TerraWorld.getWorld(world);
            TerraBiomeGrid grid = tw.getGrid();
            TerraConfig config = tw.getConfig();
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z, GenerationPhase.POPULATE);
                    if(biome.getDecorator().getFloraChance() <= 0) continue;
                    try {
                        BiomeConfig c = config.getBiome(biome);
                        for(int i = 0; i < c.getFloraAttempts(); i++) {
                            Flora item;
                            if(c.isFloraSimplex()) item = biome.getDecorator().getFlora().get(c.getFloraNoise(), (chunk.getX() << 4) + x, (chunk.getZ() << 4) + z);
                            else item = biome.getDecorator().getFlora().get(random);
                            for(Block highest : item.getValidSpawnsAt(chunk, x, z, c.getFloraHeights(item))) {
                                if(random.nextInt(100) < biome.getDecorator().getFloraChance())
                                    item.plant(highest.getLocation());
                            }
                        }
                    } catch(NullPointerException ignore) {}
                }
            }
        }
    }
}
