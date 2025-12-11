package com.dfsek.terra.minestom.block;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import net.minestom.server.instance.block.Block;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.Property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public record MinestomBlockState(Block block) implements BlockState {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinestomBlockState.class);
    public static final MinestomBlockState AIR = new MinestomBlockState(Block.AIR);
    private static final TagStringIO tagStringIO = TagStringIO.tagStringIO();

    public MinestomBlockState {
        block = Objects.requireNonNullElse(block, Block.AIR);
    }

    public static MinestomBlockState fromStateId(String data) {
        CompoundBinaryTag nbt = CompoundBinaryTag.empty();
        int splitIndex = data.indexOf('{');
        if(splitIndex != -1) {
            String fullId = data;
            data = data.substring(0, splitIndex);
            String dataString = fullId.substring(splitIndex);
            try {
                nbt = tagStringIO.asCompound(dataString);
            } catch(IOException exception) {
                LOGGER.warn("Invalid entity data, will be ignored: {}", dataString);
            }
        }

        int openBracketIndex = data.indexOf('[');
        int closeBracketIndex = data.indexOf(']');

        if(openBracketIndex == -1 || closeBracketIndex == -1 || closeBracketIndex < openBracketIndex) {
            // no or invalid properties
            Block block = Block.fromKey(data);
            if(block != null && !nbt.isEmpty()) {
                block = block.withNbt(nbt);
            }
            return new MinestomBlockState(block);
        }

        String namespaceId = data.substring(0, openBracketIndex);
        String propertiesContent = data.substring(openBracketIndex + 1, closeBracketIndex);
        Block block = Block.fromKey(namespaceId);
        if (block == null) {
            LOGGER.error("Invalid block ID found during parsing: {}", namespaceId);
            return new MinestomBlockState(Block.AIR);
        }

        HashMap<String, String> propertiesMap = new HashMap<>();
        int current = 0;
        while (current < propertiesContent.length()) {
            int nextComma = propertiesContent.indexOf(',', current);
            String property;

            if (nextComma == -1) {
                property = propertiesContent.substring(current);
                current = propertiesContent.length();
            } else {
                property = propertiesContent.substring(current, nextComma);
                current = nextComma + 1;
            }

            int equalsIndex = property.indexOf('=');

            if (equalsIndex == -1) {
                LOGGER.warn("Invalid block property syntax (missing '=') in string: {}", property);
                continue;
            }

            String key = property.substring(0, equalsIndex).strip();
            String value = property.substring(equalsIndex + 1).strip();
            propertiesMap.put(key, value);
        }

        if(!nbt.isEmpty()) {
            block = block.withNbt(nbt);
        }

        return new MinestomBlockState(block.withProperties(propertiesMap));
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