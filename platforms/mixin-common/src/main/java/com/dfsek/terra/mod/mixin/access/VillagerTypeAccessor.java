package com.dfsek.terra.mod.mixin.access;

import net.minecraft.registry.RegistryKey;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;


@Mixin(VillagerType.class)
public interface VillagerTypeAccessor {
    @Accessor("BIOME_TO_TYPE")
    static Map<RegistryKey<Biome>, VillagerType> getBiomeTypeToIdMap() {
        try {
            Class<?> accessor = Class.forName("com.dfsek.terra.neoforge.mixin.access.VillagerTypeAccessor");
            @SuppressWarnings("unchecked")
            Map<RegistryKey<Biome>, VillagerType> map =
                (Map<RegistryKey<Biome>, VillagerType>) accessor.getMethod("getBiomeTypeToIdMap").invoke(null);
            return map;
        } catch (Throwable t) {
            throw new AssertionError("Untransformed Accessor!", t);
        }
    }
}
