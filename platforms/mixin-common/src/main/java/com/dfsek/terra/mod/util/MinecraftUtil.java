package com.dfsek.terra.mod.util;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProviderType;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Builder;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.data.Codecs;
import com.dfsek.terra.mod.implmentation.TerraIntProvider;
import com.dfsek.terra.mod.mixin.access.BiomeAccessor;
import com.dfsek.terra.mod.mixin_ifaces.FloraFeatureHolder;


public final class MinecraftUtil {
    public static final Logger logger = LoggerFactory.getLogger(MinecraftUtil.class);
    public static final Map<Identifier, List<Identifier>>
        TERRA_BIOME_MAP = new HashMap<>();

    private MinecraftUtil() {

    }

    public static <T> Optional<RegistryEntry<T>> getEntry(Registry<T> registry, Identifier identifier) {
        return registry.getOrEmpty(identifier)
            .flatMap(registry::getKey)
            .flatMap(registry::getEntry);
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

    public static void registerFlora(Registry<net.minecraft.world.biome.Biome> biomeRegistry) {

        CommonPlatform.get().getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            PreLoadCompatibilityOptions compatibilityOptions = pack.getContext().get(PreLoadCompatibilityOptions.class);
            if(compatibilityOptions.isInjectFlora()) {
                pack.getCheckedRegistry(com.dfsek.terra.api.world.biome.Biome.class)
                    .forEach((id, biome) -> {
                        registerFlora(biome, pack, id, biomeRegistry);
                    });
            }
        });
        logger.info("Injecting flora into Terra biomes...");

    }

    public static void registerFlora(com.dfsek.terra.api.world.biome.Biome biome, ConfigPack pack,
                                     com.dfsek.terra.api.registry.key.RegistryKey id,
                                     Registry<net.minecraft.world.biome.Biome> biomeRegistry) {
        RegistryKey<net.minecraft.world.biome.Biome> vanillaKey = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(biomeRegistry);
        biomeRegistry.getOrEmpty(vanillaKey)
            .ifPresentOrElse(vanillaBiome -> {
                    Identifier terraBiomeIdentifier = Identifier.of("terra", MinecraftUtil.createBiomeID(pack, id));
                    biomeRegistry.getOrEmpty(terraBiomeIdentifier).ifPresentOrElse(
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

    public static Map<Identifier, List<Identifier>> getTerraBiomeMap() {
        return Map.copyOf(TERRA_BIOME_MAP);
    }


    public static void registerIntProviderTypes() {
        IntProviderType<TerraIntProvider> CONSTANT = IntProviderType.register("terra:constant_range",
            Codecs.TERRA_CONSTANT_RANGE_INT_PROVIDER_TYPE);

        TerraIntProvider.TERRA_RANGE_TYPE_TO_INT_PROVIDER_TYPE.put(ConstantRange.class, CONSTANT);
    }

    public static RegistryKey<Biome> registerBiomeKey(Identifier identifier) {
        return RegistryKey.of(RegistryKeys.BIOME, identifier);
    }

    public static RegistryKey<DimensionType> registerDimensionTypeKey(Identifier identifier) {
        return RegistryKey.of(RegistryKeys.DIMENSION_TYPE, identifier);
    }

    public static Biome createBiome(com.dfsek.terra.api.world.biome.Biome biome, Biome vanilla,
                                    VanillaBiomeProperties vanillaBiomeProperties) {
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();

        BiomeEffects.Builder effects = new BiomeEffects.Builder();

        net.minecraft.world.biome.Biome.Builder builder = new Builder();

        effects.waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()))
            .waterFogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor()))
            .fogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor()))
            .skyColor(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor()))
            .grassColorModifier(
                Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColorModifier(),
                    vanilla.getEffects().getGrassColorModifier()));

        if(vanillaBiomeProperties.getFoliageColor() == null) {
            vanilla.getEffects().getFoliageColor().ifPresent(effects::foliageColor);
        } else {
            effects.foliageColor(vanillaBiomeProperties.getFoliageColor());
        }

        if(vanillaBiomeProperties.getGrassColor() == null) {
            vanilla.getEffects().getGrassColor().ifPresent(effects::grassColor);
        } else {
            effects.grassColor(vanillaBiomeProperties.getGrassColor());
        }

        if(vanillaBiomeProperties.getParticleConfig() == null) {
            vanilla.getEffects().getParticleConfig().ifPresent(effects::particleConfig);
        } else {
            effects.particleConfig(vanillaBiomeProperties.getParticleConfig());
        }

        if(vanillaBiomeProperties.getLoopSound() == null) {
            vanilla.getEffects().getLoopSound().ifPresent(effects::loopSound);
        } else {
            effects.loopSound(Registries.SOUND_EVENT.getEntry(vanillaBiomeProperties.getLoopSound()));
        }

        if(vanillaBiomeProperties.getMoodSound() == null) {
            vanilla.getEffects().getMoodSound().ifPresent(effects::moodSound);
        } else {
            effects.moodSound(vanillaBiomeProperties.getMoodSound());
        }

        if(vanillaBiomeProperties.getAdditionsSound() == null) {
            vanilla.getEffects().getAdditionsSound().ifPresent(effects::additionsSound);
        } else {
            effects.additionsSound(vanillaBiomeProperties.getAdditionsSound());
        }

        if(vanillaBiomeProperties.getMusic() == null) {
            vanilla.getEffects().getMusic().ifPresent(effects::music);
        } else {
            effects.music(vanillaBiomeProperties.getMusic());
        }

        builder.precipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.hasPrecipitation()));

        builder.temperature(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperature(), vanilla.getTemperature()));

        builder.downfall(Objects.requireNonNullElse(vanillaBiomeProperties.getDownfall(),
            ((BiomeAccessor) ((Object) vanilla)).getWeather().downfall()));

        builder.temperatureModifier(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperatureModifier(),
            ((BiomeAccessor) ((Object) vanilla)).getWeather().temperatureModifier()));

        builder.spawnSettings(Objects.requireNonNullElse(vanillaBiomeProperties.getSpawnSettings(), vanilla.getSpawnSettings()));

        return builder
            .effects(effects.build())
            .generationSettings(generationSettings.build())
            .build();
    }

    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
}
