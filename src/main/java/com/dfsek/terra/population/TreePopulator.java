package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.generation.UserDefinedDecorator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.util.WorldUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreePopulator extends GaeaBlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("TreeGenTime")) {
            TerraWorld tw = TerraWorld.getWorld(world);
            TerraBiomeGrid grid = tw.getGrid();;
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            int origX = chunk.getX() << 4;
            int origZ = chunk.getZ() << 4;
            Biome b = grid.getBiome(x+origX, z+origZ, GenerationPhase.POPULATE);
            if(((UserDefinedDecorator) b.getDecorator()).getTreeChance() < random.nextInt(100)) return;
            int max = 50;
            int att = 0;
            for(int i = 0; i < b.getDecorator().getTreeDensity() && att < max; ) {
                att++;
                for(Block block : getValidSpawnsAt(chunk, x, z, new Range(0, 254))) {
                    Tree tree = b.getDecorator().getTrees().get(random);
                    Range range = tw.getConfig().getBiome((UserDefinedBiome) b).getTreeRange(tree);
                    if(!range.isInRange(block.getY())) continue;
                    b = grid.getBiome(x+origX, z+origZ, GenerationPhase.POPULATE);
                    try {
                        if(tree.plant(block.getLocation(), random, false, Terra.getInstance())) i++;
                    } catch(NullPointerException ignore) {}
                }

                x = random.nextInt(16);
                z = random.nextInt(16);
            }
        }
    }
    public List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range check) {
        List<Block> blocks = new ArrayList<>();
        for(int y : check) {
            if(chunk.getBlock(x, y, z).getType().isSolid() && chunk.getBlock(x, y + 1, z).getType().isAir()) {
                blocks.add(chunk.getBlock(x, y, z));
            }
        }
        return blocks;
    }
}
