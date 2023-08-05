/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions;

import java.util.List;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public interface FunctionBuilder<T extends Function<?>> {
    T build(List<Expression<?>> argumentList, SourcePosition position);
    
    /**
     * @return Number of function parameters, -1 if the function uses a vararg at the end
     */
    int argNumber();
    
    Type getArgument(int position);
}
