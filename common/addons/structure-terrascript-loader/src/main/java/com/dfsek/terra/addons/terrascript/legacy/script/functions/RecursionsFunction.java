/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.functions;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.legacy.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class RecursionsFunction implements Function<Number> {
    private final SourcePosition position;
    
    public RecursionsFunction(SourcePosition position) {
        this.position = position;
    }
    
    @Override
    public Type returnType() {
        return Type.NUMBER;
    }
    
    @Override
    public Number evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return ((TerraImplementationArguments) implementationArguments).getRecursions();
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
