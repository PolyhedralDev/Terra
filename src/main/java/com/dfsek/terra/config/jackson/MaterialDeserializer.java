package com.dfsek.terra.config.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.IOException;

public class MaterialDeserializer extends FromStringDeserializer<Material> {
    protected MaterialDeserializer() {
        super(Material.class);
    }

    @Override
    protected Material _deserialize(String value, DeserializationContext ctxt) throws IOException {
        try {
            return Bukkit.createBlockData(value).getMaterial();
        } catch(IllegalArgumentException e) {
            throw new JsonParseException(ctxt.getParser(), "This is not a valid block.", e);
        }
    }
}
