package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.constants.ConstantExpression;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class BlockFunction implements Function<Void> {
    private final BlockData data;
    private final Returnable<Integer> x, y, z;
    private final Position position;

    public BlockFunction(Returnable<Integer> x, Returnable<Integer> y, Returnable<Integer> z, Returnable<String> data, TerraPlugin main, Position position) {
        this.position = position;
        if(!(data instanceof ConstantExpression)) throw new IllegalArgumentException("Block data must be constant.");

        this.data = main.getWorldHandle().createBlockData(((ConstantExpression<String>) data).getConstant());
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
        location.clone().add(x.apply(location), y.apply(location), z.apply(location)).getBlock().setBlockData(data, false);
        return null;
    }

    @Override
    public Void apply(Location location, Chunk chunk) {
        //TODO: do
        return null;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
}
