package com.dfsek.terra.api.structures.parser.lang.functions;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;

public interface FunctionBuilder<T extends Function<?>> {
    T build(List<Returnable<?>> argumentList, Position position) throws ParseException;

    int argNumber();

    Returnable.ReturnType getArgument(int position);
}
