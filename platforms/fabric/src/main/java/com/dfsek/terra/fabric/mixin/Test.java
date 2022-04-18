package com.dfsek.terra.fabric.mixin;

import com.dfsek.terra.api.world.biome.Biome;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


@Mixin(ChunkGenerator.class)
public class Test<FC extends FeatureConfig, F extends StructureFeature<FC>> {
    
    @Redirect(method = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;locateStructure(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/registry/RegistryEntryList;Lnet/minecraft/util/math/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;", at = @At(value = "INVOKE", target = "Ljava/util/Set;isEmpty()Z"))
    private boolean inject(Set<?> instance) {
        System.out.println("Biome Set: " + instance);
        return instance.isEmpty();
    }
    
    @Redirect(method = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;locateStructure(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/registry/RegistryEntryList;Lnet/minecraft/util/math/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;", at = @At(value = "INVOKE", target = "Ljava/util/Collections;disjoint(Ljava/util/Collection;Ljava/util/Collection;)Z"))
    private boolean inject2(Collection<?> c, Collection<?> c2) {
        System.out.println("Biome Source Set: " + c);
        return Collections.disjoint(c, c2);
    }
}
