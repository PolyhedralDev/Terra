package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.registry.ScriptRegistry;
import net.jafama.FastMath;

public class StructureFunction implements Function<Void> {
    private final ScriptRegistry registry;
    private final Returnable<String> id;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final TerraPlugin main;

    public StructureFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> id, ScriptRegistry registry, Position position, TerraPlugin main) {
        this.registry = registry;
        this.id = id;
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
        this.main = main;
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
    public Void apply(Location location, Rotation rotation) {
        System.out.println("executing structure function");

        Vector2 xz = new Vector2(x.apply(location, rotation).doubleValue(), z.apply(location, rotation).doubleValue());

        RotationUtil.rotateVector(xz, rotation);

        StructureScript script = registry.get(id.apply(location, rotation));
        if(script == null) {
            main.getLogger().severe("No such structure " + id.apply(location, rotation));
            return null;
        }

        Location location1 = location.clone().add(FastMath.roundToInt(xz.getX()), y.apply(location, rotation).intValue(), FastMath.roundToInt(xz.getZ()));

        script.execute(location1, rotation);

        return null;
    }

    @Override
    public Void apply(Location location, Chunk chunk, Rotation rotation) {
        return null;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
