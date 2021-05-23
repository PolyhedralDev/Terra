package com.dfsek.terra.forge;

import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.forge.config.PostLoadCompatibilityOptions;
import com.dfsek.terra.forge.config.PreLoadCompatibilityOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public final class ForgeUtil {
    public static String createBiomeID(ConfigPack pack, String biomeID) {
        return pack.getTemplate().getID().toLowerCase() + "/" + biomeID.toLowerCase(Locale.ROOT);
    }

    public static Biome createBiome(BiomeBuilder biome, ConfigPack pack, TerraForgePlugin.ForgeAddon forgeAddon) {
        BiomeTemplate template = biome.getTemplate();
        Map<String, Integer> colors = template.getColors();

        Biome vanilla = (Biome) (new ArrayList<>(biome.getVanillaBiomes().getContents()).get(0)).getHandle();

        BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder();

        generationSettings.surfaceBuilder(vanilla.getGenerationSettings().getSurfaceBuilder()); // It needs a surfacebuilder, even though we dont use it.

        generationSettings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TerraForgePlugin.POPULATOR_CONFIGURED_FEATURE);

        if(pack.getTemplate().vanillaCaves()) {
            for(GenerationStage.Carving carver : GenerationStage.Carving.values()) {
                for(Supplier<ConfiguredCarver<?>> configuredCarverSupplier : vanilla.getGenerationSettings().getCarvers(carver)) {
                    generationSettings.addCarver(carver, configuredCarverSupplier.get());
                }
            }
        }

        Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions> pair = forgeAddon.getTemplates().get(pack);
        PreLoadCompatibilityOptions compatibilityOptions = pair.getLeft();
        PostLoadCompatibilityOptions postLoadCompatibilityOptions = pair.getRight();

        TerraForgePlugin.getInstance().getDebugLogger().info("Injecting Vanilla structures and features into Terra biome " + biome.getTemplate().getID());

        for(Supplier<StructureFeature<?, ?>> structureFeature : vanilla.getGenerationSettings().structures()) {
            ResourceLocation key = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE.getKey(structureFeature.get());
            if(!compatibilityOptions.getExcludedBiomeStructures().contains(key) && !postLoadCompatibilityOptions.getExcludedPerBiomeStructures().getOrDefault(biome, Collections.emptySet()).contains(key)) {
                generationSettings.addStructureStart(structureFeature.get());
                TerraForgePlugin.getInstance().getDebugLogger().info("Injected structure " + key);
            }
        }

        if(compatibilityOptions.doBiomeInjection()) {
            for(int step = 0; step < vanilla.getGenerationSettings().features().size(); step++) {
                for(Supplier<ConfiguredFeature<?, ?>> featureSupplier : vanilla.getGenerationSettings().features().get(step)) {
                    ResourceLocation key = WorldGenRegistries.CONFIGURED_FEATURE.getKey(featureSupplier.get());
                    if(!compatibilityOptions.getExcludedBiomeFeatures().contains(key) && !postLoadCompatibilityOptions.getExcludedPerBiomeFeatures().getOrDefault(biome, Collections.emptySet()).contains(key)) {
                        generationSettings.addFeature(step, featureSupplier);
                        TerraForgePlugin.getInstance().getDebugLogger().info("Injected feature " + key + " at stage " + step);
                    }
                }
            }
        }

        BiomeAmbience vanillaEffects = vanilla.getSpecialEffects();
        BiomeAmbience.Builder effects = new BiomeAmbience.Builder()
                .waterColor(colors.getOrDefault("water", vanillaEffects.getWaterColor()))
                .waterFogColor(colors.getOrDefault("water-fog", vanillaEffects.getWaterFogColor()))
                .fogColor(colors.getOrDefault("fog", vanillaEffects.getFogColor()))
                .skyColor(colors.getOrDefault("sky", vanillaEffects.getSkyColor()))
                .grassColorModifier(vanillaEffects.getGrassColorModifier());

        if(colors.containsKey("grass")) {
            effects.grassColorOverride(colors.get("grass"));
        } else {
            vanillaEffects.getGrassColorOverride().ifPresent(effects::grassColorOverride);
        }

        if(colors.containsKey("foliage")) {
            effects.foliageColorOverride(colors.get("foliage"));
        } else {
            vanillaEffects.getFoliageColorOverride().ifPresent(effects::foliageColorOverride);
        }

        return new Biome.Builder()
                .precipitation(vanilla.getPrecipitation())
                .biomeCategory(vanilla.getBiomeCategory())
                .depth(vanilla.getDepth())
                .scale(vanilla.getScale())
                .temperature(vanilla.getBaseTemperature())
                .downfall(vanilla.getDownfall())
                .specialEffects(effects.build())
                .mobSpawnSettings(vanilla.getMobSettings())
                .generationSettings(generationSettings.build())
                .build()
                .setRegistryName("terra", createBiomeID(template.getPack(), template.getID()));
    }
}
