package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;

import com.dfsek.terra.api.world.biome.tag.BiomeTag;
import com.dfsek.terra.api.world.biome.tag.BiomeTagIndex;


public class BiomeTagLoader implements TypeLoader<BiomeTag> {
    private final BiomeTagIndex index;

    public BiomeTagLoader(BiomeTagIndex index) {
        this.index = index;
    }

    @Override
    public BiomeTag load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker)
    throws LoadException {
        if(!(c instanceof String name)) {
            throw new LoadException("expected a tag name, got " + describe(c), depthTracker);
        }
        return index.tag(name);
    }

    private static String describe(Object value) {
        return value == null ? "null" : "type " + value.getClass().getCanonicalName();
    }
}
