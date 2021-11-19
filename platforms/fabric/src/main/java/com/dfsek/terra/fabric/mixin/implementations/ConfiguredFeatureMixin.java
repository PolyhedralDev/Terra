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

package com.dfsek.terra.fabric.mixin.implementations;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Locale;
import java.util.Random;
import java.util.Set;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.fabric.FabricEntryPoint;


@Mixin(ConfiguredFeature.class)
@Implements(@Interface(iface = Tree.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ConfiguredFeatureMixin {
    @Shadow
    public abstract boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos);
    
    @SuppressWarnings({ "ConstantConditions", "try" })
    public boolean terra$plant(Vector3 l, World world, Random r) {
        String id = BuiltinRegistries.CONFIGURED_FEATURE.getId((ConfiguredFeature<?, ?>) (Object) this).toString();
        try(ProfileFrame ignore = FabricEntryPoint.getPlatform().getProfiler().profile("fabric_tree:" + id.toLowerCase(Locale.ROOT))) {
            StructureWorldAccess fabricWorldAccess = ((StructureWorldAccess) world);
            ChunkGenerator generatorWrapper = ((ServerWorldAccess) world).toServerWorld().getChunkManager().getChunkGenerator();
            return generate(fabricWorldAccess, generatorWrapper, r, new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        }
    }
    
    public Set<BlockType> terra$getSpawnable() {
        return MaterialSet.get(FabricEntryPoint.getPlatform().getWorldHandle().createBlockData("minecraft:grass_block"),
                               FabricEntryPoint.getPlatform().getWorldHandle().createBlockData("minecraft:podzol"),
                               FabricEntryPoint.getPlatform().getWorldHandle().createBlockData("minecraft:mycelium"));
    }
}
