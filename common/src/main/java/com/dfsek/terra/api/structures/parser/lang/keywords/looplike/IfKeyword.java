package com.dfsek.terra.api.structures.parser.lang.keywords.looplike;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

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
    public Block.ReturnInfo<?> apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        if(statement.apply(buffer, rotation, random, recursions)) return conditional.apply(buffer, rotation, random, recursions);
        else {
            for(Pair<Returnable<Boolean>, Block> pair : elseIf) {
                if(pair.getLeft().apply(buffer, rotation, random, recursions)) {
                    return pair.getRight().apply(buffer, rotation, random, recursions);
                }
            }
            if(elseBlock != null) return elseBlock.apply(buffer, rotation, random, recursions);
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
