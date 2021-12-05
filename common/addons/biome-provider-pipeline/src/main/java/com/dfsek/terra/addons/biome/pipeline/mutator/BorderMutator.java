/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class BorderMutator implements BiomeMutator {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<Biome> replace;
    private final String replaceTag;
    
    public BorderMutator(String border, String replaceTag, NoiseSampler noiseSampler, ProbabilityCollection<Biome> replace) {
        this.border = border;
        this.noiseSampler = noiseSampler;
        this.replace = replace;
        this.replaceTag = replaceTag;
    }
    
    @Override
    public Biome mutate(ViewPoint viewPoint, double x, double z, long seed) {
        Biome origin = viewPoint.getBiome(0, 0);
        if(origin.getTags().contains(replaceTag)) {
            for(int xi = -1; xi <= 1; xi++) {
                for(int zi = -1; zi <= 1; zi++) {
                    if(xi == 0 && zi == 0) continue;
                    Biome current = viewPoint.getBiome(xi, zi);
                    if(current == null) continue;
                    if(current.getTags().contains(border)) {
                        Biome biome = replace.get(noiseSampler, x, z, seed);
                        return biome == null ? origin : biome;
                    }
                }
            }
        }
        return origin;
    }
    
    @Override
    public Iterable<Biome> getBiomes(Iterable<Biome> biomes) {
        Set<Biome> biomeSet = new HashSet<>();
        biomes.forEach(biomeSet::add);
        biomeSet.addAll(replace.getContents().stream().filter(Objects::nonNull).toList());
        return biomeSet;
    }
}
