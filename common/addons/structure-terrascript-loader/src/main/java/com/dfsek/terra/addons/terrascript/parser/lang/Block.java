/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang;

import java.util.List;

import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class Block implements Item<Block.ReturnInfo<?>> {
    private final List<Item<?>> items;
    private final Position position;
    
    public Block(List<Item<?>> items, Position position) {
        this.items = items;
        this.position = position;
    }
    
    @Override
    public ReturnInfo<?> apply(ImplementationArguments implementationArguments, Scope scope) {
        for(Item<?> item : items) {
            Object result = item.apply(implementationArguments, scope);
            if(result instanceof ReturnInfo<?> level) {
                if(!level.getLevel().equals(ReturnLevel.NONE)) return level;
            }
        }
        return new ReturnInfo<>(ReturnLevel.NONE, null);
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
    
    public enum ReturnLevel {
        NONE(false),
        BREAK(false),
        CONTINUE(false),
        RETURN(true),
        FAIL(true);
        
        private final boolean returnFast;
        
        ReturnLevel(boolean returnFast) {
            this.returnFast = returnFast;
        }
        
        public boolean isReturnFast() {
            return returnFast;
        }
    }
    
    
    public static class ReturnInfo<T> {
        private final ReturnLevel level;
        private final T data;
        
        public ReturnInfo(ReturnLevel level, T data) {
            this.level = level;
            this.data = data;
        }
        
        public ReturnLevel getLevel() {
            return level;
        }
        
        public T getData() {
            return data;
        }
    }
}
