/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike;

import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.Block.EvaluationInfo;
import com.dfsek.terra.addons.terrascript.parser.lang.Block.EvaluationLevel;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


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
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
}
