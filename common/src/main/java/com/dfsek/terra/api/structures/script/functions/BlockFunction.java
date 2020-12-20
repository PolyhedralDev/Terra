package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Function;

public class BlockFunction implements Function<Void> {
    private final BlockData data;
    private final int x, y, z;

    public BlockFunction(int x, int y, int z, BlockData data) {
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String name() {
        return "block";
    }

    @Override
    public Void apply(Location location) {
        location.clone().add(x, y, z).getBlock().setBlockData(data, false);
        return null;
    }

    @Override
    public Void apply(Location location, Chunk chunk) {
        //TODO: do
        return null;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
}
