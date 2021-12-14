package com.dfsek.terra.fabric.mixin;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.BiConsumer;

import com.dfsek.terra.fabric.util.FabricUtil;


@Mixin(ConfiguredStructureFeatures.class)
public class ConfiguredStructureFeaturesMixin {
    @Inject(method = "register(Ljava/util/function/BiConsumer;Lnet/minecraft/world/gen/feature/ConfiguredStructureFeature;" +
                     "Lnet/minecraft/util/registry/RegistryKey;)V",
            at = @At("HEAD"))
    private static void addStructuresToTerraBiomes(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> registrar,
                                                   ConfiguredStructureFeature<?, ?> feature,
                                                   RegistryKey<Biome> biome,
                                                   CallbackInfo ci) {
        if(FabricUtil.getTerraVanillaBiomes().containsKey(biome)) {
            FabricUtil.getTerraVanillaBiomes().get(biome).forEach(biomeRegistryKey -> registrar.accept(feature, biomeRegistryKey));
        }
    }
    
    @Inject(method = "register(Ljava/util/function/BiConsumer;Lnet/minecraft/world/gen/feature/ConfiguredStructureFeature;Ljava/util/Set;" +
                     ")V",
            at = @At("HEAD"))
    private static void addStructuresToTerraBiomes(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> registrar,
                                                   ConfiguredStructureFeature<?, ?> feature, Set<RegistryKey<Biome>> biomes,
                                                   CallbackInfo ci) {
        biomes.forEach(biome -> {
            if(FabricUtil.getTerraVanillaBiomes().containsKey(biome)) {
                FabricUtil.getTerraVanillaBiomes().get(biome).forEach(biomeRegistryKey -> registrar.accept(feature, biomeRegistryKey));
            }
        });
    }
}
