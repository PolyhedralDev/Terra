package com.dfsek.terra.bukkit.nms.v1_19_R1.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.VillagerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.nms.v1_19_R1.Reflection;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.FertilizableConfig;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.ProtoPlatformBiome;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.VanillaBiomeProperties;


public class BiomeUtil {
    public static final Map<Holder<net.minecraft.world.level.biome.Biome>, Map<ResourceLocation, FertilizableConfig>>
            TERRA_BIOME_FERTILIZABLE_MAP = new HashMap<>();
    public static final Map<TagKey<net.minecraft.world.level.biome.Biome>, List<ResourceLocation>>
            TERRA_BIOME_TAG_MAP = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(BiomeUtil.class);
    
    protected static ResourceKey<net.minecraft.world.level.biome.Biome> registerBiome(ResourceLocation identifier,
                                                                                      net.minecraft.world.level.biome.Biome biome) {
        BuiltinRegistries.register(BuiltinRegistries.BIOME,
                                   MinecraftUtil.registerKey(identifier)
                                                .location(),
                                   biome);
        return getBiomeKey(identifier);
    }
    
    public static ResourceKey<net.minecraft.world.level.biome.Biome> getBiomeKey(ResourceLocation identifier) {
        return BuiltinRegistries.BIOME.getResourceKey(BuiltinRegistries.BIOME.get(identifier)).orElseThrow();
    }
    
    /**
     * Clones a Vanilla biome and injects Terra data to create a Terra-vanilla biome delegate.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     */
    public static void registerBiome(Biome biome, ConfigPack pack,
                                     com.dfsek.terra.api.registry.key.RegistryKey id) {
        VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);
        
        net.minecraft.world.level.biome.Biome minecraftBiome = MinecraftUtil.createBiome(vanillaBiomeProperties);
        
        ResourceLocation identifier = new ResourceLocation("terra", MinecraftUtil.createBiomeID(pack, id));
        
        biome.setPlatformBiome(new ProtoPlatformBiome(identifier, registerBiome(identifier, minecraftBiome)));
        
        Map villagerMap = Reflection.VILLAGER_TYPE.getByBiome();
        
        villagerMap.put(ResourceKey.create(Registry.BIOME_REGISTRY, identifier),
                        Objects.requireNonNullElse(vanillaBiomeProperties.getVillagerType(), VillagerType.PLAINS));
        
        TERRA_BIOME_FERTILIZABLE_MAP.put(Holder.direct(minecraftBiome), vanillaBiomeProperties.getFertilizables());
        
        for(ResourceLocation tag : vanillaBiomeProperties.getTags()) {
            TERRA_BIOME_TAG_MAP.getOrDefault(TagKey.create(Registry.BIOME_REGISTRY, tag), new ArrayList<>()).add(identifier);
        }
    }
}
