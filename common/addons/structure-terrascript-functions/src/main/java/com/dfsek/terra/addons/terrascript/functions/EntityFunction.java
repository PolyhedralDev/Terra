package com.dfsek.terra.addons.terrascript.functions;

import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.exception.ParseException;
import com.dfsek.terra.addons.terrascript.api.lang.ConstantExpression;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.api.buffer.items.BufferedEntity;
import com.dfsek.terra.addons.terrascript.api.TerraProperties;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;

import java.util.Map;

public class EntityFunction implements Function<Void> {
    private final EntityType data;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final TerraPlugin main;

    public EntityFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, TerraPlugin main, Position position) throws ParseException {
        this.position = position;
        this.main = main;
        if(!(data instanceof ConstantExpression)) throw new ParseException("Entity data must be constant", data.getPosition());

        this.data = main.getWorldHandle().getEntity(((ConstantExpression<String>) data).getConstant());
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Void apply(Context context, Map<String, Variable<?>> variableMap) {
        TerraProperties arguments = context.get(TerraProperties.class);
        Vector2 xz = new Vector2(x.apply(context, variableMap).doubleValue(), z.apply(context, variableMap).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());

        arguments.getBuffer().addItem(new BufferedEntity(data, main), new Vector3(xz.getX(), y.apply(context, variableMap).doubleValue(), xz.getZ()));
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
