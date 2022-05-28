package com.dfsek.terra.fabric.mixin.access;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;


@Mixin(GenerationSettings.class)
public interface GenerationSettingsAccessor {
    @Mutable
    @Accessor("flowerFeatures")
    void setFlowerFeatures(Supplier<List<ConfiguredFeature<?, ?>>> flowerFeatures);
    
    @Accessor("flowerFeatures")
    Supplier<List<ConfiguredFeature<?, ?>>> getFlowerFeaturesSupplier();
}
