package com.dfsek.terra.minestom.block;

import net.minestom.server.instance.block.Block;

import java.util.Objects;
import java.util.stream.Collectors;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;


public class MinestomBlockState implements BlockState {
    private final Block block;

    public MinestomBlockState(Block block) {
        if(block == null) {
            this.block = Block.AIR;
        } else {
            this.block = block;
        }
    }

    public MinestomBlockState(String data) {
        block = Block.fromState(data);
        if (block == null) {
            throw new IllegalArgumentException("Invalid block state: " + data);
        }
    }

    @Override
    public boolean matches(BlockState other) {
        return ((MinestomBlockState) other).block.compare(block);
    }

    @Override
    public <T extends Comparable<T>> boolean has(Property<T> property) {
        return false;
    }

    @Override
    public <T extends Comparable<T>> T get(Property<T> property) {
        return null;
    }

    @Override
    public <T extends Comparable<T>> BlockState set(Property<T> property, T value) {
        return null;
    }

    @Override
    public BlockType getBlockType() {
        return new MinestomBlockType(block);
    }

    @Override
    public String getAsString(boolean properties) {
        String name = block.key().asString();
        if(!properties || block.properties().isEmpty()) {
            return name;
        }

        name += "[" + block.properties().entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(
            Collectors.joining(",")) + "]";

        return name;
    }

    @Override
    public boolean isAir() {
        return block.isAir();
    }

    @Override
    public Object getHandle() {
        return block;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(block.id());
    }
}
