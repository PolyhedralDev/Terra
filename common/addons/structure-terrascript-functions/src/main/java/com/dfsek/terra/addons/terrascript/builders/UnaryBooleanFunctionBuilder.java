package com.dfsek.terra.addons.terrascript.builders;

import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.api.TerraProperties;
import com.dfsek.terra.api.properties.Context;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class UnaryBooleanFunctionBuilder implements FunctionBuilder<Function<Void>> {

    private final BiConsumer<Boolean, TerraProperties> function;

    public UnaryBooleanFunctionBuilder(BiConsumer<Boolean, TerraProperties> function) {
        this.function = function;
    }

    @Override
    public Function<Void> build(List<Returnable<?>> argumentList, Position position) {
        return new Function<Void>() {
            @Override
            public ReturnType returnType() {
                return ReturnType.VOID;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Void apply(Context context, Map<String, Variable<?>> variableMap) {
                function.accept(((Returnable<Boolean>) argumentList.get(0)).apply(context, variableMap), context.get(TerraProperties.class));
                return null;
            }

            @Override
            public Position getPosition() {
                return position;
            }
        };
    }

    @Override
    public int argNumber() {
        return 1;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        if(position == 0) return Returnable.ReturnType.BOOLEAN;
        return null;
    }
}
