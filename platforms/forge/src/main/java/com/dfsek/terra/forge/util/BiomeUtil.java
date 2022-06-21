package com.dfsek.terra.forge.util;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.forge.ForgeEntryPoint;
import com.dfsek.terra.forge.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.forge.config.VanillaBiomeProperties;

import com.dfsek.terra.forge.mixin_ifaces.FloraFeatureHolder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome.Builder;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent.RegisterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public final class BiomeUtil {
    private static final Logger logger = LoggerFactory.getLogger(BiomeUtil.class);
    
    private static final Map<Identifier, List<Identifier>>
            TERRA_BIOME_MAP = new HashMap<>();
    
    private BiomeUtil() {
    
    }
    
    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
    
    public static void registerBiomes(RegisterHelper<net.minecraft.world.biome.Biome> helper) {
        logger.info("Registering biomes...");
        ForgeEntryPoint.getPlatform().getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            pack.getCheckedRegistry(Biome.class)
                .forEach((id, biome) -> registerBiome(biome, pack, id, helper));
        });
        registerFlora(BuiltinRegistries.BIOME);
        logger.info("Terra biomes registered.");
    }
    
    /**
     * Clones a Vanilla biome and injects Terra data to create a Terra-vanilla biome delegate.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     */
    private static void registerBiome(Biome biome, ConfigPack pack,
                                      com.dfsek.terra.api.registry.key.RegistryKey id, RegisterHelper<net.minecraft.world.biome.Biome> helper) {
        RegistryKey<net.minecraft.world.biome.Biome> vanilla = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(ForgeRegistries.BIOMES);
        
        
        if(pack.getContext().get(PreLoadCompatibilityOptions.class).useVanillaBiomes()) {
            ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(vanilla);
        } else {
            net.minecraft.world.biome.Biome minecraftBiome = createBiome(biome, ForgeRegistries.BIOMES.getDelegateOrThrow(vanilla).value());
            
            Identifier identifier = new Identifier("terra", createBiomeID(pack, id));
            
            if(ForgeRegistries.BIOMES.containsKey(identifier)) {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(ForgeRegistries.BIOMES.getHolder(identifier).orElseThrow().getKey().orElseThrow());
            } else {
                helper.register(registerKey(identifier).getValue(), minecraftBiome);
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(ForgeRegistries.BIOMES.getHolder(identifier).orElseThrow().getKey().orElseThrow());
            }
            
            TERRA_BIOME_MAP.computeIfAbsent(vanilla.getValue(), i -> new ArrayList<>()).add(identifier);
        }
    }
    
    public static void registerFlora(Registry<net.minecraft.world.biome.Biome> biomes) {
        logger.info("Injecting flora into Terra biomes...");
        TERRA_BIOME_MAP
                .forEach((vb, terraBiomes) ->
                                 biomes.getOrEmpty(vb)
                                           .ifPresentOrElse(vanilla -> terraBiomes
                                                                    .forEach(tb -> biomes.getOrEmpty(tb)
                                                                            .ifPresentOrElse(
                                                                                    terra -> {
                                                                                        List<ConfiguredFeature<?, ?>> flowerFeatures = List.copyOf(vanilla.getGenerationSettings().getFlowerFeatures());
                                                                                        logger.debug("Injecting flora into biome {} : {}", tb, flowerFeatures);
                                                                                        ((FloraFeatureHolder) terra.getGenerationSettings()).setFloraFeatures(flowerFeatures);
                                                                                        },
                                                                                    () -> logger.error(
                                                                                            "No such biome: {}",
                                                                                            tb))),
                                                            () -> logger.error("No vanilla biome: {}", vb)));
        
    }
    
    public static Map<Identifier, List<Identifier>> getTerraBiomeMap() {
        return Map.copyOf(TERRA_BIOME_MAP);
    }
    
    private static RegistryKey<net.minecraft.world.biome.Biome> registerKey(Identifier identifier) {
        return RegistryKey.of(Registry.BIOME_KEY, identifier);
    }
    
    public static net.minecraft.world.biome.Biome createBiome(Biome biome, net.minecraft.world.biome.Biome vanilla) {
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        
        BiomeEffects.Builder effects = new BiomeEffects.Builder();
        
        net.minecraft.world.biome.Biome.Builder builder = new Builder();
        
        if(biome.getContext().has(VanillaBiomeProperties.class)) {
            VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);
            
            effects.waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()))
                   .waterFogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor()))
                   .fogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor()))
                   .skyColor(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor()))
                   .grassColorModifier(
                           Objects.requireNonNullElse(vanillaBiomeProperties.getModifier(), vanilla.getEffects().getGrassColorModifier()));
            
            
            if(vanillaBiomeProperties.getGrassColor() == null) {
                vanilla.getEffects().getGrassColor().ifPresent(effects::grassColor);
            } else {
                effects.grassColor(vanillaBiomeProperties.getGrassColor());
            }
            
            if(vanillaBiomeProperties.getFoliageColor() == null) {
                vanilla.getEffects().getFoliageColor().ifPresent(effects::foliageColor);
            } else {
                effects.foliageColor(vanillaBiomeProperties.getFoliageColor());
            }
            
            builder.precipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.getPrecipitation()));
            
        } else {
            effects.waterColor(vanilla.getWaterColor())
                   .waterFogColor(vanilla.getWaterFogColor())
                   .fogColor(vanilla.getFogColor())
                   .skyColor(vanilla.getSkyColor());
            vanilla.getEffects().getFoliageColor().ifPresent(effects::foliageColor);
            vanilla.getEffects().getGrassColor().ifPresent(effects::grassColor);
            
            builder.precipitation(vanilla.getPrecipitation());
        }
    
        vanilla.getLoopSound().ifPresent(effects::loopSound);
        vanilla.getAdditionsSound().ifPresent(effects::additionsSound);
        vanilla.getMoodSound().ifPresent(effects::moodSound);
        vanilla.getMusic().ifPresent(effects::music);
        vanilla.getParticleConfig().ifPresent(effects::particleConfig);
        
        return builder
                .temperature(vanilla.getTemperature())
                .downfall(vanilla.getDownfall())
                .effects(effects.build())
                .spawnSettings(vanilla.getSpawnSettings())
                .generationSettings(generationSettings.build())
                .build();
    }
}
