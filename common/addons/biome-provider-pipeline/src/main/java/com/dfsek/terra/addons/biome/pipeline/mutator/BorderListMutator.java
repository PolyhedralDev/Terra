/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.mutator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class BorderListMutator implements BiomeMutator {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<BiomeDelegate> replaceDefault;
    private final String defaultReplace;
    private final Map<BiomeDelegate, ProbabilityCollection<BiomeDelegate>> replace;
    
    public BorderListMutator(Map<BiomeDelegate, ProbabilityCollection<BiomeDelegate>> replace, String border, String defaultReplace,
                             NoiseSampler noiseSampler, ProbabilityCollection<BiomeDelegate> replaceDefault) {
        this.border = border;
        this.noiseSampler = noiseSampler;
        this.replaceDefault = replaceDefault;
        this.defaultReplace = defaultReplace;
        this.replace = replace;
    }
    
    @Override
    public BiomeDelegate mutate(ViewPoint viewPoint, double x, double z, long seed) {
        BiomeDelegate origin = viewPoint.getBiome(0, 0);
        if(origin.getTags().contains(defaultReplace)) {
            for(int xi = -1; xi <= 1; xi++) {
                for(int zi = -1; zi <= 1; zi++) {
                    if(xi == 0 && zi == 0) continue;
                    BiomeDelegate current = viewPoint.getBiome(xi, zi);
                    if(current != null && current.getTags().contains(border)) {
                        if(replace.containsKey(origin)) {
                            BiomeDelegate biome = replace.get(origin).get(noiseSampler, x, z, seed);
                            return biome.isSelf() ? origin : biome;
                        }
                        BiomeDelegate biome = replaceDefault.get(noiseSampler, x, z, seed);
                        return biome.isSelf() ? origin : biome;
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
        biomeSet.addAll(replaceDefault.getContents().stream().filter(Predicate.not(BiomeDelegate::isSelf)).toList());
        replace.forEach((biome, collection) -> biomeSet.addAll(collection.getContents()));
        return biomeSet;
    }
}
