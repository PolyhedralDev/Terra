package com.dfsek.terra.lifecycle.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.mixin.access.VillagerTypeAccessor;
import com.dfsek.terra.mod.util.BiomeUtil;
import com.dfsek.terra.mod.util.MinecraftUtil;


public final class LifecycleBiomeUtil {
    private static final Logger logger = LoggerFactory.getLogger(LifecycleBiomeUtil.class);

    private LifecycleBiomeUtil() {

    }

    public static void registerBiomes(Registry<net.minecraft.world.level.biome.Biome> biomeRegistry) {
        logger.info("Registering biomes...");
        CommonPlatform.get().getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            pack.getCheckedRegistry(Biome.class)
                .forEach((id, biome) -> registerBiome(biome, pack, id, biomeRegistry));
        });
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
                                      Registry<net.minecraft.world.level.biome.Biome> registry) {
        ResourceKey<net.minecraft.world.level.biome.Biome> vanilla = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(registry);

        if(vanilla == null) {
            logger.error("""
                         Failed to get Vanilla Biome Regiestry key!
                         Terra Biome ID: {}
                         Vanilla Biome: {}""", biome.getID(), biome.getPlatformBiome());
        }

        if(pack.getContext().get(PreLoadCompatibilityOptions.class).useVanillaBiomes()) {
            ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(registry.wrapAsHolder(registry.getValue(vanilla)));
        } else {
            VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);


            net.minecraft.world.level.biome.Biome vanilaBiome = registry.getValue(vanilla);
            if(vanilaBiome == null) {
                String vanillaBiomeName;
                if(vanilla != null) {
                    vanillaBiomeName = vanilla.identifier().toString();
                } else {
                    vanillaBiomeName = "NULL";
                }
                logger.error("""
                             Failed to get Vanilla Biome!
                             Terra Biome ID: {}
                             Vanilla Biome: {}""", biome.getID(), vanillaBiomeName);
                return;
            }

            net.minecraft.world.level.biome.Biome minecraftBiome = BiomeUtil.createBiome(Objects.requireNonNull(vanilaBiome),
                vanillaBiomeProperties);

            Identifier identifier = Identifier.fromNamespaceAndPath("terra", BiomeUtil.createBiomeID(pack, id));

            if(registry.containsKey(identifier)) {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(MinecraftUtil.getEntry(registry, identifier)
                    .orElseThrow());
            } else {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(Registry.registerForHolder(registry,
                    MinecraftUtil.registerBiomeKey(identifier)
                        .identifier(),
                    minecraftBiome));
            }

            Map<ResourceKey<net.minecraft.world.level.biome.Biome>, ResourceKey<VillagerType>> villagerMap =
                VillagerTypeAccessor.getBiomeTypeToIdMap();

            villagerMap.put(ResourceKey.create(Registries.BIOME, identifier),
                Objects.requireNonNullElse(vanillaBiomeProperties.getVillagerType(),
                    villagerMap.getOrDefault(vanilla, VillagerType.PLAINS)));

            BiomeUtil.TERRA_BIOME_MAP.computeIfAbsent(vanilla.identifier(), i -> new ArrayList<>()).add(identifier);
        }
    }

}
