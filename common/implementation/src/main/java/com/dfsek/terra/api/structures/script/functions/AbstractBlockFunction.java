package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.vector.Vector2Impl;
import com.dfsek.terra.vector.Vector3Impl;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedBlock;
import com.dfsek.terra.api.structures.tokenizer.Position;
import net.jafama.FastMath;

import java.util.Map;

public abstract class AbstractBlockFunction implements Function<Void> {
    protected final Returnable<Number> x, y, z;
    protected final Returnable<String> blockData;
    protected final TerraPlugin main;
    private final Returnable<Boolean> overwrite;
    private final Position position;

    protected AbstractBlockFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> blockData, Returnable<Boolean> overwrite, TerraPlugin main, Position position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockData = blockData;
        this.overwrite = overwrite;
        this.main = main;
        this.position = position;
    }

    void setBlock(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap, TerraImplementationArguments arguments, BlockData rot) {
        Vector2 xz = new Vector2Impl(x.apply(implementationArguments, variableMap).doubleValue(), z.apply(implementationArguments, variableMap).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());

        RotationUtil.rotateBlockData(rot, arguments.getRotation().inverse());
        arguments.getBuffer().addItem(new BufferedBlock(rot, overwrite.apply(implementationArguments, variableMap), main, arguments.isWaterlog()), new Vector3Impl(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).doubleValue(), FastMath.roundToInt(xz.getZ())).toLocation(arguments.getBuffer().getOrigin().getWorld()));
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
