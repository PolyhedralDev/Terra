package com.dfsek.terra.minecraft.attribute;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.timeline.Timeline;

import java.util.List;
import java.util.Objects;


public sealed interface TimelineRef {
    Object encoded();

    record Tag(TagKey<Timeline> key) implements TimelineRef {
        public Tag {
            Objects.requireNonNull(key, "key");
        }

        @Override
        public Object encoded() {
            return "#" + key.location();
        }
    }

    /**
     * Every key must be registered where the dimension type is decoded.
     */
    record Direct(List<ResourceKey<Timeline>> keys) implements TimelineRef {
        public Direct {
            keys = List.copyOf(keys);
        }

        @Override
        public Object encoded() {
            return keys.stream().map(key -> key.identifier().toString()).toList();
        }
    }
}
