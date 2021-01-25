package com.dfsek.terra.population.items.carving;

import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.ChunkAccess;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.carving.Carver;
import com.dfsek.terra.generation.math.interpolation.BiomeChunkInterpolator;
import com.dfsek.terra.generation.math.interpolation.ChunkInterpolator;

public class NoiseCarver implements Carver {
    private final Range range;
    private final BlockData data;
    private final TerraPlugin main;

    public NoiseCarver(Range range, BlockData data, TerraPlugin main) {
        this.range = range;
        this.data = data;
        this.main = main;
    }

    @Override
    public void carve(World world, int chunkX, int chunkZ, ChunkAccess chunk) {
        ChunkInterpolator interpolator = new BiomeChunkInterpolator(world, chunkX, chunkZ, main.getWorld(world).getBiomeProvider(), (gen, coord) -> gen.getCarver().getNoise(coord));
        for(int y : range) {
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    double n = interpolator.getNoise(x, y, z);
                    if(n > 0) chunk.setBlock(x, y, z, data);
                }
            }
        }
    }
}
