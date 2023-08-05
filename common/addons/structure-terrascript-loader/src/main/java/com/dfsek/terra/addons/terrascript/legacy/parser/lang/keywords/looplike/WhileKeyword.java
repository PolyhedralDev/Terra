/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.keywords.looplike;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block.EvaluationInfo;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block.EvaluationLevel;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class WhileKeyword implements Keyword<EvaluationInfo<?>> {
    private final Block conditional;
    private final Expression<Boolean> statement;
    private final SourcePosition position;
    
    public WhileKeyword(Block conditional, Expression<Boolean> statement, SourcePosition position) {
        this.conditional = conditional;
        this.statement = statement;
        this.position = position;
    }
    
    @Override
    public EvaluationInfo<?> evaluate(ImplementationArguments implementationArguments, Scope scope) {
        while(statement.evaluate(implementationArguments, scope)) {
            EvaluationInfo<?> level = conditional.evaluate(implementationArguments, scope);
            if(level.level().equals(EvaluationLevel.BREAK)) break;
            if(level.level().isReturnFast()) return level;
        }
        return new EvaluationInfo<>(EvaluationLevel.NONE, null);
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
