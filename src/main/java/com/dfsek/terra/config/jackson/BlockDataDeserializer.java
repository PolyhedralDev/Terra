package com.dfsek.terra.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bukkit.block.data.BlockData;

import java.io.IOException;

public class BlockDataDeserializer extends StdDeserializer<BlockData> {
    protected BlockDataDeserializer(Class<BlockData> vc) {
        super(vc);
    }

    protected BlockDataDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected BlockDataDeserializer(StdDeserializer<BlockData> src) {
        super(src);
    }

    @Override
    public BlockData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }
}
