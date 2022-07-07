package com.dfsek.terra.forge.util;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.VillagerType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent.RegisterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.forge.ForgeEntryPoint;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.mixin.access.VillagerTypeAccessor;
import com.dfsek.terra.mod.util.MinecraftUtil;


public final class BiomeUtil {
    private static final Logger logger = LoggerFactory.getLogger(BiomeUtil.class);
    
    
    private BiomeUtil() {
    
    }
    
    
    public static void registerBiomes(RegisterHelper<net.minecraft.world.biome.Biome> helper) {
        logger.info("Registering biomes...");
        ForgeEntryPoint.getPlatform().getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            pack.getCheckedRegistry(Biome.class)
                .forEach((id, biome) -> registerBiome(biome, pack, id, helper));
        });
        MinecraftUtil.registerFlora(BuiltinRegistries.BIOME);
        logger.info("Terra biomes registered.");
    }
    
    /**
     * Clones a Vanilla biome and injects Terra data to create a Terra-vanilla biome delegate.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     */
    private static void registerBiome(Biome biome, ConfigPack pack,
                                      com.dfsek.terra.api.registry.key.RegistryKey id,
                                      RegisterHelper<net.minecraft.world.biome.Biome> helper) {
        RegistryKey<net.minecraft.world.biome.Biome> vanilla = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(BuiltinRegistries.BIOME);
        
        
        if(pack.getContext().get(PreLoadCompatibilityOptions.class).useVanillaBiomes()) {
            ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(vanilla);
        } else {
            VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);
            
            net.minecraft.world.biome.Biome minecraftBiome = MinecraftUtil.createBiome(biome,
                                                                                       ForgeRegistries.BIOMES.getDelegateOrThrow(vanilla)
                                                                                                             .value(),
                                                                                       vanillaBiomeProperties);
            
            Identifier identifier = new Identifier("terra", MinecraftUtil.createBiomeID(pack, id));
            
            if(ForgeRegistries.BIOMES.containsKey(identifier)) {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(ForgeRegistries.BIOMES.getHolder(identifier)
                                                                                                  .orElseThrow()
                                                                                                  .getKey()
                                                                                                  .orElseThrow());
            } else {
                helper.register(MinecraftUtil.registerKey(identifier).getValue(), minecraftBiome);
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(ForgeRegistries.BIOMES.getHolder(identifier)
                                                                                                  .orElseThrow()
                                                                                                  .getKey()
                                                                                                  .orElseThrow());
            }
            
            Map villagerMap = VillagerTypeAccessor.getBiomeTypeToIdMap();
            
            villagerMap.put(RegistryKey.of(Registry.BIOME_KEY, identifier),
                            Objects.requireNonNullElse(vanillaBiomeProperties.getVillagerType(),
                                                       villagerMap.getOrDefault(vanilla, VillagerType.PLAINS)));
            
            MinecraftUtil.TERRA_BIOME_MAP.computeIfAbsent(vanilla.getValue(), i -> new ArrayList<>()).add(identifier);
        }
    }
}
