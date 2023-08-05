/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.keywords.flow;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block.EvaluationInfo;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block.EvaluationLevel;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class ContinueKeyword implements Keyword<EvaluationInfo<?>> {
    private final SourcePosition position;
    
    public ContinueKeyword(SourcePosition position) {
        this.position = position;
    }
    
    @Override
    public EvaluationInfo<?> evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return new EvaluationInfo<>(EvaluationLevel.CONTINUE, null);
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
    
    @Override
    public Type returnType() {
        return Type.VOID;
    }
}
