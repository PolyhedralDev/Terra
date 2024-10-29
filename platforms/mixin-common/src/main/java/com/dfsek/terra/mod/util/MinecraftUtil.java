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
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Builder;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
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
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.mixin.access.BiomeAccessor;
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

    public static void registerFlora(Registry<net.minecraft.world.biome.Biome> biomes) {
        logger.info("Injecting flora into Terra biomes...");
        BiomeUtil.TERRA_BIOME_MAP
            .forEach((vb, terraBiomes) ->
                biomes.getOptionalValue(vb)
                    .ifPresentOrElse(vanilla -> terraBiomes
                            .forEach(tb -> biomes.getOptionalValue(tb)
                                .ifPresentOrElse(
                                    terra -> {
                                        List<ConfiguredFeature<?, ?>> flowerFeatures = List.copyOf(
                                            vanilla.getGenerationSettings()
                                                .getFlowerFeatures());
                                        logger.debug("Injecting flora into biome" +
                                                     " {} : {}", tb,
                                            flowerFeatures);
                                        ((FloraFeatureHolder) terra.getGenerationSettings()).setFloraFeatures(
                                            flowerFeatures);
                                    },
                                    () -> logger.error(
                                        "No such biome: {}",
                                        tb))),
                        () -> logger.error("No vanilla biome: {}", vb)));

    }

    public static RegistryKey<Biome> registerKey(Identifier identifier) {
        return RegistryKey.of(RegistryKeys.BIOME, identifier);
    }
}
