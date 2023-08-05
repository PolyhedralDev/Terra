/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.keywords.looplike;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block.EvaluationLevel;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class ForKeyword implements Keyword<Block.EvaluationInfo<?>> {
    private final Block conditional;
    private final Expression<?> initializer;
    private final Expression<Boolean> statement;
    private final Expression<?> incrementer;
    private final SourcePosition position;
    
    public ForKeyword(Block conditional, Expression<?> initializer, Expression<Boolean> statement, Expression<?> incrementer,
                      SourcePosition position) {
        this.conditional = conditional;
        this.initializer = initializer;
        this.statement = statement;
        this.incrementer = incrementer;
        this.position = position;
    }
    
    @Override
    public Block.EvaluationInfo<?> evaluate(ImplementationArguments implementationArguments, Scope scope) {
        for(initializer.evaluate(implementationArguments, scope);
            statement.evaluate(implementationArguments, scope);
            incrementer.evaluate(implementationArguments, scope)) {
            Block.EvaluationInfo<?> level = conditional.evaluate(implementationArguments, scope);
            if(level.level().equals(EvaluationLevel.BREAK)) break;
            if(level.level().isReturnFast()) return level;
        }
        return new Block.EvaluationInfo<>(EvaluationLevel.NONE, null);
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
