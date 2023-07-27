package com.dfsek.terra.addons.terrascript.parser.lang.functions;

import com.dfsek.terra.addons.terrascript.parser.lang.Expression.ReturnType;
import com.dfsek.terra.api.util.generic.pair.Pair;


public record FunctionSignature(ReturnType returnType, Pair<String, ReturnType>[] arguments) {
}
