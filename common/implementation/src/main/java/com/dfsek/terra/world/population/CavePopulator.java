package com.dfsek.terra.world.population;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.util.world.PopulationUtil;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.dfsek.terra.api.world.generator.TerraBlockPopulator;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.pack.WorldConfigImpl;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.api.profiler.ProfileFrame;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CavePopulator implements TerraBlockPopulator, Chunkified {
    private static final Map<BlockType, BlockData> shiftStorage = new HashMap<>(); // Persist BlockData created for shifts, to avoid re-calculating each time.
    private final TerraPlugin main;

    public CavePopulator(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        WorldHandle handle = main.getWorldHandle();
        BlockData AIR = handle.createBlockData("minecraft:air");
        try(ProfileFrame ignore = main.getProfiler().profile("carving")) {
            Random random = PopulationUtil.getRandom(chunk);
            if(!tw.isSafe()) return;
            WorldConfigImpl config = tw.getConfig();
            if(config.getTemplate().disableCarvers()) return;

            for(UserDefinedCarver c : config.getCarvers()) {
                CarverTemplate template = c.getConfig();
                Map<LocationImpl, BlockData> shiftCandidate = new HashMap<>();
                Set<Block> updateNeeded = new HashSet<>();
                c.carve(chunk.getX(), chunk.getZ(), world, (v, type) -> {
                    try(ProfileFrame ignored = main.getProfiler().profile("carving:" + c.getConfig().getID())) {
                        Block b = chunk.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                        BlockData m = b.getBlockData();
                        BlockType re = m.getBlockType();
                        switch(type) {
                            case CENTER:
                                if(template.getInner().canReplace(re)) {
                                    b.setBlockData(template.getInner().get(v.getBlockY()).get(random), false);
                                    if(template.getUpdate().contains(re)) updateNeeded.add(b);
                                    if(template.getShift().containsKey(re)) shiftCandidate.put(b.getLocation(), m);
                                }
                                break;
                            case WALL:
                                if(template.getOuter().canReplace(re)) {
                                    b.setBlockData(template.getOuter().get(v.getBlockY()).get(random), false);
                                    if(template.getUpdate().contains(re)) updateNeeded.add(b);
                                    if(template.getShift().containsKey(re)) shiftCandidate.put(b.getLocation(), m);
                                }
                                break;
                            case TOP:
                                if(template.getTop().canReplace(re)) {
                                    b.setBlockData(template.getTop().get(v.getBlockY()).get(random), false);
                                    if(template.getUpdate().contains(re)) updateNeeded.add(b);
                                    if(template.getShift().containsKey(re)) shiftCandidate.put(b.getLocation(), m);
                                }
                                break;
                            case BOTTOM:
                                if(template.getBottom().canReplace(re)) {
                                    b.setBlockData(template.getBottom().get(v.getBlockY()).get(random), false);
                                    if(template.getUpdate().contains(re)) updateNeeded.add(b);
                                    if(template.getShift().containsKey(re)) shiftCandidate.put(b.getLocation(), m);
                                }
                                break;
                        }
                    }
                });
                for(Map.Entry<LocationImpl, BlockData> entry : shiftCandidate.entrySet()) {
                    LocationImpl l = entry.getKey();
                    LocationImpl mut = l.clone();
                    BlockData orig = l.getBlock().getBlockData();
                    do mut.subtract(0, 1, 0);
                    while(mut.getY() > world.getMinHeight() && mut.getBlock().getBlockData().matches(orig));
                    try {
                        if(template.getShift().get(entry.getValue().getBlockType()).contains(mut.getBlock().getBlockData().getBlockType())) {
                            mut.getBlock().setBlockData(shiftStorage.computeIfAbsent(entry.getValue().getBlockType(), BlockType::getDefaultData), false);
                        }
                    } catch(NullPointerException ignored) {
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
}
