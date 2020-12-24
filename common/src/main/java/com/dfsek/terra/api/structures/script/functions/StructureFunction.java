package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.structure.buffer.IntermediateBuffer;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.registry.ScriptRegistry;
import net.jafama.FastMath;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class StructureFunction implements Function<Void> {
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
    public String name() {
        return "structure";
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }

    @Override
    public Void apply(Buffer buffer, Rotation rotation, int recursions) {

        Vector2 xz = new Vector2(x.apply(buffer, rotation, recursions).doubleValue(), z.apply(buffer, rotation, recursions).doubleValue());

        RotationUtil.rotateVector(xz, rotation);

        StructureScript script = registry.get(id.apply(buffer, rotation, recursions));
        if(script == null) {
            main.getLogger().severe("No such structure " + id.apply(buffer, rotation, recursions));
            return null;
        }

        Rotation rotation1;
        String rotString = rotations.get(ThreadLocalRandom.current().nextInt(rotations.size())).apply(buffer, rotation, recursions);
        try {
            rotation1 = Rotation.valueOf(rotString);
        } catch(IllegalArgumentException e) {
            main.getLogger().severe("Invalid rotation " + rotString);
            return null;
        }

        Vector3 offset = new Vector3(FastMath.roundToInt(xz.getX()), y.apply(buffer, rotation, recursions).intValue(), FastMath.roundToInt(xz.getZ()));

        script.executeInBuffer(new IntermediateBuffer(buffer, offset), rotation.rotate(rotation1), recursions + 1);

        return null;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
