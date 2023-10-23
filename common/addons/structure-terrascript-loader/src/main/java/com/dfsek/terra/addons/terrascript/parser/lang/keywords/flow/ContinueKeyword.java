/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow;

import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class ContinueKeyword implements Keyword<Block.ReturnInfo<?>> {
    private final Position position;
    
    public ContinueKeyword(Position position) {
        this.position = position;
    }
    
    @Override
    public Block.ReturnInfo<?> apply(ImplementationArguments implementationArguments, Scope scope) {
        return new Block.ReturnInfo<>(Block.ReturnLevel.CONTINUE, null);
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
}
