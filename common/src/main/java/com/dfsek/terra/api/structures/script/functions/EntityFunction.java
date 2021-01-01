package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.entity.EntityType;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.constants.ConstantExpression;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.RotationUtil;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedEntity;
import com.dfsek.terra.api.structures.tokenizer.Position;
import net.jafama.FastMath;

import java.util.Random;

public class EntityFunction implements Function<Void> {
    private final EntityType data;
    private final Returnable<Number> x, y, z;
    private final Position position;

    public EntityFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, TerraPlugin main, Position position) throws ParseException {
        this.position = position;
        if(!(data instanceof ConstantExpression)) throw new ParseException("Entity data must be constant", data.getPosition());

        this.data = main.getWorldHandle().getEntity(((ConstantExpression<String>) data).getConstant());
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Void apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        Vector2 xz = new Vector2(x.apply(buffer, rotation, random, recursions).doubleValue(), z.apply(buffer, rotation, random, recursions).doubleValue());

        RotationUtil.rotateVector(xz, rotation);

        buffer.addItem(new BufferedEntity(data), new Vector3(FastMath.roundToInt(xz.getX()), y.apply(buffer, rotation, random, recursions).intValue(), FastMath.roundToInt(xz.getZ())));
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
