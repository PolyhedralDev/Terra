package com.dfsek.terra.neoforge.mixin.access;

import net.minecraft.registry.RegistryKey;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;


@Mixin(VillagerType.class)
public interface VillagerTypeAccessor extends com.dfsek.terra.mod.mixin.access.VillagerTypeAccessor {
    @Accessor("BIOME_TO_TYPE")
    static Map<RegistryKey<Biome>, VillagerType> getBiomeTypeToIdMap() {
        throw new AssertionError("Untransformed Accessor!");
    }
}
