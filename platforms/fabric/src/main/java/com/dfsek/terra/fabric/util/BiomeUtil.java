package com.dfsek.terra.fabric.util;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.fabric.config.VanillaBiomeProperties;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome.Builder;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


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
    
    public static void registerBiomes() {
        logger.info("Registering biomes...");
        FabricEntryPoint.getPlatform().getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            pack.getCheckedRegistry(Biome.class)
                .forEach((id, biome) -> registerBiome(biome, pack, id));
        });
        logger.info("Terra biomes registered.");
    }
    
    /**
     * Clones a Vanilla biome and injects Terra data to create a Terra-vanilla biome delegate.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     */
    public static void registerBiome(Biome biome, ConfigPack pack,
                                     com.dfsek.terra.api.registry.key.RegistryKey id) {
        Registry<net.minecraft.world.biome.Biome> registry = BuiltinRegistries.BIOME;
        RegistryKey<net.minecraft.world.biome.Biome> vanilla = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(registry);
        
        
        if(pack.getContext().get(PreLoadCompatibilityOptions.class).useVanillaBiomes()) {
            ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(vanilla);
        } else {
            net.minecraft.world.biome.Biome minecraftBiome = createBiome(biome, registry.get(vanilla));
            
            Identifier identifier = new Identifier("terra", createBiomeID(pack, id));
            
            if(registry.containsId(identifier)) {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(FabricUtil.getEntry(registry, identifier)
                                                                                      .orElseThrow()
                                                                                      .getKey()
                                                                                      .orElseThrow());
            } else {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(BuiltinRegistries.add(registry,
                                                                                                  registerKey(identifier).getValue(),
                                                                                                  minecraftBiome).getKey().orElseThrow());
            }
            
            TERRA_BIOME_MAP.computeIfAbsent(vanilla.getValue(), i -> new ArrayList<>()).add(identifier);
        }
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
            
            
            builder.precipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.getPrecipitation()))
                   .category(Objects.requireNonNullElse(vanillaBiomeProperties.getCategory(), vanilla.getCategory()));
            
        } else {
            
            effects.waterColor(vanilla.getWaterColor())
                   .waterFogColor(vanilla.getWaterFogColor())
                   .fogColor(vanilla.getFogColor())
                   .skyColor(vanilla.getSkyColor());
            vanilla.getEffects().getFoliageColor().ifPresent(effects::foliageColor);
            vanilla.getEffects().getGrassColor().ifPresent(effects::grassColor);
            
            builder.precipitation(vanilla.getPrecipitation())
                   .category(vanilla.getCategory());
        }
        
        return builder
                .temperature(vanilla.getTemperature())
                .downfall(vanilla.getDownfall())
                .effects(effects.build())
                .spawnSettings(vanilla.getSpawnSettings())
                .generationSettings(generationSettings.build())
                .build();
    }
}
