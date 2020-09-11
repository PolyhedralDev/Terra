package com.dfsek.terra.population;

import com.dfsek.terra.biome.TerraBiomeGrid;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.world.Fauna;

import java.util.Random;

public class FaunaPopulator extends BlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                Biome biome = TerraBiomeGrid.fromWorld(world).getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z);
                if(biome.getDecorator().getFaunaChance() <= 0 || random.nextInt(100) > biome.getDecorator().getFaunaChance())
                    continue;
                Block highest = Fauna.getHighestValidSpawnAt(chunk, x, z);
                try {
                    if(highest != null) biome.getDecorator().getFauna().get(random).plant(highest.getLocation());
                } catch(NullPointerException ignored) {}
            }
        }
    }
}
