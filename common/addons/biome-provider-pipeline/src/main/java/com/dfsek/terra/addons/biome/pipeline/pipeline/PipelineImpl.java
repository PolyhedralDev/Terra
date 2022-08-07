package com.dfsek.terra.addons.biome.pipeline.pipeline;

import java.util.List;

import com.dfsek.terra.addons.biome.pipeline.api.SeededVector;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeChunk;
import com.dfsek.terra.addons.biome.pipeline.api.Expander;
import com.dfsek.terra.addons.biome.pipeline.api.Pipeline;
import com.dfsek.terra.addons.biome.pipeline.api.Source;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;


public class PipelineImpl implements Pipeline {
    
//    private static final Logger logger = LoggerFactory.getLogger(PipelineImpl.class);
    
    private final Source source;
    private final List<Stage> stages;
    private final int chunkSize;
    private final int expanderCount;
    private final int arraySize;
    private final int chunkOriginArrayIndex;
    private final int resolution;
    
    public PipelineImpl(Source source, List<Stage> stages, int resolution, int idealChunkArraySize) {
        this.source = source;
        this.stages = stages;
        this.resolution = resolution;
        this.expanderCount = (int) stages.stream().filter(s -> s instanceof Expander).count();
        
        // Optimize for the ideal array size
        int arraySize;
        int chunkOriginArrayIndex;
        int chunkSize;
        int initialSize = 1;
        while (true) {
            arraySize = BiomeChunkImpl.initialSizeToArraySize(expanderCount, initialSize);
            chunkOriginArrayIndex = BiomeChunkImpl.calculateChunkOriginArrayIndex(expanderCount, stages);
            chunkSize = BiomeChunkImpl.calculateChunkSize(arraySize, chunkOriginArrayIndex, expanderCount);
            if (chunkSize > 1 && arraySize >= idealChunkArraySize) break;
            initialSize++;
        }
        
        this.arraySize = arraySize;
        this.chunkOriginArrayIndex = chunkOriginArrayIndex;
        this.chunkSize = chunkSize;
    
        System.out.println("Initialized a new biome pipeline:");
        System.out.println("Array size: " + arraySize + " (Target: " + idealChunkArraySize + ")");
        System.out.println("Internal array origin: " + chunkOriginArrayIndex);
        System.out.println("Chunk size: " + chunkSize);
    }
    
    @Override
    public BiomeChunk generateChunk(SeededVector worldCoordinates) {
        return new BiomeChunkImpl(worldCoordinates, this);
    }
    
    @Override
    public int getChunkSize() {
        return chunkSize;
    }
    
    @Override
    public Source getSource() {
        return source;
    }
    
    @Override
    public List<Stage> getStages() {
        return stages;
    }
    
    protected int getExpanderCount() {
        return expanderCount;
    }
    
    protected int getArraySize() {
        return arraySize;
    }
    
    protected int getChunkOriginArrayIndex() {
        return chunkOriginArrayIndex;
    }
    
    protected int getResolution() {
        return resolution;
    }
}
