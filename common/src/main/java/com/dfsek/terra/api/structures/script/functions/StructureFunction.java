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
import java.util.Random;

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
    public Boolean apply(Buffer buffer, Rotation rotation, Random random, int recursions) {

        Vector2 xz = new Vector2(x.apply(buffer, rotation, random, recursions).doubleValue(), z.apply(buffer, rotation, random, recursions).doubleValue());

        RotationUtil.rotateVector(xz, rotation);

        String app = id.apply(buffer, rotation, random, recursions);
        StructureScript script = registry.get(app);
        if(script == null) {
            main.getLogger().severe("No such structure " + app);
            return null;
        }

        Rotation rotation1;
        String rotString = rotations.get(random.nextInt(rotations.size())).apply(buffer, rotation, random, recursions);
        try {
            rotation1 = Rotation.valueOf(rotString);
        } catch(IllegalArgumentException e) {
            main.getLogger().severe("Invalid rotation " + rotString);
            return null;
        }

        Vector3 offset = new Vector3(FastMath.roundToInt(xz.getX()), y.apply(buffer, rotation, random, recursions).intValue(), FastMath.roundToInt(xz.getZ()));

        return script.executeInBuffer(new IntermediateBuffer(buffer, offset), random, rotation.rotate(rotation1), recursions + 1);
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
