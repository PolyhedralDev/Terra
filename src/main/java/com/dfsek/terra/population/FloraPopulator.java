package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import com.dfsek.terra.config.genconfig.biome.BiomeFloraConfig;
import com.dfsek.terra.event.TreeGenerateEvent;
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
import org.polydev.gaea.world.Flora;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Populates Flora and Trees
 */
public class FloraPopulator extends GaeaBlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("FloraTime")) {
            TerraWorld tw = TerraWorld.getWorld(world);
            if(!tw.isSafe()) return;
            int originX = chunk.getX() << 4;
            int originZ = chunk.getZ() << 4;
            TerraBiomeGrid grid = tw.getGrid();
            ConfigPack config = tw.getConfig();
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z, GenerationPhase.POPULATE);
                    if((x & 1) == 0 && (z & 1) == 0) {
                        int treeChance = biome.getDecorator().getTreeDensity();
                        if(random.nextInt(1000) < treeChance) {
                            int xt = offset(random, x);
                            int zt = offset(random, z);
                            if(doTrees(biome, tw, random, chunk, xt, zt)) continue;
                        }
                    }
                    if(biome.getDecorator().getFloraChance() <= 0) continue;
                    try {
                        BiomeConfig c = config.getBiome(biome);
                        BiomeFloraConfig f = c.getFlora();
                        for(int i = 0; i < f.getFloraAttempts(); i++) {
                            Flora item;
                            if(f.isFloraSimplex())
                                item = biome.getDecorator().getFlora().get(f.getFloraNoise(), originX + x, originZ + z);
                            else item = biome.getDecorator().getFlora().get(random);
                            for(Block highest : item.getValidSpawnsAt(chunk, x, z, c.getFloraHeights(item))) {
                                if (random.nextInt(100) < biome.getDecorator().getFloraChance())
                                    item.plant(highest.getLocation());
                            }
                        }
                    } catch(NullPointerException ignore) {
                    }
                }
            }
        }
    }

    private static boolean doTrees(@NotNull UserDefinedBiome biome, TerraWorld world, @NotNull Random random, @NotNull Chunk chunk, int x, int z) {
        for(Block block : getValidTreeSpawnsAt(chunk, x, z, new Range(0, 254))) {
            Tree tree = biome.getDecorator().getTrees().get(random);
            Range range = world.getConfig().getBiome(biome).getTreeRange(tree);
            if (!range.isInRange(block.getY())) continue;
            try {
                Location l = block.getLocation();
                TreeGenerateEvent event = new TreeGenerateEvent(world, l, tree);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) tree.plant(l, random, Terra.getInstance());
            } catch(NullPointerException ignore) {
            }
        }
        return false;
    }

    public static List<Block> getValidTreeSpawnsAt(Chunk chunk, int x, int z, Range check) {
        List<Block> blocks = new ArrayList<>();
        for(int y : check) {
            if (chunk.getBlock(x, y, z).getType().isSolid() && chunk.getBlock(x, y + 1, z).getType().isAir()) {
                blocks.add(chunk.getBlock(x, y + 1, z));
            }
        }
        return blocks;
    }

    private static int offset(Random r, int i) {
        return Math.min(Math.max(i + r.nextInt(3) - 1, 0), 15);
    }
}
