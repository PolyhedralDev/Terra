package com.dfsek.terra.population;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.config.genconfig.CarverConfig;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.carving.CarvingData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CavePopulator extends BlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        ProfileFuture cave = TerraProfiler.fromWorld(world).measure("CaveTime");
        for(CarverConfig c : CarverConfig.getCarvers()) {
            Map<Location, Material> shiftCandidate = new HashMap<>();
            Map<Vector, CarvingData.CarvingType> blocks = c.getCarver().carve(chunk.getX(), chunk.getZ(), world).getCarvedBlocks();
            for(Map.Entry<Vector, CarvingData.CarvingType> e : blocks.entrySet()) {
                Vector v = e.getKey();
                Block b = chunk.getBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                Material m = b.getType();
                boolean liquid = m.equals(Material.WATER) || m.equals(Material.LAVA);
                if(e.getValue().equals(CarvingData.CarvingType.CENTER) && c.isReplaceableInner(m)) {
                    if(c.getShiftedBlocks().containsKey(b.getType())) shiftCandidate.put(b.getLocation(), b.getType());
                    b.setBlockData(c.getPaletteInner(v.getBlockY()).get(random), liquid);
                } else if(c.isReplaceableOuter(m)){
                    if(c.getShiftedBlocks().containsKey(b.getType())) shiftCandidate.put(b.getLocation(), b.getType());
                    b.setBlockData(c.getPaletteOuter(v.getBlockY()).get(random), liquid);
                } else if(liquid) {
                    b.setBlockData(b.getBlockData(), true);
                }
            }
            int i = 0;
            int j = 0;
            for(Location l : shiftCandidate.keySet()) {
                Location mut = l.clone();
                Material orig = l.getBlock().getType();
                do mut.subtract(0, 1, 0);
                while(mut.getBlock().getType().equals(orig));
                try {
                    if(c.getShiftedBlocks().get(shiftCandidate.get(l)).contains(mut.getBlock().getType())) {
                        mut.getBlock().setType(shiftCandidate.get(l));
                        j++;
                    }
                } catch(NullPointerException ignored) {}
                i++;
            }
            if(i > 0) System.out.println("Shifted " + i + " blocks. " + j + " successful shifts.");
        }

        if(cave != null) cave.complete();
    }
}
