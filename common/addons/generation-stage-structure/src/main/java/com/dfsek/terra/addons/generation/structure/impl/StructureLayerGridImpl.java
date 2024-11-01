package com.dfsek.terra.addons.generation.structure.impl;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.cache.SeededVector2Key;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.chunk.Chunk;

import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;


public class StructureLayerGridImpl implements StructureLayerGrid {

    private final StructureLayerGrid previous;
    private final Cache<SeededVector2Key, StructureCell> structureCells;

    private final int padding;
    private final int cellSize;

    protected ProbabilityCollection<Structure> getStructures() {
        return structures;
    }

    protected NoiseSampler getDistribution() {
        return distribution;
    }

    private final ProbabilityCollection<Structure> structures;
    private final NoiseSampler distribution;

    public StructureLayerGridImpl(StructureLayerGrid previous, int size, int padding, ProbabilityCollection<Structure> structures,
                                  NoiseSampler distribution) {
        this.previous = previous;
        this.padding = padding;
        this.structures = structures;
        this.distribution = distribution;
        this.cellSize = 1 + (size + padding) * 2;
        this.structureCells = Caffeine.newBuilder()
            .build();
    }

    @Override
    public Chunk getChunk(ProtoWorld world, int chunkX, int chunkZ, long seed) {
        int cellX = Math.floorMod(chunkX, cellSize);
        int cellZ = Math.floorMod(chunkZ, cellSize);
        SeededVector2Key lookupPos = new SeededVector2Key(cellX, cellZ, seed);
        StructureCell cell = structureCells.get(lookupPos, pos -> new StructureCell(this, world, pos));
        return cell.getChunk(chunkX, chunkZ);
    }

    protected int getGridSizeChunks() {
       return cellSize;
    }

    protected StructureLayerGrid getPrevious() {
        return previous;
    }

    protected int getPadding() {
        return padding;
    }
}
