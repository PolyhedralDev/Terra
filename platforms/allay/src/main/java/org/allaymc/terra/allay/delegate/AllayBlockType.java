package org.allaymc.terra.allay.delegate;

import org.allaymc.api.block.tag.BlockTags;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.terra.allay.Mapping;

import com.dfsek.terra.api.block.state.BlockState;

/**
 * @author daoge_cmd
 */
public record AllayBlockType(BlockType<?> allayBlockType) implements com.dfsek.terra.api.block.BlockType {
    @Override
    public BlockState getDefaultState() {
        return new AllayBlockState(allayBlockType.getDefaultState(), Mapping.blockStateBeToJe(allayBlockType.getDefaultState()));
    }

    @Override
    public boolean isSolid() {
        return allayBlockType.getMaterial().isSolid();
    }

    @Override
    public boolean isWater() {
        return allayBlockType.hasBlockTag(BlockTags.WATER);
    }

    @Override
    public BlockType<?> getHandle() {
        return allayBlockType;
    }
}
