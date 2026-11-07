package com.dfsek.terra.mod.util;

import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.range.ConstantRange;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.data.Codecs;
import com.dfsek.terra.mod.implmentation.MinecraftEntityTypeExtended;
import com.dfsek.terra.mod.implmentation.TerraIntProvider;
import com.dfsek.terra.mod.mixin_ifaces.FloraFeatureHolder;


public final class MinecraftUtil {
    public static final Logger logger = LoggerFactory.getLogger(MinecraftUtil.class);

    private MinecraftUtil() {

    }

    public static <T> Optional<Holder<T>> getEntry(Registry<T> registry, Identifier identifier) {
        return registry.getOptional(identifier)
            .flatMap(id -> Optional.ofNullable(registry.wrapAsHolder(id)));
    }

    public static BlockEntity createBlockEntity(LevelAccessor worldAccess, BlockPos pos) {
        net.minecraft.world.level.block.entity.BlockEntity entity = worldAccess.getBlockEntity(pos);
        if(entity instanceof SignBlockEntity) {
            return (Sign) entity;
        } else if(entity instanceof SpawnerBlockEntity) {
            return (MobSpawner) entity;
        } else if(entity instanceof RandomizableContainerBlockEntity) {
            return (Container) entity;
        }
        return null;
    }

    public static void schedulePhysics(BlockState blockState, BlockPos blockPos, TickAccess<Fluid> fluidScheduler,
                                       TickAccess<Block> blockScheduler) {
        if(blockState.liquid()) {
            fluidScheduler.schedule(ScheduledTick.probe(blockState.getFluidState().getType(), blockPos));
        } else {
            blockScheduler.schedule(ScheduledTick.probe(blockState.getBlock(), blockPos));
        }
    }

    public static boolean isCompatibleBlockStateExtended(com.dfsek.terra.api.block.state.BlockState blockState) {
        return blockState.isExtended() && BlockInput.class.isAssignableFrom(blockState.getClass());
    }

    //[Vanilla Copy]
    public static void loadBlockEntity(ChunkAccess chunk, Level world, BlockPos blockPos, BlockState state, CompoundTag nbt) {
        net.minecraft.world.level.block.entity.BlockEntity blockEntity;
        if("DUMMY".equals(nbt.getStringOr("id", ""))) {
            if(state.hasBlockEntity()) {
                blockEntity = ((EntityBlock) state.getBlock()).newBlockEntity(blockPos, state);
            } else {
                blockEntity = null;
            }
        } else {
            blockEntity = net.minecraft.world.level.block.entity.BlockEntity.loadStatic(blockPos, state, nbt, world.registryAccess());
        }

        if(blockEntity != null) {
            blockEntity.setLevel(world);
            chunk.setBlockEntity(blockEntity);
        }
    }

    public static boolean isCompatibleEntityTypeExtended(EntityType entityType) {
        return entityType.isExtended() && MinecraftEntityTypeExtended.class.isAssignableFrom(entityType.getClass());
    }

    public static void registerIntProviderTypes() {
        IntProviderType<TerraIntProvider> CONSTANT = IntProviderType.register("terra:constant_range",
            Codecs.TERRA_CONSTANT_RANGE_INT_PROVIDER_TYPE);

        TerraIntProvider.TERRA_RANGE_TYPE_TO_INT_PROVIDER_TYPE.put(ConstantRange.class, CONSTANT);
    }

    public static void registerFlora(Registry<net.minecraft.world.level.biome.Biome> biomeRegistry) {
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
                                      Registry<net.minecraft.world.level.biome.Biome> biomeRegistry) {
        ResourceKey<net.minecraft.world.level.biome.Biome> vanillaKey = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(biomeRegistry);
        biomeRegistry.getOptional(vanillaKey)
            .ifPresentOrElse(vanillaBiome -> {
                    Identifier terraBiomeIdentifier = Identifier.fromNamespaceAndPath("terra", BiomeUtil.createBiomeID(pack, id));
                    biomeRegistry.getOptional(terraBiomeIdentifier).ifPresentOrElse(
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

    public static ResourceKey<Biome> registerBiomeKey(Identifier identifier) {
        return ResourceKey.create(Registries.BIOME, identifier);
    }

    public static ResourceKey<DimensionType> registerDimensionTypeKey(Identifier identifier) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, identifier);
    }
}
