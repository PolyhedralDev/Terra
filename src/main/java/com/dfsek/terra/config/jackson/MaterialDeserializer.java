package com.dfsek.terra.config.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.IOException;
import java.util.Objects;

public class MaterialDeserializer extends StdDeserializer<Material> {
    protected MaterialDeserializer(Class<Material> vc) {
        super(vc);
    }

    protected MaterialDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected MaterialDeserializer(StdDeserializer<Material> src) {
        super(src);
    }

    @Override
    public Material deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            return Bukkit.createBlockData(Objects.requireNonNull(p.getValueAsString())).getMaterial();
        } catch(IllegalArgumentException e) {
            throw new JsonParseException(p, "This is not a valid block.", e);
        }
    }
}
