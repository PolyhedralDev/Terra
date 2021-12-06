/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class BorderMutator implements BiomeMutator {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<BiomeDelegate> replace;
    private final String replaceTag;
    
    public BorderMutator(String border, String replaceTag, NoiseSampler noiseSampler, ProbabilityCollection<BiomeDelegate> replace) {
        this.border = border;
        this.noiseSampler = noiseSampler;
        this.replace = replace;
        this.replaceTag = replaceTag;
    }
    
    @Override
    public BiomeDelegate mutate(ViewPoint viewPoint, double x, double z, long seed) {
        BiomeDelegate origin = viewPoint.getBiome(0, 0);
        if(origin.getTags().contains(replaceTag)) {
            for(int xi = -1; xi <= 1; xi++) {
                for(int zi = -1; zi <= 1; zi++) {
                    if(xi == 0 && zi == 0) continue;
                    BiomeDelegate current = viewPoint.getBiome(xi, zi);
                    if(current == null) continue;
                    if(current.getTags().contains(border)) {
                        BiomeDelegate biome = replace.get(noiseSampler, x, z, seed);
                        return biome == null ? origin : biome;
                    }
                }
            }
        }
        return origin;
    }
    
    @Override
    public Iterable<BiomeDelegate> getBiomes(Iterable<BiomeDelegate> biomes) {
        Set<BiomeDelegate> biomeSet = new HashSet<>();
        biomes.forEach(biomeSet::add);
        biomeSet.addAll(replace.getContents().stream().filter(Objects::nonNull).toList());
        return biomeSet;
    }
}
