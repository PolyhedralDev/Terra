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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.StructureWeightSampler.class_7301;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;


// net.minecraft.world.gen.StructureWeightSampler
public class BeardGenerator {
    private static final float[] STRUCTURE_WEIGHT_TABLE = Util.make(new float[13824], array -> {
        for(int i = 0; i < 24; ++i) {
            for(int j = 0; j < 24; ++j) {
                for(int k = 0; k < 24; ++k) {
                    array[i * 24 * 24 + j * 24 + k] = (float)calculateStructureWeight(j - 12, k - 12, i - 12);
                }
            }
        }
    });
    
    private final ObjectListIterator<StructureWeightSampler.class_7301> pieceIterator;
    private final ObjectListIterator<JigsawJunction> junctionIterator;
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
        ObjectList<JigsawJunction> junctions = new ObjectArrayList<>(32);
        ObjectList<class_7301> pieces = new ObjectArrayList<>(10);
        int minY = chunk.getBottomY();
        int maxY = chunk.getTopY();
        for(StructureStart structureStart : structureAccessor.method_41035(chunkPos,
                                                                           structureType -> structureType.getTerrainAdaptation() !=
                                                                                            StructureTerrainAdaptation.NONE)) {
            StructureTerrainAdaptation structureTerrainAdaptation = structureStart.getStructure().getTerrainAdaptation();
        
            for(StructurePiece structurePiece : structureStart.getChildren()) {
                if(structurePiece.intersectsChunk(chunkPos, 12)) {
                    if(structurePiece instanceof PoolStructurePiece poolStructurePiece) {
                        Projection projection = poolStructurePiece.getPoolElement().getProjection();
                        if(projection == Projection.RIGID) {
                            pieces.add(
                                    new class_7301(
                                            poolStructurePiece.getBoundingBox(), structureTerrainAdaptation,
                                            poolStructurePiece.getGroundLevelDelta()
                                    )
                                      );
                            maxY = Math.max(maxY, poolStructurePiece.getCenter().getY());
                            minY = Math.min(minY, poolStructurePiece.getCenter().getY());
                        }
                    
                        for(JigsawJunction jigsawJunction : poolStructurePiece.getJunctions()) {
                            int k = jigsawJunction.getSourceX();
                            int l = jigsawJunction.getSourceZ();
                            if(k > i - 12 && l > j - 12 && k < i + 15 + 12 && l < j + 15 + 12) {
                                junctions.add(jigsawJunction);
                                maxY = Math.max(maxY, jigsawJunction.getSourceGroundY());
                                minY = Math.min(minY, jigsawJunction.getSourceGroundY());
                            }
                        }
                    } else {
                        pieces.add(new class_7301(structurePiece.getBoundingBox(), structureTerrainAdaptation, 0));
                        maxY = Math.max(maxY, structurePiece.getCenter().getY());
                        minY = Math.min(minY, structurePiece.getCenter().getY());
                    }
                }
            }
        
        }
        this.pieceIterator = pieces.iterator();
        this.junctionIterator = junctions.iterator();
        this.minY = minY;
        this.maxY = maxY;
    }
    
    private static double getMagnitudeWeight(int x, int y, int z) {
        double d = MathHelper.magnitude(x, (double) y / 2.0, z);
        return MathHelper.clampedLerpFromProgress(d, 0.0, 6.0, 1.0, 0.0);
    }
    
    /**
     * Gets the structure weight from the array from the given position, or 0 if the position is out of bounds.
     */
    private static double getStructureWeight(int x, int y, int z, int i) {
        int j = x + 12;
        int k = y + 12;
        int l = z + 12;
        if (isInRange(j) && isInRange(k) && isInRange(l)) {
            double d = (double)i + 0.5;
            double e = MathHelper.squaredMagnitude(x, d, z);
            double f = -d * MathHelper.fastInverseSqrt(e / 2.0) / 2.0;
            return f * (double)STRUCTURE_WEIGHT_TABLE[l * 24 * 24 + j * 24 + k];
        } else {
            return 0.0;
        }
    }
    
    private static boolean isInRange(int i) {
        return i >= 0 && i < 24;
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
        double inverseSquareRootDistance = -yOffset * MathHelper.fastInverseSqrt(verticalSquared / 2.0 + horizontalDistanceSquared / 2.0) /
                                           2.0;
        return inverseSquareRootDistance * naturalDistance;
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
    
    public double calculateNoise(int x, int y, int z) {
        double d;
        double var10001;
        for(d = 0.0; this.pieceIterator.hasNext(); d += var10001) {
            StructureWeightSampler.class_7301 lv = this.pieceIterator.next();
            BlockBox blockBox = lv.box();
            int l = lv.groundLevelDelta();
            int m = Math.max(0, Math.max(blockBox.getMinX() - x, x - blockBox.getMaxX()));
            int n = Math.max(0, Math.max(blockBox.getMinZ() - z, z - blockBox.getMaxZ()));
            int o = blockBox.getMinY() + l;
            int p = y - o;
        
            int q = switch(lv.terrainAdjustment()) {
                case NONE -> 0;
                case BURY, BEARD_THIN -> p;
                case BEARD_BOX -> Math.max(0, Math.max(o - y, y - blockBox.getMaxY()));
            };
            var10001 = switch(lv.terrainAdjustment()) {
                case NONE -> 0.0;
                case BURY -> getMagnitudeWeight(m, q, n);
                case BEARD_THIN, BEARD_BOX -> getStructureWeight(m, q, n, p) * 0.8;
            };
        }
    
        this.pieceIterator.back(Integer.MAX_VALUE);
    
        while(this.junctionIterator.hasNext()) {
            JigsawJunction jigsawJunction = this.junctionIterator.next();
            int r = x - jigsawJunction.getSourceX();
            int l = y - jigsawJunction.getSourceGroundY();
            int m = z - jigsawJunction.getSourceZ();
            d += getStructureWeight(r, l, m, l) * 0.4;
        }
    
        this.junctionIterator.back(Integer.MAX_VALUE);
        return d;
    }
}
