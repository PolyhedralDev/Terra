package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.EntityFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.TerraPlugin;


public class EntityFunctionBuilder implements FunctionBuilder<EntityFunction> {
    private final TerraPlugin main;
    
    public EntityFunctionBuilder(TerraPlugin main) {
        this.main = main;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public EntityFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        return new EntityFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1),
                                  (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), main, position);
    }
    
    @Override
    public int argNumber() {
        return 4;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        switch(position) {
            case 0:
            case 1:
            case 2:
                return Returnable.ReturnType.NUMBER;
            case 3:
                return Returnable.ReturnType.STRING;
            default:
                return null;
        }
    }
}
