package com.dfsek.terra.addons.terrascript.api;

import com.dfsek.terra.addons.terrascript.api.lang.Returnable;

import java.util.List;

public interface FunctionBuilder<T extends Function<?>> {
    T build(List<Returnable<?>> argumentList, Position position) throws ParseException;

    int argNumber();

    Returnable.ReturnType getArgument(int position);
}
