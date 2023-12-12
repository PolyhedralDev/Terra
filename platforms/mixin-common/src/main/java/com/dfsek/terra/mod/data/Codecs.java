package com.dfsek.terra.mod.data;

import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.api.util.Range;

import com.dfsek.terra.mod.implmentation.TerraIntProvider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;
import com.dfsek.terra.mod.generation.TerraBiomeSource;


public final class Codecs {
    public static final Codec<RegistryKey> TERRA_REGISTRY_KEY = RecordCodecBuilder
        .create(registryKey -> registryKey.group(Codec.STRING.fieldOf("namespace")
                    .stable()
                    .forGetter(RegistryKey::getNamespace),
                Codec.STRING.fieldOf("id")
                    .stable()
                    .forGetter(RegistryKey::getID))
            .apply(registryKey, registryKey.stable(RegistryKey::of)));

    public static final Codec<ConfigPack> CONFIG_PACK = RecordCodecBuilder
        .create(config -> config.group(TERRA_REGISTRY_KEY.fieldOf("pack")
                .stable()
                .forGetter(ConfigPack::getRegistryKey))
            .apply(config, config.stable(id -> CommonPlatform.get()
                .getConfigRegistry()
                .get(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "No such config pack " +
                    id)))));

    public static final Codec<TerraBiomeSource> TERRA_BIOME_SOURCE = RecordCodecBuilder
        .create(instance -> instance.group(
                CONFIG_PACK.fieldOf("pack")
                    .stable()
                    .forGetter(TerraBiomeSource::getPack))
            .apply(instance, instance.stable(TerraBiomeSource::new)));

    public static final Codec<MinecraftChunkGeneratorWrapper> MINECRAFT_CHUNK_GENERATOR_WRAPPER = RecordCodecBuilder
        .create(
            instance -> instance.group(
                TERRA_BIOME_SOURCE.fieldOf("biome_source")
                    .stable()
                    .forGetter(MinecraftChunkGeneratorWrapper::getBiomeSource),
                CONFIG_PACK.fieldOf("pack")
                    .stable()
                    .forGetter(MinecraftChunkGeneratorWrapper::getPack),
                ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings")
                    .stable()
                    .forGetter(MinecraftChunkGeneratorWrapper::getSettings)
            ).apply(instance, instance.stable(
                MinecraftChunkGeneratorWrapper::new))
        );

    public static final Codec<TerraIntProvider> TERRA_CONSTANT_RANGE_INT_PROVIDER_TYPE = RecordCodecBuilder.create(range -> range.group(
            Codec.INT.fieldOf("min").stable().forGetter(TerraIntProvider::getMin),
            Codec.INT.fieldOf("max").stable().forGetter(TerraIntProvider::getMax)).apply(range, range.stable((min, max) -> new TerraIntProvider(new ConstantRange(
            min, max)))));
}
