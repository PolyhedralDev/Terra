package com.dfsek.terra.addons.terrascript.functions;

import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.StructureScript;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.api.buffer.items.BufferedLootApplication;
import com.dfsek.terra.addons.terrascript.api.TerraProperties;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.properties.Context;
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
    public Void apply(Context context, Map<String, Variable<?>> variableMap) {
        TerraProperties arguments = context.get(TerraProperties.class);
        Vector2 xz = new Vector2(x.apply(context, variableMap).doubleValue(), z.apply(context, variableMap).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());

        String id = data.apply(context, variableMap);
        LootTable table = registry.get(id);

        if(table == null) {
            main.logger().severe("No such loot table " + id);
            return null;
        }

        arguments.getBuffer().addItem(new BufferedLootApplication(table, main, script), new Vector3(FastMath.roundToInt(xz.getX()), y.apply(context, variableMap).intValue(), FastMath.roundToInt(xz.getZ())));
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
