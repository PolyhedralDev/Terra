/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.keywords.flow;

import javax.annotation.Nullable;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block.EvaluationInfo;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block.EvaluationLevel;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class ReturnKeyword implements Keyword<EvaluationInfo<?>> {
    private final SourcePosition position;
    
    private final Expression<?> data;
    
    public ReturnKeyword(@Nullable Expression<?> data, SourcePosition position) {
        this.data = data;
        this.position = position;
    }
    
    @Override
    public EvaluationInfo<?> evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return new EvaluationInfo<>(EvaluationLevel.RETURN, data);
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
    
    @Override
    public Type returnType() {
        return Type.VOID;
    }
    
    public Type dataReturnType() {
        if(data != null) {
            return data.returnType();
        } else {
            return Type.VOID;
        }
    }
}
