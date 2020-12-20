package com.dfsek.terra.api.structures.parser;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Function;

import java.util.List;

public interface FunctionBuilder<T extends Function<?>> {
    T build(List<String> argumentList) throws ParseException;

    int getArguments();
}
