package com.dfsek.terra.population;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.genconfig.OreConfig;
import com.dfsek.terra.config.genconfig.biome.BiomeOreConfig;
import com.dfsek.terra.event.OreVeinGenerateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.Map;
import java.util.Random;

public class OrePopulator extends GaeaBlockPopulator {
    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("OreTime")) {
            TerraWorld tw = TerraWorld.getWorld(world);
            if(!tw.isSafe()) return;
            ConfigPack config = tw.getConfig();
            Biome b = TerraWorld.getWorld(world).getGrid().getBiome((chunk.getX() << 4) + 8, (chunk.getZ() << 4) + 8, GenerationPhase.POPULATE);
            BiomeOreConfig ores = config.getBiome((UserDefinedBiome) b).getOres();
            for(Map.Entry<OreConfig, Range> e : ores.getOres().entrySet()) {
                int num = e.getValue().get(random);
                OreConfig ore = e.getKey();
                int edgeOffset = ore.getChunkEdgeOffset();
                for(int i = 0; i < num; i++) {
                    int x = random.nextInt(16 - (edgeOffset << 1)) + edgeOffset;
                    int z = random.nextInt(16 - (edgeOffset << 1)) + edgeOffset;
                    int y = ores.getOreHeights().get(ore).get(random);

                    Vector v = new Vector(x, y, z);
                    OreVeinGenerateEvent event = new OreVeinGenerateEvent(tw, v.toLocation(world), ore);
                    Bukkit.getPluginManager().callEvent(event);
                    if(!event.isCancelled()) {
                        if(ore.crossChunks()) ore.doVein(v, chunk, random);
                        else ore.doVeinSingle(new Vector(x, y, z), chunk, random);
                    }
                }
            }
        }
    }
}
