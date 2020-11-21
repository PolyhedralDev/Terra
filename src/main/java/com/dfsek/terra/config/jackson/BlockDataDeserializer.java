package com.dfsek.terra.config.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import java.io.IOException;

public class BlockDataDeserializer extends FromStringDeserializer<BlockData> {
    protected BlockDataDeserializer() {
        super(BlockData.class);
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected BlockData _deserialize(String value, DeserializationContext ctxt) throws IOException {
        return Bukkit.createBlockData(value);
    }
}
