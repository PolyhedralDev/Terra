/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class IfKeyword implements Keyword<Block.ReturnInfo<?>> {
    private final Block conditional;
    private final Returnable<Boolean> statement;
    private final Position position;
    private final List<Pair<Returnable<Boolean>, Block>> elseIf;
    private final Block elseBlock;
    
    public IfKeyword(Block conditional, Returnable<Boolean> statement, List<Pair<Returnable<Boolean>, Block>> elseIf,
                     @Nullable Block elseBlock, Position position) {
        this.conditional = conditional;
        this.statement = statement;
        this.position = position;
        this.elseIf = elseIf;
        this.elseBlock = elseBlock;
    }
    
    @Override
    public Block.ReturnInfo<?> apply(ImplementationArguments implementationArguments, Scope scope) {
        if(statement.apply(implementationArguments, scope)) return conditional.apply(implementationArguments, scope);
        else {
            for(Pair<Returnable<Boolean>, Block> pair : elseIf) {
                if(pair.getLeft().apply(implementationArguments, scope)) {
                    return pair.getRight().apply(implementationArguments, scope);
                }
            }
            if(elseBlock != null) return elseBlock.apply(implementationArguments, scope);
        }
        return new Block.ReturnInfo<>(Block.ReturnLevel.NONE, null);
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
