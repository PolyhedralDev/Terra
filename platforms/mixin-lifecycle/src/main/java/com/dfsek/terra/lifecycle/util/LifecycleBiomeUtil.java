package com.dfsek.terra.lifecycle.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.minecraft.attribute.BiomeTags.PlatformTagSource;
import com.dfsek.terra.minecraft.attribute.MinecraftAttributeParents;
import com.dfsek.terra.minecraft.attribute.MinecraftBiomeFactory;
import com.dfsek.terra.minecraft.attribute.PackParents;
import com.dfsek.terra.minecraft.attribute.TerraBiomeIndex;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.ModPlatform;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.mixin.access.VillagerTypeAccessor;
import com.dfsek.terra.mod.util.BiomeUtil;
import com.dfsek.terra.mod.util.MinecraftUtil;
import com.dfsek.terra.world.attribute.platform.AttributeReport;


public final class LifecycleBiomeUtil {
    private static final Logger logger = LoggerFactory.getLogger(LifecycleBiomeUtil.class);

    private LifecycleBiomeUtil() {

    }

    public static void registerBiomes(Registry<net.minecraft.world.level.biome.Biome> biomeRegistry, MinecraftBiomeFactory factory) {
        logger.info("Registering biomes...");
        TerraBiomeIndex biomes = new TerraBiomeIndex();
        ModPlatform platform = CommonPlatform.get();
        MinecraftAttributeParents parents = platform.attributeParents();
        platform.getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            PackParents packParents = parents.prepare(pack, platform.getAttributeHandle().pack(pack),
                AttributeReport.logging(pack));
            pack.getCheckedRegistry(Biome.class)
                .forEach((id, biome) -> registerBiome(biome, pack, packParents, id, biomeRegistry, parents, factory, biomes));
        });
        BiomeUtil.setTerraBiomes(biomes);
        logger.info("Terra biomes registered.");
    }

    /**
     * Builds a Terra biome's vanilla biome from its vanilla parent and its attribute entries, and registers it.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     */
    private static void registerBiome(Biome biome, ConfigPack pack, PackParents packParents,
                                      com.dfsek.terra.api.registry.key.RegistryKey id,
                                      Registry<net.minecraft.world.level.biome.Biome> registry, MinecraftAttributeParents parents,
                                      MinecraftBiomeFactory factory, TerraBiomeIndex biomes) {
        ProtoPlatformBiome proto = (ProtoPlatformBiome) biome.getPlatformBiome();
        ResourceKey<net.minecraft.world.level.biome.Biome> vanilla = proto.get(registry);
        net.minecraft.world.level.biome.Biome vanillaBiome = vanilla == null ? null : registry.getValue(vanilla);
        if(vanillaBiome == null) {
            logger.error("""
                         Failed to get Vanilla Biome!
                         Terra Biome ID: {}
                         Vanilla Biome: {}""", biome.getID(), vanilla == null ? biome.getPlatformBiome() : vanilla.identifier());
            return;
        }

        proto.setParentEntries(parents.biomeEntries(packParents, vanilla, vanillaBiome));

        if(pack.getContext().get(PreLoadCompatibilityOptions.class).useVanillaBiomes()) {
            proto.setDelegate(registry.wrapAsHolder(vanillaBiome));
            return;
        }

        Identifier identifier = MinecraftBiomeFactory.terraId(pack, id);

        if(biomes.contains(identifier)) {
            logger.warn(TerraBiomeIndex.sharedBiome(pack.getID(), biome.getID(), identifier));
            proto.setDelegate(MinecraftUtil.getEntry(registry, identifier).orElseThrow());
            return;
        }

        if(registry.containsKey(identifier)) {
            // Registered by an earlier load; edits wait for the next registry load.
            proto.setDelegate(MinecraftUtil.getEntry(registry, identifier).orElseThrow());
        } else {
            MinecraftBiomeFactory.Built built = factory.create(vanilla, vanillaBiome, biome, packParents, packParents.report(),
                vanillaBiome.getGenerationSettings());
            proto.setDelegate(Registry.registerForHolder(registry, MinecraftUtil.registerBiomeKey(identifier).identifier(),
                built.biome()));
            VillagerTypeAccessor.getBiomeTypeToIdMap().put(ResourceKey.create(Registries.BIOME, identifier), built.villagerType());
        }

        biomes.record(identifier, vanilla.identifier(), new PlatformTagSource(packParents.report(), biome.getID(), biome.getPlatformTags()));
    }
}
