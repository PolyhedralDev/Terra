package org.allaymc.terra.allay.delegate;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.properties.Property;

import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.terra.allay.JeBlockState;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayBlockState(BlockState allayBlockState, JeBlockState jeBlockState) implements com.dfsek.terra.api.block.state.BlockState {

    public static final AllayBlockState AIR = new AllayBlockState(BlockTypes.AIR_TYPE.getDefaultState(), JeBlockState.fromString("minecraft:air"));

    @Override
    public boolean matches(com.dfsek.terra.api.block.state.BlockState other) {
        return ((AllayBlockState) other).allayBlockState == this.allayBlockState;
    }

    @Override
    public <T extends Comparable<T>> boolean has(Property<T> property) {
        // TODO
        return false;
    }

    @Override
    public <T extends Comparable<T>> T get(Property<T> property) {
        // TODO
        return null;
    }

    @Override
    public <T extends Comparable<T>> com.dfsek.terra.api.block.state.BlockState set(Property<T> property, T value) {
        // TODO
        return null;
    }

    @Override
    public BlockType getBlockType() {
        return new AllayBlockType(allayBlockState.getBlockType());
    }

    @Override
    public String getAsString(boolean properties) {
        return jeBlockState.toString(properties);
    }

    @Override
    public boolean isAir() {
        return allayBlockState.getBlockType() == BlockTypes.AIR_TYPE;
    }

    @Override
    public BlockState getHandle() {
        return allayBlockState;
    }
}
