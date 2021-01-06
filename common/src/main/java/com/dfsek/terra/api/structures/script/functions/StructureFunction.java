package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.structure.buffer.IntermediateBuffer;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.registry.ScriptRegistry;
import net.jafama.FastMath;

import java.util.List;

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
    public Boolean apply(ImplementationArguments implementationArguments) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;

        Vector2 xz = new Vector2(x.apply(implementationArguments).doubleValue(), z.apply(implementationArguments).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());

        String app = id.apply(implementationArguments);
        StructureScript script = registry.get(app);
        if(script == null) {
            main.getLogger().severe("No such structure " + app);
            return null;
        }

        Rotation rotation1;
        String rotString = rotations.get(arguments.getRandom().nextInt(rotations.size())).apply(implementationArguments);
        try {
            rotation1 = Rotation.valueOf(rotString);
        } catch(IllegalArgumentException e) {
            main.getLogger().severe("Invalid rotation " + rotString);
            return null;
        }

        Vector3 offset = new Vector3(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments).doubleValue(), FastMath.roundToInt(xz.getZ()));

        return script.executeInBuffer(new IntermediateBuffer(arguments.getBuffer(), offset), arguments.getRandom(), arguments.getRotation().rotate(rotation1), arguments.getRecursions() + 1);
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
