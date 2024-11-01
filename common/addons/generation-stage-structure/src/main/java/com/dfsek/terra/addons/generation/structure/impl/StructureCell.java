package com.dfsek.terra.addons.generation.structure.impl;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.cache.SeededVector2Key;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Represents a section of the world that contains at most a single structure within the structure layer
 */
public class StructureCell {

    private final Chunk[][] chunks;
    private final int size;
    private final int chunkOriginX;

    private final int chunkOriginZ;

    public StructureCell(StructureLayerGridImpl layer, ProtoWorld world, SeededVector2Key gridPos) {
        System.out.println("Generating structure cell");
        this.size = layer.getGridSizeChunks();

        this.chunkOriginX = gridPos.x * size;
        this.chunkOriginZ = gridPos.z * size;

        this.chunks = new Chunk[size][size];

        for (int x = 0; x < size; ++x) {
            for (int z = 0; z < size; ++z) {
                int chunkX = chunkOriginX + x;
                int chunkZ = chunkOriginZ + z;
                chunks[x][z] = new CopyOnWriteDelegatedChunk(layer.getPrevious().getChunk(world, chunkX, chunkZ, gridPos.seed));
            }
        }
        int minCoord = (chunkOriginX + layer.getPadding()) * 16;
        int maxCoord = (chunkOriginZ + layer.getGridSizeChunks() - layer.getPadding()) * 16;
        // TODO - Make these random within the range
        int structureX = (minCoord + maxCoord) / 2;
        int structureZ = (minCoord + maxCoord) / 2;
        int structureY = (world.getMinHeight() + world.getMaxHeight()) / 2;
        Vector3Int structureOrigin = Vector3Int.of(structureX, structureY, structureZ);

        Structure structure = layer.getStructures().get(layer.getDistribution(), structureOrigin, gridPos.seed);

        WorldView worldView = new WorldView(world);
        structure.generate(structureOrigin, worldView, null /* TODO */, Rotation.NONE /* TODO */);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        return getChunkLocal(chunkX - chunkOriginX, chunkZ - chunkOriginZ);
    }

    private Chunk getChunkLocal(int ix, int iz) {
        if (ix < 0 || ix >= size || iz < 0 || iz >= size)
            throw new IndexOutOfBoundsException("Attempted to retrieve chunk outside of structure cell bounds");
        return chunks[ix][iz];
    }

    class WorldView implements WritableWorld {

        private static final Logger logger = LoggerFactory.getLogger(WorldView.class);

        private final ProtoWorld worldDelegate;

        private WorldView(ProtoWorld world) {
            this.worldDelegate = world;
        }

        @Override
        public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            Chunk chunk = getChunkSafe(chunkX, chunkZ);
            int xInChunk = x - chunkX << 4;
            int zInChunk = z - chunkZ << 4;
            chunk.setBlock(xInChunk, y, zInChunk, data);
        }

        @Override
        public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
            return worldDelegate.spawnEntity(x, y, z, entityType);
        }

        @Override
        public BlockState getBlockState(int x, int y, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            Chunk chunk = getChunkSafe(chunkX, chunkZ);
            int xInChunk = x - chunkX << 4;
            int zInChunk = z - chunkZ << 4;
            return chunk.getBlock(xInChunk, y, zInChunk);
        }

        @Override
        public BlockEntity getBlockEntity(int x, int y, int z) {
            return worldDelegate.getBlockEntity(x, y, z);
        }

        @Override
        public ChunkGenerator getGenerator() {
            return worldDelegate.getGenerator();
        }

        @Override
        public BiomeProvider getBiomeProvider() {
            return worldDelegate.getBiomeProvider();
        }

        @Override
        public ConfigPack getPack() {
            return worldDelegate.getPack();
        }

        @Override
        public long getSeed() {
            return worldDelegate.getSeed();
        }

        @Override
        public int getMaxHeight() {
            return worldDelegate.getMaxHeight();
        }

        @Override
        public int getMinHeight() {
            return worldDelegate.getMinHeight();
        }

        @Override
        public Object getHandle() {
            return worldDelegate.getHandle();
        }

        private Chunk getChunkSafe(int chunkX, int chunkZ) {
            try {
                return getChunk(chunkX, chunkZ);
            } catch(IndexOutOfBoundsException e) {
                logger.warn("Chunk accessed outside permissible structure boundaries, this may cause unexpected behaviour");
                return new WorldChunk(worldDelegate, chunkX, chunkZ);
            }
        }
    }
}
