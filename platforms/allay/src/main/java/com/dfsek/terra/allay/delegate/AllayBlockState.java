package com.dfsek.terra.allay.delegate;

import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockTypes;
import com.dfsek.terra.allay.JeBlockState;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.properties.Property;

/**
 * @author daoge_cmd
 */
public final class AllayBlockState implements com.dfsek.terra.api.block.state.BlockState {

    public static final AllayBlockState AIR = new AllayBlockState(BlockTypes.AIR.getDefaultState(),
        JeBlockState.fromString("minecraft:air"));

    private final BlockState allayBlockState;
    private final JeBlockState jeBlockState;
    private final boolean containsWater;

    public AllayBlockState(BlockState allayBlockState, JeBlockState jeBlockState) {
        this.allayBlockState = allayBlockState;
        this.jeBlockState = jeBlockState;
        this.containsWater = "true".equals(jeBlockState.getPropertyValue("waterlogged"));
    }

    @Override
    public boolean matches(com.dfsek.terra.api.block.state.BlockState o) {
        var other = ((AllayBlockState) o);
        return other.allayBlockState == this.allayBlockState && other.containsWater == this.containsWater;
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
        return allayBlockState.getBlockType() == BlockTypes.AIR;
    }

    @Override
    public BlockState getHandle() {
        return allayBlockState;
    }

    public BlockState allayBlockState() { return allayBlockState; }

    public boolean containsWater() { return containsWater; }

    public JeBlockState jeBlockState()  { return jeBlockState; }
}
