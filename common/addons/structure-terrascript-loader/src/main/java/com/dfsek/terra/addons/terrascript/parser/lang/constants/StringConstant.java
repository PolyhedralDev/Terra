/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.constants;

import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public class StringConstant extends ConstantExpression<String> {
    public StringConstant(String constant, SourcePosition position) {
        super(constant, position);
    }
    
    @Override
    public Expression.ReturnType returnType() {
        return Expression.ReturnType.STRING;
    }
}
