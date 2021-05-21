package com.dfsek.terra.world.carving;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.ChunkAccess;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.world.Carver;
import com.dfsek.terra.world.generation.math.interpolation.ChunkInterpolator;
import com.dfsek.terra.world.generation.math.interpolation.ChunkInterpolator3D;

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
        ChunkInterpolator interpolator = new ChunkInterpolator3D(world, chunkX, chunkZ, main.getWorld(world).getBiomeProvider(), (gen, coord) -> gen.getCarver().getNoise(coord.setY(coord.getY())));
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
