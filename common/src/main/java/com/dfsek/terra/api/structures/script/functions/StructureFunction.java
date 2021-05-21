package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.structure.buffer.IntermediateBuffer;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.registry.config.ScriptRegistry;
import net.jafama.FastMath;

import java.util.List;
import java.util.Map;

public class StructureFunction implements Function<Boolean> {
    private final ScriptRegistry registry;
    private final Returnable<String> id;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final TerraPlugin main;
    private final List<Returnable<String>> rotations;

    public StructureFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> id, List<Returnable<String>> rotations, ScriptRegistry registry, Position position, TerraPlugin main) {
        this.registry = registry;
        this.id = id;
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
        this.main = main;
        this.rotations = rotations;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }

    @Override
    public Boolean apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;

        if(arguments.getRecursions() > main.getTerraConfig().getMaxRecursion())
            throw new RuntimeException("Structure recursion too deep: " + arguments.getRecursions());

        Vector2 xz = new Vector2(x.apply(implementationArguments, variableMap).doubleValue(), z.apply(implementationArguments, variableMap).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());

        String app = id.apply(implementationArguments, variableMap);
        StructureScript script = registry.get(app);
        if(script == null) {
            main.logger().severe("No such structure " + app);
            return null;
        }

        Rotation rotation1;
        String rotString = rotations.get(arguments.getRandom().nextInt(rotations.size())).apply(implementationArguments, variableMap);
        try {
            rotation1 = Rotation.valueOf(rotString);
        } catch(IllegalArgumentException e) {
            main.logger().severe("Invalid rotation " + rotString);
            return null;
        }

        Vector3 offset = new Vector3(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).doubleValue(), FastMath.roundToInt(xz.getZ()));

        return script.executeInBuffer(new IntermediateBuffer(arguments.getBuffer(), offset), arguments.getRandom(), arguments.getRotation().rotate(rotation1), arguments.getRecursions() + 1);
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
