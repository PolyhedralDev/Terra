package com.dfsek.terra.mod.util;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.VillagerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.config.FertilizableConfig;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.mixin.access.VillagerTypeAccessor;


public class BiomeUtil {
    public static final Map<RegistryEntry<net.minecraft.world.biome.Biome>, Map<Identifier, FertilizableConfig>>
            TERRA_BIOME_FERTILIZABLE_MAP = new HashMap<>();
    public static final Map<TagKey<net.minecraft.world.biome.Biome>, List<Identifier>>
            TERRA_BIOME_TAG_MAP = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(BiomeUtil.class);
    
    public static void registerBiomes() {
        logger.info("Registering biomes...");
        CommonPlatform.get().getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            pack.getCheckedRegistry(Biome.class)
                .forEach((id, biome) -> registerBiome(biome, pack, id));
        });
        logger.info("Terra biomes registered.");
    }
    
    public static RegistryKey<net.minecraft.world.biome.Biome> registerKey(Identifier identifier) {
        return RegistryKey.of(Registry.BIOME_KEY, identifier);
    }
    
    protected static RegistryKey<net.minecraft.world.biome.Biome> registerBiome(Identifier identifier,
                                                                                net.minecraft.world.biome.Biome biome) {
        RegistryKey key = registerKey(identifier);
        if(!BuiltinRegistries.BIOME.contains(key)) {
            BuiltinRegistries.add(BuiltinRegistries.BIOME,
                                  key.getValue(),
                                  biome);
        }
        return getBiomeKey(identifier);
    }
    
    public static RegistryKey<net.minecraft.world.biome.Biome> getBiomeKey(Identifier identifier) {
        return BuiltinRegistries.BIOME.getKey(BuiltinRegistries.BIOME.get(identifier)).orElseThrow();
    }
    
    /**
     * Clones a Vanilla biome and injects Terra data to create a Terra-vanilla biome delegate.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     */
    protected static void registerBiome(Biome biome, ConfigPack pack,
                                        com.dfsek.terra.api.registry.key.RegistryKey id) {
        VanillaBiomeProperties vanillaBiomeProperties;
        vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);

        
        net.minecraft.world.biome.Biome minecraftBiome = MinecraftUtil.createBiome(vanillaBiomeProperties);
        
        Identifier identifier = new Identifier("terra", MinecraftUtil.createBiomeID(pack, id));
        
        biome.setPlatformBiome(new ProtoPlatformBiome(identifier, registerBiome(identifier, minecraftBiome)));
        
        Map villagerMap = VillagerTypeAccessor.getBiomeTypeToIdMap();
        
        villagerMap.put(RegistryKey.of(Registry.BIOME_KEY, identifier),
                        Objects.requireNonNullElse(vanillaBiomeProperties.getVillagerType(), VillagerType.PLAINS));
        
        TERRA_BIOME_FERTILIZABLE_MAP.put(RegistryEntry.of(minecraftBiome), vanillaBiomeProperties.getFertilizables());
        
        for(Identifier tag : vanillaBiomeProperties.getTags()) {
            TERRA_BIOME_TAG_MAP.getOrDefault(TagKey.of(Registry.BIOME_KEY, tag), new ArrayList<>()).add(identifier);
        }
    }
}
