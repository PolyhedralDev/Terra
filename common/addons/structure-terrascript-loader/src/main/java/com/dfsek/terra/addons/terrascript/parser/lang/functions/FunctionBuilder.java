/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.functions;

import java.util.List;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;


public interface FunctionBuilder<T extends Function<?>> {
    T build(List<Expression<?>> argumentList, SourcePosition position);
    
    int argNumber();
    
    Expression.ReturnType getArgument(int position);
}
