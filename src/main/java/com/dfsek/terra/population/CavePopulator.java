package com.dfsek.terra.population;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.genconfig.CarverConfig;
import com.dfsek.terra.util.PopulationUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.carving.CarvingData;

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
        if(ConfigUtil.masterDisableCaves) return;
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("CaveTime")) {
            Random random = PopulationUtil.getRandom(chunk);
            TerraWorld tw = TerraWorld.getWorld(world);
            if(!tw.isSafe()) return;
            ConfigPack config = tw.getConfig();

            for(CarverConfig c : config.getCarvers().values()) {
                Map<Location, Material> shiftCandidate = new HashMap<>();
                Set<Block> updateNeeded = new HashSet<>();
                Map<Vector, CarvingData.CarvingType> blocks = c.getCarver().carve(chunk.getX(), chunk.getZ(), world).getCarvedBlocks();
                for(Map.Entry<Vector, CarvingData.CarvingType> e : blocks.entrySet()) {
                    Vector v = e.getKey();
                    Block b = chunk.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                    Material m = b.getType();
                    if(e.getValue().equals(CarvingData.CarvingType.CENTER) && c.isReplaceableInner(m)) {
                        if(c.getShiftedBlocks().containsKey(b.getType()))
                            shiftCandidate.put(b.getLocation(), b.getType());
                        b.setBlockData(c.getPaletteInner(v.getBlockY()).get(random), c.shouldUpdateOcean() && borderingOcean(b));
                    } else if(e.getValue().equals(CarvingData.CarvingType.WALL) && c.isReplaceableOuter(m)) {
                        if(c.getShiftedBlocks().containsKey(b.getType()))
                            shiftCandidate.put(b.getLocation(), b.getType());
                        b.setBlockData(c.getPaletteOuter(v.getBlockY()).get(random), c.shouldUpdateOcean() && borderingOcean(b));
                    } else if(e.getValue().equals(CarvingData.CarvingType.TOP) && c.isReplaceableTop(m)) {
                        if(c.getShiftedBlocks().containsKey(b.getType()))
                            shiftCandidate.put(b.getLocation(), b.getType());
                        b.setBlockData(c.getPaletteTop(v.getBlockY()).get(random), c.shouldUpdateOcean() && borderingOcean(b));
                    } else if(e.getValue().equals(CarvingData.CarvingType.BOTTOM) && c.isReplaceableBottom(m)) {
                        if(c.getShiftedBlocks().containsKey(b.getType()))
                            shiftCandidate.put(b.getLocation(), b.getType());
                        b.setBlockData(c.getPaletteBottom(v.getBlockY()).get(random), c.shouldUpdateOcean() && borderingOcean(b));
                    }
                    if(c.getUpdateBlocks().contains(m)) {
                        updateNeeded.add(b);
                    }
                }
                for(Map.Entry<Location, Material> entry : shiftCandidate.entrySet()) {
                    Location l = entry.getKey();
                    Location mut = l.clone();
                    Material orig = l.getBlock().getType();
                    do mut.subtract(0, 1, 0);
                    while(mut.getBlock().getType().equals(orig));
                    try {
                        if(c.getShiftedBlocks().get(entry.getValue()).contains(mut.getBlock().getType())) {
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
                /*for(Map.Entry<Vector, CarvingData.CarvingType> e : new SimplexCarver(chunk.getX(), chunk.getZ()).carve(chunk.getX(), chunk.getZ(), world).getCarvedBlocks().entrySet()) {
                    Vector v = e.getKey();
                    switch(e.getValue()) {
                        case TOP:
                        case CENTER:
                            chunk.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ()).setBlockData(AIR, false);
                            break;
                        case BOTTOM:
                            chunk.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ()).setBlockData(Material.MYCELIUM.createBlockData(), false);
                    }

                }*/
            }

        }
    }

    private boolean borderingOcean(Block b) {
        return b.getRelative(BlockFace.UP).getType().equals(Material.WATER) || b.getType().equals(Material.LAVA);
    }
}
