package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.buffer.items.BufferedLootApplication;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;
import net.jafama.FastMath;

import java.util.Map;

public class LootFunction implements Function<Void> {
    private final Registry<LootTable> registry;
    private final Returnable<String> data;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final TerraPlugin main;
    private final StructureScript script;

    public LootFunction(Registry<LootTable> registry, Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, TerraPlugin main, Position position, StructureScript script) {
        this.registry = registry;
        this.position = position;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
        this.main = main;
        this.script = script;
    }

    @Override
    public Void apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = new Vector2(x.apply(implementationArguments, variableMap).doubleValue(), z.apply(implementationArguments, variableMap).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());

        String id = data.apply(implementationArguments, variableMap);
        LootTable table = registry.get(id);

        if(table == null) {
            main.logger().severe("No such loot table " + id);
            return null;
        }

        arguments.getBuffer().addItem(new BufferedLootApplication(table, main, script), new Vector3(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).intValue(), FastMath.roundToInt(xz.getZ())));
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
