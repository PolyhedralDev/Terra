package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.biome.TerraBiomeGrid;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.Fauna;

import java.util.Random;

public class FaunaPopulator extends BlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        ProfileFuture fauna = TerraProfiler.fromWorld(world).measure("FaunaTime");
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                Biome biome = TerraBiomeGrid.fromWorld(world).getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z);
                if(biome.getDecorator().getFaunaChance() <= 0 || random.nextInt(100) > biome.getDecorator().getFaunaChance())
                    continue;
                Fauna item = biome.getDecorator().getFauna().get(random);
                Block highest = item.getHighestValidSpawnAt(chunk, x, z);
                try {
                    if(highest != null) item.plant(highest.getLocation());
                } catch(NullPointerException ignored) {}
            }
        }
        if(fauna!=null) fauna.complete();
    }
}
