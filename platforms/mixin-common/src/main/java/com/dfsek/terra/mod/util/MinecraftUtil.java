package com.dfsek.terra.mod.util;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProviderType;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.range.ConstantRange;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.data.Codecs;
import com.dfsek.terra.mod.implmentation.TerraIntProvider;
import com.dfsek.terra.mod.mixin_ifaces.FloraFeatureHolder;


public final class MinecraftUtil {
    public static final Logger logger = LoggerFactory.getLogger(MinecraftUtil.class);

    private MinecraftUtil() {

    }

    public static <T> Optional<RegistryEntry<T>> getEntry(Registry<T> registry, Identifier identifier) {
        return registry.getOptionalValue(identifier)
            .flatMap(id -> Optional.ofNullable(registry.getEntry(id)));
    }

    public static BlockEntity createState(WorldAccess worldAccess, BlockPos pos) {
        net.minecraft.block.entity.BlockEntity entity = worldAccess.getBlockEntity(pos);
        if(entity instanceof SignBlockEntity) {
            return (Sign) entity;
        } else if(entity instanceof MobSpawnerBlockEntity) {
            return (MobSpawner) entity;
        } else if(entity instanceof LootableContainerBlockEntity) {
            return (Container) entity;
        }
        return null;
    }

    public static void registerIntProviderTypes() {
        IntProviderType<TerraIntProvider> CONSTANT = IntProviderType.register("terra:constant_range",
            Codecs.TERRA_CONSTANT_RANGE_INT_PROVIDER_TYPE);

        TerraIntProvider.TERRA_RANGE_TYPE_TO_INT_PROVIDER_TYPE.put(ConstantRange.class, CONSTANT);
    }

    public static void registerFlora(Registry<net.minecraft.world.biome.Biome> biomeRegistry) {
        logger.info("Injecting flora into Terra biomes...");
        CommonPlatform.get().getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            PreLoadCompatibilityOptions compatibilityOptions = pack.getContext().get(PreLoadCompatibilityOptions.class);
            if(compatibilityOptions.isInjectFlora()) {
                pack.getCheckedRegistry(com.dfsek.terra.api.world.biome.Biome.class)
                    .forEach((id, biome) -> {
                        registerFlora(biome, pack, id, biomeRegistry);
                    });
            }
        });
    }

    private static void registerFlora(com.dfsek.terra.api.world.biome.Biome biome, ConfigPack pack,
                                      com.dfsek.terra.api.registry.key.RegistryKey id,
                                      Registry<net.minecraft.world.biome.Biome> biomeRegistry) {
        RegistryKey<net.minecraft.world.biome.Biome> vanillaKey = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(biomeRegistry);
        biomeRegistry.getOptionalValue(vanillaKey)
            .ifPresentOrElse(vanillaBiome -> {
                    Identifier terraBiomeIdentifier = Identifier.of("terra", BiomeUtil.createBiomeID(pack, id));
                    biomeRegistry.getOptionalValue(terraBiomeIdentifier).ifPresentOrElse(
                        terraBiome -> {
                            List<ConfiguredFeature<?, ?>> flowerFeatures = List.copyOf(
                                vanillaBiome.getGenerationSettings()
                                    .getFlowerFeatures());
                            logger.debug("Injecting flora into biome" +
                                         " {} : {}", terraBiomeIdentifier,
                                flowerFeatures);
                            ((FloraFeatureHolder) terraBiome.getGenerationSettings()).setFloraFeatures(
                                flowerFeatures);
                        },
                        () -> logger.error(
                            "No such biome: {}",
                            terraBiomeIdentifier)
                    );
                },
                () -> logger.error("No vanilla biome: {}", vanillaKey));
    }

    public static RegistryKey<Biome> registerBiomeKey(Identifier identifier) {
        return RegistryKey.of(RegistryKeys.BIOME, identifier);
    }

    public static RegistryKey<DimensionType> registerDimensionTypeKey(Identifier identifier) {
        return RegistryKey.of(RegistryKeys.DIMENSION_TYPE, identifier);
    }
}
