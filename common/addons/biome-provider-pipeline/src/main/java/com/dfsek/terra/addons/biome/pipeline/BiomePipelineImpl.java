package com.dfsek.terra.addons.biome.pipeline;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeHolder;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BiomePipelineImpl {
    private final BiomeSource source;
    private final List<Stage> stages;
    private final int size;
    private final int init;

    private BiomePipelineImpl(BiomeSource source, List<Stage> stages, int size, int init) {
        this.source = source;
        this.stages = stages;
        this.size = size;
        this.init = init;
    }

    /**
     * Get biomes in a chunk
     *
     * @param x Chunk X coord
     * @param z Chunk Z coord
     * @return BiomeHolder containing biomes.
     */
    public BiomeHolder getBiomes(int x, int z) {
        BiomeHolder holder = new BiomeHolderImpl(init, new Vector2(x * (init - 1), z * (init - 1)));
        holder.fill(source);
        for(Stage stage : stages) holder = stage.apply(holder);
        return holder;
    }

    public int getSize() {
        return size;
    }

    public static final class BiomePipelineBuilder {
        private final int init;
        List<StageSeeded> stages = new ArrayList<>();
        private int expand;

        public BiomePipelineBuilder(int init) {
            this.init = init;
            expand = init;
        }

        public BiomePipelineImpl build(BiomeSource source, long seed) {
            List<Stage> stagesBuilt = stages.stream().map(stageBuilder -> stageBuilder.build(seed)).collect(Collectors.toList());

            for(Stage stage : stagesBuilt) {
                if(stage.isExpansion()) expand = expand * 2 - 1;
            }

            return new BiomePipelineImpl(source, stagesBuilt, expand, init);
        }

        public BiomePipelineBuilder addStage(StageSeeded stage) {
            stages.add(stage);
            return this;
        }
    }
}
