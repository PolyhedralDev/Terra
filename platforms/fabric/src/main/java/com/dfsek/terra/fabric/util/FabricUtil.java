package com.dfsek.terra.fabric.util;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.config.PostLoadCompatibilityOptions;
import com.dfsek.terra.fabric.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.fabric.mixin.access.BiomeEffectsAccessor;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public final class FabricUtil {
    public static String createBiomeID(ConfigPack pack, String biomeID) {
        return pack.getID().toLowerCase() + "/" + biomeID.toLowerCase(Locale.ROOT);
    }

    /**
     * Clones a Vanilla biome and injects Terra data to create a Terra-vanilla biome delegate.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     * @return The Minecraft delegate biome.
     */
    public static Biome createBiome(BiomeBuilder biome, ConfigPack pack, DynamicRegistryManager registryManager) {
        BiomeTemplate template = biome.getTemplate();
        Map<String, Integer> colors = template.getColors();

        TerraFabricPlugin.FabricAddon fabricAddon = TerraFabricPlugin.getInstance().getFabricAddon();

        Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
        Biome vanilla = ((ProtoBiome) (new ArrayList<>(biome.getVanillaBiomes().getContents()).get(0))).get(biomeRegistry);

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();

        generationSettings.surfaceBuilder(vanilla.getGenerationSettings().getSurfaceBuilder()); // It needs a surfacebuilder, even though we dont use it.

        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, TerraFabricPlugin.POPULATOR_CONFIGURED_FEATURE);

        if(pack.vanillaCaves()) {
            for(GenerationStep.Carver carver : GenerationStep.Carver.values()) {
                for(Supplier<ConfiguredCarver<?>> configuredCarverSupplier : vanilla.getGenerationSettings().getCarversForStep(carver)) {
                    generationSettings.carver(carver, configuredCarverSupplier.get());
                }
            }
        }

        Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions> pair = fabricAddon.getTemplates().get(pack);
        PreLoadCompatibilityOptions compatibilityOptions = pair.getLeft();
        PostLoadCompatibilityOptions postLoadCompatibilityOptions = pair.getRight();

        TerraFabricPlugin.getInstance().getDebugLogger().info("Injecting Vanilla structures and features into Terra biome " + biome.getTemplate().getID());

        Registry<ConfiguredStructureFeature<?, ?>> configuredStructureFeatureRegistry = registryManager.get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY);
        for(Supplier<ConfiguredStructureFeature<?, ?>> structureFeature : vanilla.getGenerationSettings().getStructureFeatures()) {
            Identifier key = configuredStructureFeatureRegistry.getId(structureFeature.get());
            if(!compatibilityOptions.getExcludedBiomeStructures().contains(key) && !postLoadCompatibilityOptions.getExcludedPerBiomeStructures().getOrDefault(biome, Collections.emptySet()).contains(key)) {
                generationSettings.structureFeature(structureFeature.get());
                TerraFabricPlugin.getInstance().getDebugLogger().info("Injected structure " + key);
            }
        }

        if(compatibilityOptions.doBiomeInjection()) {
            Registry<ConfiguredFeature<?, ?>> configuredFeatureRegistry = registryManager.get(Registry.CONFIGURED_FEATURE_KEY);
            for(int step = 0; step < vanilla.getGenerationSettings().getFeatures().size(); step++) {
                for(Supplier<ConfiguredFeature<?, ?>> featureSupplier : vanilla.getGenerationSettings().getFeatures().get(step)) {
                    Identifier key = configuredFeatureRegistry.getId(featureSupplier.get());
                    if(!compatibilityOptions.getExcludedBiomeFeatures().contains(key) && !postLoadCompatibilityOptions.getExcludedPerBiomeFeatures().getOrDefault(biome, Collections.emptySet()).contains(key)) {
                        generationSettings.feature(step, featureSupplier);
                        TerraFabricPlugin.getInstance().getDebugLogger().info("Injected feature " + key + " at stage " + step);
                    }
                }
            }
        }

        BiomeEffectsAccessor accessor = (BiomeEffectsAccessor) vanilla.getEffects();
        BiomeEffects.Builder effects = new BiomeEffects.Builder()
                .waterColor(colors.getOrDefault("water", accessor.getWaterColor()))
                .waterFogColor(colors.getOrDefault("water-fog", accessor.getWaterFogColor()))
                .fogColor(colors.getOrDefault("fog", accessor.getFogColor()))
                .skyColor(colors.getOrDefault("sky", accessor.getSkyColor()))
                .grassColorModifier(accessor.getGrassColorModifier());

        if(colors.containsKey("grass")) {
            effects.grassColor(colors.get("grass"));
        } else {
            accessor.getGrassColor().ifPresent(effects::grassColor);
        }
        if(colors.containsKey("foliage")) {
            effects.foliageColor(colors.get("foliage"));
        } else {
            accessor.getFoliageColor().ifPresent(effects::foliageColor);
        }

        return new Biome.Builder()
                .precipitation(vanilla.getPrecipitation())
                .category(vanilla.getCategory())
                .depth(vanilla.getDepth())
                .scale(vanilla.getScale())
                .temperature(vanilla.getTemperature())
                .downfall(vanilla.getDownfall())
                .effects(effects.build())
                .spawnSettings(vanilla.getSpawnSettings())
                .generationSettings(generationSettings.build())
                .build();
    }

    public static <T> void registerOrOverwrite(Registry<T> registry, RegistryKey<Registry<T>> key, Identifier identifier, T item) {
        if(registry.containsId(identifier)) {
            ((MutableRegistry<T>) registry).set(registry.getRawId(registry.get(identifier)), RegistryKey.of(key, identifier), item, Lifecycle.stable());
        } else {
            Registry.register(registry, identifier, item);
        }
    }
}
