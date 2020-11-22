package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import com.dfsek.terra.event.TreeGenerateEvent;
import net.jafama.FastMath;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.util.GlueList;

import java.util.List;
import java.util.Random;

public class TreePopulator extends GaeaBlockPopulator {
    private static void doTrees(@NotNull UserDefinedBiome biome, TerraWorld world, @NotNull Random random, @NotNull Chunk chunk, int x, int z) {
        for(Block block : getValidTreeSpawnsAt(chunk, x, z, new Range(0, 254))) {
            Tree tree = biome.getDecorator().getTrees().get(random);
            Range range = biome.getConfig().getTreeRange(tree);
            if(!range.isInRange(block.getY())) continue;
            try {
                Location l = block.getLocation();
                TreeGenerateEvent event = new TreeGenerateEvent(world, l, tree);
                Bukkit.getPluginManager().callEvent(event);
                if(!event.isCancelled()) tree.plant(l, random, Terra.getInstance());
            } catch(NullPointerException ignore) {
            }
        }
    }

    public static List<Block> getValidTreeSpawnsAt(Chunk chunk, int x, int z, Range check) {
        List<Block> blocks = new GlueList<>();
        for(int y : check) {
            if(chunk.getBlock(x, y, z).getType().isSolid() && chunk.getBlock(x, y + 1, z).getType().isAir()) {
                blocks.add(chunk.getBlock(x, y + 1, z));
            }
        }
        return blocks;
    }

    private static int offset(Random r, int i) {
        return FastMath.min(FastMath.max(i + r.nextInt(3) - 1, 0), 15);
    }

    @Override
    @SuppressWarnings("try")
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("TreeTime")) {
            TerraWorld tw = TerraWorld.getWorld(world);
            if(!tw.isSafe()) return;
            TerraBiomeGrid grid = tw.getGrid();
            for(int x = 0; x < 16; x += 2) {
                for(int z = 0; z < 16; z += 2) {
                    UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z, GenerationPhase.POPULATE);
                    int treeChance = biome.getDecorator().getTreeDensity();
                    if(random.nextInt(1000) < treeChance) {
                        int xt = offset(random, x);
                        int zt = offset(random, z);
                        doTrees(biome, tw, random, chunk, xt, zt);
                    }
                }
            }
        }
    }
}
