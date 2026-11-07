package com.dfsek.terra.mod.mixin_ifaces;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.List;


public interface FloraFeatureHolder {
    void setFloraFeatures(List<ConfiguredFeature<?, ?>> features);
}
