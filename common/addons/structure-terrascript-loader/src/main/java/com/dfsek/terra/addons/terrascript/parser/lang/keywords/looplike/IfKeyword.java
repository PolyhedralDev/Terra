/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike;

import com.dfsek.terra.addons.terrascript.parser.lang.Block.EvaluationLevel;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class IfKeyword implements Keyword<Block.EvaluationInfo<?>> {
    private final Block conditional;
    private final Expression<Boolean> statement;
    private final SourcePosition position;
    private final List<Pair<Expression<Boolean>, Block>> elseIf;
    private final Block elseBlock;
    
    public IfKeyword(Block conditional, Expression<Boolean> statement, List<Pair<Expression<Boolean>, Block>> elseIf,
                     @Nullable Block elseBlock, SourcePosition position) {
        this.conditional = conditional;
        this.statement = statement;
        this.position = position;
        this.elseIf = elseIf;
        this.elseBlock = elseBlock;
    }
    
    @Override
    public Block.EvaluationInfo<?> evaluate(ImplementationArguments implementationArguments, Scope scope) {
        if(statement.evaluate(implementationArguments, scope)) return conditional.evaluate(implementationArguments, scope);
        else {
            for(Pair<Expression<Boolean>, Block> pair : elseIf) {
                if(pair.getLeft().evaluate(implementationArguments, scope)) {
                    return pair.getRight().evaluate(implementationArguments, scope);
                }
            }
            if(elseBlock != null) return elseBlock.evaluate(implementationArguments, scope);
        }
        return new Block.EvaluationInfo<>(EvaluationLevel.NONE, null);
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
