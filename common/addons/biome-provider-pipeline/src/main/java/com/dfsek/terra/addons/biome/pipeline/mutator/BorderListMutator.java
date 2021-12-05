/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.mutator;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.Biome;


public class BorderListMutator implements BiomeMutator {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<Biome> replaceDefault;
    private final String defaultReplace;
    private final Map<Biome, ProbabilityCollection<Biome>> replace;
    
    public BorderListMutator(Map<Biome, ProbabilityCollection<Biome>> replace, String border, String defaultReplace,
                             NoiseSampler noiseSampler, ProbabilityCollection<Biome> replaceDefault) {
        this.border = border;
        this.noiseSampler = noiseSampler;
        this.replaceDefault = replaceDefault;
        this.defaultReplace = defaultReplace;
        this.replace = replace;
    }
    
    @Override
    public Biome mutate(ViewPoint viewPoint, double x, double z, long seed) {
        Biome origin = viewPoint.getBiome(0, 0);
        if(origin.getTags().contains(defaultReplace)) {
            for(int xi = -1; xi <= 1; xi++) {
                for(int zi = -1; zi <= 1; zi++) {
                    if(xi == 0 && zi == 0) continue;
                    Biome current = viewPoint.getBiome(xi, zi);
                    if(current == null) continue;
                    if(current.getTags().contains(border)) {
                        if(replace.containsKey(origin)) {
                            Biome biome = replace.get(origin).get(noiseSampler, x, z, seed);
                            return biome == null ? origin : biome;
                        }
                        Biome biome = replaceDefault.get(noiseSampler, x, z, seed);
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
        biomeSet.addAll(replaceDefault.getContents().stream().filter(Objects::nonNull).toList());
        replace.forEach((biome, collection) -> biomeSet.addAll(collection.getContents()));
        return biomeSet;
    }
}
