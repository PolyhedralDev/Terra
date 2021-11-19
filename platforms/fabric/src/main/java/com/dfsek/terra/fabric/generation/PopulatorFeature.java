/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.fabric.generation;

import com.mojang.serialization.Codec;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.Chunkified;


/**
 * Feature wrapper for Terra populator
 */
public class PopulatorFeature extends Feature<DefaultFeatureConfig> {
    public PopulatorFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }
    
    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        ChunkGenerator chunkGenerator = context.getGenerator();
        if(!(chunkGenerator instanceof FabricChunkGeneratorWrapper)) return true;
        StructureWorldAccess world = context.getWorld();
        FabricChunkGeneratorWrapper gen = (FabricChunkGeneratorWrapper) chunkGenerator;
        gen.getHandle().getGenerationStages().forEach(populator -> {
            if(!(populator instanceof Chunkified)) {
                populator.populate((World) world, (Chunk) world);
            }
        });
        return true;
    }
}
