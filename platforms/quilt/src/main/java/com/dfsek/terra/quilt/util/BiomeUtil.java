package com.dfsek.terra.quilt.util;

import com.dfsek.terra.quilt.QuiltEntryPoint;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.util.MinecraftUtil;


public final class BiomeUtil {
    private static final Logger logger = LoggerFactory.getLogger(BiomeUtil.class);
    
    private BiomeUtil() {
    
    }
    
    public static void registerBiomes() {
        logger.info("Registering biomes...");
        QuiltEntryPoint.getPlatform().getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            pack.getCheckedRegistry(Biome.class)
                .forEach((id, biome) -> registerBiome(biome, pack, id));
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
                                     com.dfsek.terra.api.registry.key.RegistryKey id) {
        Registry<net.minecraft.world.biome.Biome> registry = BuiltinRegistries.BIOME;
        RegistryKey<net.minecraft.world.biome.Biome> vanilla = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(registry);
        
        
        if(pack.getContext().get(PreLoadCompatibilityOptions.class).useVanillaBiomes()) {
            ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(vanilla);
        } else {
            net.minecraft.world.biome.Biome minecraftBiome = MinecraftUtil.createBiome(biome, registry.get(vanilla));
            
            Identifier identifier = new Identifier("terra", MinecraftUtil.createBiomeID(pack, id));
            
            if(registry.containsId(identifier)) {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(MinecraftUtil.getEntry(registry, identifier)
                                                                                         .orElseThrow()
                                                                                         .getKey()
                                                                                         .orElseThrow());
            } else {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(BuiltinRegistries.add(registry,
                                                                                                  MinecraftUtil.registerKey(identifier).getValue(),
                                                                                                  minecraftBiome).getKey().orElseThrow());
            }
            
            MinecraftUtil.TERRA_BIOME_MAP.computeIfAbsent(vanilla.getValue(), i -> new ArrayList<>()).add(identifier);
        }
    }
    
}
