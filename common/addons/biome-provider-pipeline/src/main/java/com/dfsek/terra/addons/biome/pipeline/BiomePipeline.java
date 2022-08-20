/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeHolder;
import com.dfsek.terra.addons.biome.pipeline.api.stage.Stage;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;


public class BiomePipeline {
    private final BiomeSource source;
    private final List<Stage> stages;
    private final int size;
    private final int init;
    private final int resolution;
    
    private BiomePipeline(BiomeSource source, List<Stage> stages, int size, int init, int resolution) {
        this.source = source;
        this.stages = stages;
        this.size = size;
        this.init = init;
        this.resolution = resolution;
    }
    
    /**
     * Get biomes in a chunk
     *
     * @param x Chunk X coord
     * @param z Chunk Z coord
     *
     * @return BiomeHolder containing biomes.
     */
    public BiomeHolder getBiomes(int x, int z, long seed) {
        x *= size;
        z *= size;
        BiomeHolder holder = new BiomeHolderImpl(init, size, resolution);
        holder.fill(source, x, z, seed);
        for(Stage stage : stages) {
            holder = stage.apply(holder, x, z, seed);
        }
        return holder;
    }
    
    public BiomeSource getSource() {
        return source;
    }
    
    public List<Stage> getStages() {
        return Collections.unmodifiableList(stages);
    }
    
    public int getSize() {
        return size;
    }
    
    public static final class BiomePipelineBuilder {
        private final int init;
        private final List<Stage> stages = new ArrayList<>();
        private int expand;
        private final int resolution;
        
        public BiomePipelineBuilder(int init, int resolution) {
            this.init = init;
            expand = init;
            this.resolution = resolution;
        }
        
        public BiomePipeline build(BiomeSource source) {
            for(Stage stage : stages) {
                if(stage.isExpansion()) expand = expand * 2 - 1;
            }
            
            return new BiomePipeline(source, stages, expand, init, resolution);
        }
        
        public BiomePipelineBuilder addStage(Stage stage) {
            stages.add(stage);
            return this;
        }
    }
}
