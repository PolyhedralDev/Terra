package com.dfsek.terra.population;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.config.CarverConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.Vector;
import org.polydev.gaea.generation.GenerationPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.world.carving.CarvingData;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class CavePopulator extends GenerationPopulator {

    @Override
    public ChunkGenerator.ChunkData populate(World world, ChunkGenerator.ChunkData chunk, Random random, int chunkX, int chunkZ) {
        for(CarverConfig c : CarverConfig.getCarvers()) {
            ProfileFuture cave = TerraProfiler.fromWorld(world).measure("CaveTime");
            Map<Vector, CarvingData.CarvingType> blocks = c.getCarver().carve(chunkX, chunkZ, world).getCarvedBlocks();
            for(Map.Entry<Vector, CarvingData.CarvingType> e : blocks.entrySet()) {
                Vector v = e.getKey();
                Material m = chunk.getType(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                if(e.getValue().equals(CarvingData.CarvingType.CENTER) && c.isReplaceableInner(m)) {
                    chunk.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), Material.AIR);
                } else if(c.isReplaceableOuter(m)){
                    chunk.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), Material.ANDESITE);
                }
                
            }
            if(cave != null) cave.complete();
        }
        return chunk;
    }
}
