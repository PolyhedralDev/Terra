package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.loot.LootTable;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedLootApplication;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.registry.LootRegistry;
import net.jafama.FastMath;

import java.util.Random;

public class LootFunction implements Function<Void> {
    private final LootRegistry registry;
    private final Returnable<String> data;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final TerraPlugin main;

    public LootFunction(LootRegistry registry, Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, TerraPlugin main, Position position) {
        this.registry = registry;
        this.position = position;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
        this.main = main;
    }

    @Override
    public String name() {
        return "block";
    }

    @Override
    public Void apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        Vector2 xz = new Vector2(x.apply(buffer, rotation, random, recursions).doubleValue(), z.apply(buffer, rotation, random, recursions).doubleValue());

        RotationUtil.rotateVector(xz, rotation);

        String id = data.apply(buffer, rotation, random, recursions);
        LootTable table = registry.get(id);

        if(table == null) {
            main.getLogger().severe("No such loot table " + id);
            return null;
        }

        buffer.addItem(new BufferedLootApplication(table, main), new Vector3(FastMath.roundToInt(xz.getX()), y.apply(buffer, rotation, random, recursions).intValue(), FastMath.roundToInt(xz.getZ())));
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
