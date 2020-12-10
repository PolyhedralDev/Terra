package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.gaea.profiler.ProfileFuture;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.util.PopulationUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CavePopulator extends BlockPopulator {
    private final Terra main;
    private static final Map<Material, BlockData> shiftStorage = new HashMap<>(); // Persist BlockData created for shifts, to avoid re-calculating each time.
    private static final BlockData AIR = Material.AIR.createBlockData();

    public CavePopulator(Terra main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Random r, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        WorldHandle handle = main.getHandle();
        try(ProfileFuture ignored = tw.getProfiler().measure("CaveTime")) {
            Random random = PopulationUtil.getRandom(chunk);
            if(!tw.isSafe()) return;
            ConfigPack config = tw.getConfig();

            for(UserDefinedCarver c : config.getCarvers()) {
                CarverTemplate template = c.getConfig();
                Map<Location, Material> shiftCandidate = new HashMap<>();
                Set<Block> updateNeeded = new HashSet<>();
                c.carve(chunk.getX(), chunk.getZ(), world, (v, type) -> {
                    Block b = chunk.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                    Material m = handle.getType(b);
                    switch(type) {
                        case CENTER:
                            if(template.getInner().canReplace(m)) {
                                handle.setBlockData(b, template.getInner().get(v.getBlockY()).get(random), false);
                                if(template.getUpdate().contains(m)) updateNeeded.add(b);
                                if(template.getShift().containsKey(m)) shiftCandidate.put(b.getLocation(), m);
                            }
                            break;
                        case WALL:
                            if(template.getOuter().canReplace(m)) {
                                handle.setBlockData(b, template.getOuter().get(v.getBlockY()).get(random), false);
                                if(template.getUpdate().contains(m)) updateNeeded.add(b);
                                if(template.getShift().containsKey(m)) shiftCandidate.put(b.getLocation(), m);
                            }
                            break;
                        case TOP:
                            if(template.getTop().canReplace(m)) {
                                handle.setBlockData(b, template.getTop().get(v.getBlockY()).get(random), false);
                                if(template.getUpdate().contains(m)) updateNeeded.add(b);
                                if(template.getShift().containsKey(m)) shiftCandidate.put(b.getLocation(), m);
                            }
                            break;
                        case BOTTOM:
                            if(template.getBottom().canReplace(m)) {
                                handle.setBlockData(b, template.getBottom().get(v.getBlockY()).get(random), false);
                                if(template.getUpdate().contains(m)) updateNeeded.add(b);
                                if(template.getShift().containsKey(m)) shiftCandidate.put(b.getLocation(), m);
                            }
                            break;
                    }
                });
                for(Map.Entry<Location, Material> entry : shiftCandidate.entrySet()) {
                    Location l = entry.getKey();
                    Location mut = l.clone();
                    Material orig = handle.getType(l.getBlock());
                    do mut.subtract(0, 1, 0);
                    while(handle.getType(mut.getBlock()).equals(orig));
                    try {
                        if(template.getShift().get(entry.getValue()).contains(mut.getBlock().getType())) {
                            handle.setBlockData(mut.getBlock(), shiftStorage.computeIfAbsent(entry.getValue(), Material::createBlockData), false);
                        }
                    } catch(NullPointerException ignore) {
                    }
                }
                for(Block b : updateNeeded) {
                    BlockData orig = handle.getBlockData(b);
                    handle.setBlockData(b, AIR, false);
                    handle.setBlockData(b, orig, true);
                }
            }

        }
    }
}
