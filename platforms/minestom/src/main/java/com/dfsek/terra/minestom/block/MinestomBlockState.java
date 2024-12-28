package com.dfsek.terra.minestom.block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;

import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.stream.Collectors;


public class MinestomBlockState implements BlockState {
    private final Block block;

    public MinestomBlockState(Block block) {
        if (block == null) {
            this.block = Block.AIR;
        } else {
            this.block = block;
        }
    }
    
    public MinestomBlockState(String data) {
        if (!data.contains("[")) {
            block = Block.fromNamespaceId(data);
            return;
        }
        
        String[] split = data.split("\\[");
        String namespaceId = split[0];
        String properties = split[1].substring(0, split[1].length() - 1);
        Block block = Block.fromNamespaceId(namespaceId);
        HashMap<String, String> propertiesMap = new HashMap<>();

        for (String property : properties.split(",")) {
            String[] kv = property.split("=");
            propertiesMap.put(kv[0].strip(), kv[1].strip());
        }

        assert block != null;
        this.block = block.withProperties(propertiesMap);
    }

    @Override
    public boolean matches(BlockState other) {
        return ((MinestomBlockState) other).block == block;
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
        String name = block.namespace().asString();
        if (!properties) {
            return name;
        }

        name += "[" + block
                    .properties()
                    .entrySet()
                    .stream()
                    .map(entry ->
                        entry.getKey() + "=" + entry.getValue()
                    )
                    .collect(Collectors.joining(",")) + "]";

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
}
