package com.dfsek.terra.bukkit.nms.v1_18_R2;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.config.VanillaBiomeProperties;


public class NMSBiomeInjector {
    
    public static <T> Optional<Holder<T>> getEntry(Registry<T> registry, ResourceLocation identifier) {
        return registry.getOptional(identifier)
                       .flatMap(registry::getResourceKey)
                       .map(registry::getOrCreateHolder);
    }
    
    public static Biome createBiome(com.dfsek.terra.api.world.biome.Biome biome, Biome vanilla)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Biome.BiomeBuilder builder = new Biome.BiomeBuilder(); // Builder
        
        
        builder.biomeCategory(Reflection.BIOME.getBiomeCategory(vanilla))
               .precipitation(vanilla.getPrecipitation()) // getPrecipitation
               .mobSpawnSettings(vanilla.getMobSettings())
               .generationSettings(vanilla.getGenerationSettings())
               .temperature(vanilla.getBaseTemperature())
               .downfall(vanilla.getDownfall());

        
        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder();

        effects.grassColorModifier(vanilla.getSpecialEffects().getGrassColorModifier());

        VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);

        effects.fogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor()))

               .waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()))

               .waterFogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor()))

               .skyColor(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor()));

        if(vanillaBiomeProperties.getFoliageColor() == null) {
            vanilla.getSpecialEffects().getFoliageColorOverride().ifPresent(effects::foliageColorOverride);
        } else {
            effects.foliageColorOverride(vanillaBiomeProperties.getFoliageColor());
        }

        if(vanillaBiomeProperties.getGrassColor() == null) {
            vanilla.getSpecialEffects().getGrassColorOverride().ifPresent(effects::grassColorOverride);
        } else {
            effects.grassColorOverride(vanillaBiomeProperties.getGrassColor());
        }
        
        vanilla.getAmbientLoop().ifPresent(effects::ambientLoopSound);
        vanilla.getAmbientAdditions().ifPresent(effects::ambientAdditionsSound);
        vanilla.getAmbientMood().ifPresent(effects::ambientMoodSound);
        vanilla.getBackgroundMusic().ifPresent(effects::backgroundMusic);
        vanilla.getAmbientParticle().ifPresent(effects::ambientParticle);

        builder.specialEffects(effects.build());
        
        return builder.build(); // build()
    }
    
    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
}
