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
import com.dfsek.terra.api.util.vector.Vector2Int;


public class BiomePipeline {
    private final BiomeSource source;
    private final List<Stage> stages;
    private final int size;
    private final int ratio;
    private final int maxArraySize;
    
    private BiomePipeline(BiomeSource source, List<Stage> stages, int size, int ratio, int maxArraySize) {
        this.source = source;
        this.stages = stages;
        this.size = size;
        this.ratio = ratio;
        this.maxArraySize = maxArraySize;
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
        BiomeHolder holder = new BiomeHolderImpl(maxArraySize, ratio, Vector2Int.of(x * size, z * size).mutable());
        holder.fill(source, seed);
        for(Stage stage : stages) {
            stage.apply(holder, seed);
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
        private final List<Stage> stages = new ArrayList<>();
        private final int finalSize;
        
        public BiomePipelineBuilder(int finalSize) {
            this.finalSize = finalSize;
        }
        
        public BiomePipeline build(BiomeSource source) {
            int init = 2;
            int ratio;
            int expand;
            int maxSize;
            while(true) {
                expand = init;
                for(Stage stage : stages) {
                    if(stage.isExpansion()) {
                        expand = expand * 2 - 1;
                    } else {
                        expand -= 2;
                    }
                    if(expand <= 0) {
                        break;
                    }
                }
                if(expand < finalSize) {
                    init++;
                    continue;
                }
                ratio = 1;
                maxSize = init;
                for(Stage stage : stages) {
                    if(stage.isExpansion()) {
                        ratio = ratio * 2;
                        maxSize = maxSize * 2 - 1;
                    }
                }
                break;
            }
            
            return new BiomePipeline(source, stages, expand, ratio, maxSize);
        }
        
        public BiomePipelineBuilder addStage(Stage stage) {
            stages.add(stage);
            return this;
        }
    }
}
