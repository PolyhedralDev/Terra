package com.dfsek.terra.forge;

import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public final class ForgeUtil {
    public static String createBiomeID(ConfigPack pack, String biomeID) {
        return pack.getTemplate().getID().toLowerCase() + "/" + biomeID.toLowerCase(Locale.ROOT);
    }

    @SuppressWarnings("ConstantConditions")
    public static Biome createBiome(BiomeBuilder biome) {
        BiomeTemplate template = biome.getTemplate();
        Map<String, Integer> colors = template.getColors();

        Biome vanilla = (Biome) ((Object) new ArrayList<>(biome.getVanillaBiomes().getContents()).get(0));

        BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder();
        generationSettings.surfaceBuilder(SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.GRAVEL.defaultBlockState()))); // It needs a surfacebuilder, even though we dont use it.
        generationSettings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TerraForgePlugin.POPULATOR_CONFIGURED_FEATURE);

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
        vanillaEffects.getFoliageColorOverride().ifPresent(effects::foliageColorOverride);
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
                .build().setRegistryName("terra", createBiomeID(template.getPack(), template.getID()));
    }
}
