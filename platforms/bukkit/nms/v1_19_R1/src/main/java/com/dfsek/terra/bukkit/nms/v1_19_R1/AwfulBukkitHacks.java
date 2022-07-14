package com.dfsek.terra.bukkit.nms.v1_19_R1;

import com.dfsek.terra.bukkit.nms.v1_19_R1.util.BiomeUtil;

import com.dfsek.terra.bukkit.nms.v1_19_R1.util.TagUtil;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.registry.master.ConfigRegistry;


public class AwfulBukkitHacks {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwfulBukkitHacks.class);
    
    public static final Map<Holder<net.minecraft.world.level.biome.Biome>, Map<ResourceLocation,
            ProbabilityCollection<ConfiguredStructure>>>
            TERRA_BIOME_FERTILIZABLE_MAP = new HashMap<>();
    public static final Map<TagKey<Biome>, List<ResourceLocation>>
            TERRA_BIOME_TAG_MAP = new HashMap<>();
    
    public static void registerBiomes(ConfigRegistry configRegistry) {
        try {
            LOGGER.info("Hacking biome registry...");
            WritableRegistry<Biome> biomeRegistry = (WritableRegistry<Biome>) Registries.biomeRegistry();
            
            Reflection.MAPPED_REGISTRY.setFrozen((MappedRegistry<?>) biomeRegistry, false);
            
            configRegistry.forEach(pack -> pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach((key, biome) -> {
                BiomeUtil.registerBiome(biome, pack, key);
            }));
            
            Reflection.MAPPED_REGISTRY.setFrozen((MappedRegistry<?>) biomeRegistry, true); // freeze registry again :)
    
            TagUtil.registerBiomeTags(biomeRegistry);
            
        } catch(SecurityException | IllegalArgumentException exception) {
            throw new RuntimeException(exception);
        }
    }
}
