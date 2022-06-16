package com.dfsek.terra.fabric.mixin_ifaces;

import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.List;


public interface FloraFeatureHolder {
    void setFloraFeatures(List<ConfiguredFeature<?, ?>> features);
}
