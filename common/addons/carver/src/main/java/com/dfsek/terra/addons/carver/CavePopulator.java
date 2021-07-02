package com.dfsek.terra.addons.carver;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.dfsek.terra.api.world.generator.TerraBlockPopulator;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.templates.CarverTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CavePopulator implements TerraBlockPopulator, Chunkified {
    private static final Map<BlockType, BlockState> shiftStorage = new HashMap<>(); // Persist BlockData created for shifts, to avoid re-calculating each time.
    private final TerraPlugin main;

    public CavePopulator(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        WorldHandle handle = main.getWorldHandle();
        try(ProfileFrame ignore = main.getProfiler().profile("carving")) {
            Random random = PopulationUtil.getRandom(chunk);
            if(!tw.isSafe()) return;
            WorldConfig config = tw.getConfig();
            if(config.disableCarving()) return;

            for(UserDefinedCarver c : config.getRegistry(UserDefinedCarver.class).entries()) {
                CarverTemplate template = c.getConfig();
                Map<Vector3, BlockState> shiftCandidate = new HashMap<>();
                c.carve(chunk.getX(), chunk.getZ(), world, (v, type) -> {
                    try(ProfileFrame ignored = main.getProfiler().profile("carving:" + c.getConfig().getID())) {
                        BlockState m = chunk.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                        BlockType re = m.getBlockType();
                        switch(type) {
                            case CENTER:
                                if(template.getInner().canReplace(re)) {
                                    chunk.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), template.getInner().get(v.getBlockY()).get(random), template.getUpdate().contains(re));
                                    if(template.getShift().containsKey(re)) shiftCandidate.put(v, m);
                                }
                                break;
                            case WALL:
                                if(template.getOuter().canReplace(re)) {
                                    chunk.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), template.getOuter().get(v.getBlockY()).get(random), template.getUpdate().contains(re));
                                    if(template.getShift().containsKey(re)) shiftCandidate.put(v, m);
                                }
                                break;
                            case TOP:
                                if(template.getTop().canReplace(re)) {
                                    chunk.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), template.getTop().get(v.getBlockY()).get(random), template.getUpdate().contains(re));
                                    if(template.getShift().containsKey(re)) shiftCandidate.put(v, m);
                                }
                                break;
                            case BOTTOM:
                                if(template.getBottom().canReplace(re)) {
                                    chunk.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), template.getBottom().get(v.getBlockY()).get(random), template.getUpdate().contains(re));
                                    if(template.getShift().containsKey(re)) shiftCandidate.put(v, m);
                                }
                                break;
                        }
                    }
                });
                for(Map.Entry<Vector3, BlockState> entry : shiftCandidate.entrySet()) {
                    Vector3 l = entry.getKey();
                    Vector3 mut = l.clone();
                    BlockState orig = chunk.getBlock(l.getBlockX(), l.getBlockY(), l.getBlockZ());
                    do mut.subtract(0, 1, 0);
                    while(mut.getY() > world.getMinHeight() && chunk.getBlock(mut.getBlockX(), mut.getBlockY(), mut.getBlockZ()).matches(orig));
                    try {
                        if(template.getShift().get(entry.getValue().getBlockType()).contains(chunk.getBlock(mut.getBlockX(), mut.getBlockY(), mut.getBlockZ()).getBlockType())) {
                            chunk.setBlock(mut.getBlockX(), mut.getBlockY(), mut.getBlockZ(), shiftStorage.computeIfAbsent(entry.getValue().getBlockType(), BlockType::getDefaultData), false);
                        }
                    } catch(NullPointerException ignored) {
                    }
                }
            }

        }
    }
}
