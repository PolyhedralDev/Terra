package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.constants.ConstantExpression;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.tokenizer.Position;
import net.jafama.FastMath;

public class BlockFunction implements Function<Void> {
    private final BlockData data;
    private final Returnable<Number> x, y, z;
    private final Position position;

    public BlockFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, TerraPlugin main, Position position) throws ParseException {
        this.position = position;
        if(!(data instanceof ConstantExpression)) throw new ParseException("Block data must be constant.");

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
    public Void apply(Location location, Rotation rotation, int recursions) {
        Vector2 xz = new Vector2(x.apply(location, rotation, recursions).doubleValue(), z.apply(location, rotation, recursions).doubleValue());

        RotationUtil.rotateVector(xz, rotation);

        location.clone().add(FastMath.roundToInt(xz.getX()), y.apply(location, rotation, recursions).intValue(), FastMath.roundToInt(xz.getZ())).getBlock().setBlockData(data, false);
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
