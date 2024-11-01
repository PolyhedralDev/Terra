package com.dfsek.terra.addons.generation.structure.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public record StructureLayerGridDescription(int padding, int size, ProbabilityCollection<Structure> structures, NoiseSampler distribution) {
    public static class Template implements ObjectTemplate<StructureLayerGridDescription> {
        
        @Value("padding")
        private int padding;

        @Value("size")
        private int size;

        @Value("structures.structures")
        private ProbabilityCollection<Structure> structures;

        @Value("structures.distribution")
        private NoiseSampler distribution;

        @Override
        public StructureLayerGridDescription get() {
            return new StructureLayerGridDescription(padding, size, structures, distribution);
        }
    }
}
