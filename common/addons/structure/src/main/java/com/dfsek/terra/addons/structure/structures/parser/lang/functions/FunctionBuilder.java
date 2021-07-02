package com.dfsek.terra.addons.structure.structures.parser.lang.functions;

import com.dfsek.terra.addons.structure.structures.parser.exceptions.ParseException;
import com.dfsek.terra.addons.structure.structures.parser.lang.Returnable;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

import java.util.List;

public interface FunctionBuilder<T extends Function<?>> {
    T build(List<Returnable<?>> argumentList, Position position) throws ParseException;

    int argNumber();

    Returnable.ReturnType getArgument(int position);
}
