package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.functions.RecursionsFunction;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;

public class RecursionsFunctionBuilder implements FunctionBuilder<RecursionsFunction> {
    @Override
    public RecursionsFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        return new RecursionsFunction(position);
    }

    @Override
    public int argNumber() {
        return 0;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        return null;
    }
}
