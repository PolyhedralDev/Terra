package com.dfsek.terra.population;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.util.PopulationUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.carving.Carver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CavePopulator extends BlockPopulator {
    private static final Map<Material, BlockData> shiftStorage = new HashMap<>(); // Persist BlockData created for shifts, to avoid re-calculating each time.
    private static final BlockData AIR = Material.AIR.createBlockData();

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Random r, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("CaveTime")) {
            Random random = PopulationUtil.getRandom(chunk);
            TerraWorld tw = TerraWorld.getWorld(world);
            if(!tw.isSafe()) return;
            ConfigPack config = tw.getConfig();

            for(UserDefinedCarver c : config.getCarvers()) {
                CarverTemplate template = c.getConfig();
                Map<Location, Material> shiftCandidate = new HashMap<>();
                Set<Block> updateNeeded = new HashSet<>();
                c.carve(chunk.getX(), chunk.getZ(), world, (v, type) -> {
                    Block b = chunk.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                    Material m = b.getType();
                    if(type.equals(Carver.CarvingType.CENTER) && template.getInner().canReplace(m)) {
                        if(template.getShift().containsKey(b.getType()))
                            shiftCandidate.put(b.getLocation(), b.getType());
                        b.setBlockData(template.getInner().get(v.getBlockY()).get(random), false);
                    } else if(type.equals(Carver.CarvingType.WALL) && template.getOuter().canReplace(m)) {
                        if(template.getShift().containsKey(b.getType()))
                            shiftCandidate.put(b.getLocation(), b.getType());
                        b.setBlockData(template.getOuter().get(v.getBlockY()).get(random), false);
                    } else if(type.equals(Carver.CarvingType.TOP) && template.getTop().canReplace(m)) {
                        if(template.getShift().containsKey(b.getType()))
                            shiftCandidate.put(b.getLocation(), b.getType());
                        b.setBlockData(template.getTop().get(v.getBlockY()).get(random), false);
                    } else if(type.equals(Carver.CarvingType.BOTTOM) && template.getBottom().canReplace(m)) {
                        if(template.getShift().containsKey(b.getType()))
                            shiftCandidate.put(b.getLocation(), b.getType());
                        b.setBlockData(template.getBottom().get(v.getBlockY()).get(random), false);
                    }
                    if(template.getUpdate().contains(m)) {
                        updateNeeded.add(b);
                    }
                });
                for(Map.Entry<Location, Material> entry : shiftCandidate.entrySet()) {
                    Location l = entry.getKey();
                    Location mut = l.clone();
                    Material orig = l.getBlock().getType();
                    do mut.subtract(0, 1, 0);
                    while(mut.getBlock().getType().equals(orig));
                    try {
                        if(template.getShift().get(entry.getValue()).contains(mut.getBlock().getType())) {
                            mut.getBlock().setBlockData(shiftStorage.computeIfAbsent(entry.getValue(), Material::createBlockData), false);
                        }
                    } catch(NullPointerException ignore) {
                    }
                }
                for(Block b : updateNeeded) {
                    BlockData orig = b.getBlockData();
                    b.setBlockData(AIR, false);
                    b.setBlockData(orig, true);
                }
            }

        }
    }

    private boolean borderingOcean(Block b) {
        return b.getRelative(BlockFace.UP).getType().equals(Material.WATER) || b.getType().equals(Material.LAVA);
    }
}
