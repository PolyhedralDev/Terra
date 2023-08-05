/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang;

import java.util.List;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Block.EvaluationInfo;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class Block implements Expression<EvaluationInfo<?>> {
    private final List<Expression<?>> items;
    private final SourcePosition position;
    private final Type returnType;
    
    public Block(List<Expression<?>> items, SourcePosition position, Type returnType) {
        this.items = items;
        this.position = position;
        this.returnType = returnType;
    }
    
    @Override
    public Type returnType() {
        return returnType;
    }
    
    @Override
    public EvaluationInfo<?> evaluate(ImplementationArguments implementationArguments, Scope scope) {
        for(Expression<?> item : items) {
            Object result = item.evaluate(implementationArguments, scope);
            if(result instanceof EvaluationInfo<?> evalInfo) {
                if(!evalInfo.level().equals(EvaluationLevel.NONE)) return evalInfo;
            }
        }
        return new EvaluationInfo<>(EvaluationLevel.NONE, NOOP);
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
    
    public enum EvaluationLevel {
        NONE(false),
        BREAK(false),
        CONTINUE(false),
        RETURN(true),
        FAIL(true);
        
        private final boolean returnFast;
        
        EvaluationLevel(boolean returnFast) {
            this.returnFast = returnFast;
        }
        
        public boolean isReturnFast() {
            return returnFast;
        }
    }
    
    
    public record EvaluationInfo<T extends Expression<?>>(EvaluationLevel level, T data) {
    }
}
