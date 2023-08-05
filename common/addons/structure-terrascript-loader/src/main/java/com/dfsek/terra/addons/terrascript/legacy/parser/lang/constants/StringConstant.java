/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.constants;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class StringConstant extends ConstantExpression<String> {
    public StringConstant(String constant, SourcePosition position) {
        super(constant, position);
    }
    
    @Override
    public Type returnType() {
        return Type.STRING;
    }
}
