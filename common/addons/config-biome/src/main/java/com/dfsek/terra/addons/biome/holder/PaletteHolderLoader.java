/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.holder;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class PaletteHolderLoader implements TypeLoader<PaletteHolder> {
    @SuppressWarnings("unchecked")
    @Override
    public PaletteHolder load(@NotNull AnnotatedType type, @NotNull Object o, @NotNull ConfigLoader configLoader, DepthTracker depthTracker)
    throws LoadException {
        List<Map<String, Integer>> palette = (List<Map<String, Integer>>) o;
        PaletteHolderBuilder builder = new PaletteHolderBuilder();
        for(Map<String, Integer> layer : palette) {
            for(Map.Entry<String, Integer> entry : layer.entrySet()) {
                builder.add(entry.getValue(), configLoader.loadType(Palette.class, entry.getKey(), depthTracker));
            }
        }
        return builder.build();
    }
}
