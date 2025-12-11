package com.dfsek.terra.minestom.entity;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import net.minestom.server.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class MinestomEntityType implements com.dfsek.terra.api.entity.EntityType {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinestomEntityType.class);
    private static final TagStringIO tagStringIO = TagStringIO.tagStringIO();
    private final EntityType delegate;
    private final CompoundBinaryTag data;

    public MinestomEntityType(String id) {
        int splitIndex = id.indexOf('{');
        if(splitIndex != -1) {
            String fullId = id;
            id = id.substring(0, splitIndex);
            String dataString = fullId.substring(splitIndex);
            CompoundBinaryTag data;
            try {
                data = tagStringIO.asCompound(dataString);
            } catch(IOException exception) {
                LOGGER.warn("Invalid entity data, will be ignored: {}", dataString);
                data = CompoundBinaryTag.empty();
            }
            this.data = data;
        } else {
            this.data = CompoundBinaryTag.empty();
        }

        delegate = EntityType.fromKey(id);
    }

    @Override
    public EntityType getHandle() {
        return delegate;
    }

    public CompoundBinaryTag getData() {
        return data;
    }
}