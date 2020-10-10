package com.dfsek.terra.population;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import com.dfsek.terra.util.DataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SnowPopulator extends GaeaBlockPopulator {
    private static final Set<Material> blacklistSpawn = new HashSet<>();
    static {
        for(Material m : Material.values()) {
            String name = m.toString().toLowerCase();
            if(name.contains("slab")
                    || name.contains("stair")
                    || name.contains("wall")
                    || name.contains("fence")
                    || name.contains("lantern")
                    || name.contains("chest")
                    || name.contains("door")
                    || name.contains("repeater")
                    || name.equals("lily_pad")
                    || name.equals("snow")
                    || name.equals("pane")) blacklistSpawn.add(m);
        }
        blacklistSpawn.add(Material.END_STONE);
        if(ConfigUtil.debug)
            Bukkit.getLogger().info("Added " + blacklistSpawn.size() + " materials to snow blacklist");
    }
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try (ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("SnowTime")) {
            int origX = chunk.getX() << 4;
            int origZ = chunk.getZ() << 4;
            TerraWorld w = TerraWorld.getWorld(world);
            if(! w.isSafe()) return;
            TerraBiomeGrid g = w.getGrid();
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    BiomeConfig biome = w.getConfig().getBiome((UserDefinedBiome) g.getBiome(origX + x, origZ + z, GenerationPhase.PALETTE_APPLY));
                    if(!biome.getSnow().doSnow()) continue;
                    int y;
                    Block b = null;
                    for(y = 254; y > 0; y--) {
                        b = chunk.getBlock(x, y, z);
                        if(! b.getType().isAir()) break;
                    }
                    if(random.nextInt(100) >= biome.getSnow().getSnowChance(y))
                        continue;
                    if(blacklistSpawn.contains(b.getType()) || b.isPassable()) continue;
                    chunk.getBlock(x, ++ y, z).setBlockData(DataUtil.SNOW);
                }
            }
        }
    }
}
