package com.dfsek.terra.fabric.generation;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.BlockState;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.StructurePool.Projection;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightType;
import net.minecraft.world.gen.feature.StructureFeature;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;


public class BeardGenerator {
    private static final float[] STRUCTURE_WEIGHT_TABLE = Util.make(new float[13824], array -> {
        for (int i = 0; i < 24; ++i) {
            for (int j = 0; j < 24; ++j) {
                for (int k = 0; k < 24; ++k) {
                    array[i * 24 * 24 + j * 24 + k] = (float) calculateStructureWeight(j - 12, k - 12, i - 12);
                }
            }
        }
    });
    private final ObjectArrayList<StructurePiece> pieces;
    private final ObjectArrayList<JigsawJunction> junctions;
    private final Chunk chunk;
    private final int minY;
    private final int maxY;
    
    private final double threshold;
    
    public BeardGenerator(StructureAccessor structureAccessor, Chunk chunk, double threshold) {
        this.chunk = chunk;
        this.threshold = threshold;
        ChunkPos chunkPos = chunk.getPos();
        int i = chunkPos.getStartX();
        int j = chunkPos.getStartZ();
        this.junctions = new ObjectArrayList<>(32);
        this.pieces = new ObjectArrayList<>(10);
        int minY = chunk.getBottomY();
        int maxY = chunk.getTopY();
        for (StructureFeature<?> structureFeature : StructureFeature.LAND_MODIFYING_STRUCTURES) {
            for(StructureStart<?> start : structureAccessor.getStructureStarts(ChunkSectionPos.from(chunk), structureFeature)) {
                for(StructurePiece structurePiece : start.getChildren()) {
                    if(!structurePiece.intersectsChunk(chunkPos, 12)) continue;
                    if(structurePiece instanceof PoolStructurePiece poolStructurePiece) {
                        Projection projection = poolStructurePiece.getPoolElement().getProjection();
                        if(projection == Projection.RIGID) {
                            this.pieces.add(poolStructurePiece);
                        }
                        for(JigsawJunction jigsawJunction : poolStructurePiece.getJunctions()) {
                            int k = jigsawJunction.getSourceX();
                            int l = jigsawJunction.getSourceZ();
                            if(k <= i - 12 || l <= j - 12 || k >= i + 15 + 12 || l >= j + 15 + 12) {
                                continue;
                            }
                            maxY = Math.max(maxY, jigsawJunction.getSourceGroundY());
                            minY = Math.min(minY, jigsawJunction.getSourceGroundY());
                    
                            this.junctions.add(jigsawJunction);
                        }
                        continue;
                    }
                    maxY = Math.max(maxY, structurePiece.getCenter().getY());
                    minY = Math.min(minY, structurePiece.getCenter().getY());
                    this.pieces.add(structurePiece);
                }
            }
        }
        this.pieces.trim();
        this.junctions.trim();
        this.minY = minY;
        this.maxY = maxY;
    }
    
    public void generate(ChunkGenerator generator, WorldProperties worldProperties, BiomeProvider biomeProvider) {
        int xi = chunk.getPos().x << 4;
        int zi = chunk.getPos().z << 4;
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                int depth = 0;
                for(int y = maxY; y >= minY; y--) {
                    if(calculateNoise(x + xi, y, z + zi) > threshold) {
                        chunk.setBlockState(new BlockPos(x, y, z), (BlockState) generator
                                .getPalette(x + xi, y, z + zi, worldProperties, biomeProvider)
                                .get(depth, x + xi, y, z + zi, worldProperties.getSeed()), false);
                        depth++;
                    } else {
                        depth = 0;
                    }
                }
            }
        }
    }
    
    
    @SuppressWarnings("RedundantCast") // Suppressed to prevent IDEs from removing these casts causing ClassCastExceptions
    public double calculateNoise(int x, int y, int z) {
        double noise = 0.0;
        
        for (Object __structurePiece: (Object[]) this.pieces.elements()) {
            if (__structurePiece == null) break;
            StructurePiece structurePiece = (StructurePiece) __structurePiece;
            BlockBox blockBox = structurePiece.getBoundingBox();
            int structureX = Math.max(0, Math.max(blockBox.getMinX() - x, x - blockBox.getMaxX()));
            int structureY = y - (blockBox.getMinY() + (structurePiece instanceof PoolStructurePiece ? ((PoolStructurePiece)structurePiece).getGroundLevelDelta() : 0));
            int structureZ = Math.max(0, Math.max(blockBox.getMinZ() - z, z - blockBox.getMaxZ()));
            StructureWeightType structureWeightType = structurePiece.getWeightType();
            if (structureWeightType == StructureWeightType.BURY) {
                noise += getMagnitudeWeight(structureX, structureY, structureZ);
                continue;
            }
            if (structureWeightType != StructureWeightType.BEARD) continue;
            
            noise += getStructureWeight(structureX, structureY, structureZ) * 0.8;
        }
    
        for (Object __structurePiece: (Object[]) this.junctions.elements()) {
            if (__structurePiece == null) break;
            JigsawJunction structurePiece = (JigsawJunction) __structurePiece;
            int structureX = x - structurePiece.getSourceX();
            int structureY = y - structurePiece.getSourceGroundY();
            int structureZ = z - structurePiece.getSourceZ();
            noise += getStructureWeight(structureX, structureY, structureZ) * 0.4;
        }
        
        return noise;
    }
    
    private static double getMagnitudeWeight(int x, int y, int z) {
        double d = MathHelper.magnitude(x, (double)y / 2.0, z);
        return MathHelper.clampedLerpFromProgress(d, 0.0, 6.0, 1.0, 0.0);
    }
    
    /**
     * Gets the structure weight from the array from the given position, or 0 if the position is out of bounds.
     */
    private static double getStructureWeight(int x, int y, int z) {
        int xOffset = x + 12;
        int yOffset = y + 12;
        int zOffset = z + 12;
        if (xOffset < 0 || xOffset >= 24) {
            return 0.0;
        }
        if (yOffset < 0 || yOffset >= 24) {
            return 0.0;
        }
        if (zOffset < 0 || zOffset >= 24) {
            return 0.0;
        }
        return STRUCTURE_WEIGHT_TABLE[zOffset * 24 * 24 + xOffset * 24 + yOffset];
    }
    
    
    /**
     * Calculates the structure weight for the given position.
     * <p>The weight increases as x and z approach {@code (0, 0)}, and positive y values make the weight negative while negative y
     * values make the weight positive.
     */
    private static double calculateStructureWeight(int x, int y, int z) {
        double horizontalDistanceSquared = x * x + z * z;
        double yOffset = y + 0.5;
        double verticalSquared = yOffset * yOffset;
        double naturalDistance = Math.pow(Math.E, -(verticalSquared / 16.0 + horizontalDistanceSquared / 16.0));
        double inverseSquareRootDistance = -yOffset * MathHelper.fastInverseSqrt(verticalSquared / 2.0 + horizontalDistanceSquared / 2.0) / 2.0;
        return inverseSquareRootDistance * naturalDistance;
    }
}
