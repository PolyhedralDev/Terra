package com.dfsek.terra.addons.structure.structures.parser.lang.keywords.looplike;

import com.dfsek.terra.addons.structure.structures.parser.lang.Block;
import com.dfsek.terra.addons.structure.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.structure.structures.parser.lang.Keyword;
import com.dfsek.terra.addons.structure.structures.parser.lang.Returnable;
import com.dfsek.terra.addons.structure.structures.parser.lang.variables.Variable;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class IfKeyword implements Keyword<Block.ReturnInfo<?>> {
    private final Block conditional;
    private final Returnable<Boolean> statement;
    private final Position position;
    private final List<Pair<Returnable<Boolean>, Block>> elseIf;
    private final Block elseBlock;

    public IfKeyword(Block conditional, Returnable<Boolean> statement, List<Pair<Returnable<Boolean>, Block>> elseIf, @Nullable Block elseBlock, Position position) {
        this.conditional = conditional;
        this.statement = statement;
        this.position = position;
        this.elseIf = elseIf;
        this.elseBlock = elseBlock;
    }

    @Override
    public Block.ReturnInfo<?> apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        if(statement.apply(implementationArguments, variableMap)) return conditional.apply(implementationArguments, variableMap);
        else {
            for(Pair<Returnable<Boolean>, Block> pair : elseIf) {
                if(pair.getLeft().apply(implementationArguments, variableMap)) {
                    return pair.getRight().apply(implementationArguments, variableMap);
                }
            }
            if(elseBlock != null) return elseBlock.apply(implementationArguments, variableMap);
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

    public static class Pair<L, R> {
        private final L left;
        private final R right;

        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }
    }
}
