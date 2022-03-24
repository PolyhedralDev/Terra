package com.dfsek.terra.fabric.data;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;

import com.dfsek.terra.fabric.FabricEntryPoint;

import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.RegistryElementCodec;
import net.minecraft.util.dynamic.RegistryLoader;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryCodecs;
import net.minecraft.util.registry.RegistryFixedCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;


public final class Codecs {
    public static final Codec<RegistryKey> REGISTRY_KEY = RecordCodecBuilder
            .create(registryKey -> registryKey.group(Codec.STRING.fieldOf("namespace")
                                                                 .forGetter(RegistryKey::getNamespace),
                                                     Codec.STRING.fieldOf("id")
                                                                 .forGetter(RegistryKey::getID))
                                              .apply(registryKey, registryKey.stable(RegistryKey::of)));
    
    public static final Codec<ConfigPack> CONFIG_PACK = RecordCodecBuilder
            .create(config -> config.group(REGISTRY_KEY.fieldOf("pack")
                                                       .forGetter(ConfigPack::getRegistryKey))
                                    .apply(config, config.stable(id -> FabricEntryPoint.getPlatform()
                                                                                       .getConfigRegistry()
                                                                                       .get(id)
                                                                                       .orElseThrow(() -> new IllegalArgumentException(
                                                                                               "No such config pack " +
                                                                                               id)))));
    
    public static final Codec<TerraBiomeSource> TERRA_BIOME_SOURCE = RecordCodecBuilder
            .create(instance -> instance.group(RegistryCodecs.entryList(Registry.BIOME_KEY)
                                                       .fieldOf("biome_registry")
                                                             .forGetter(TerraBiomeSource::getBiomeRegistry),
                                               Codec.LONG.fieldOf("seed").stable()
                                                         .forGetter(TerraBiomeSource::getSeed),
                                               CONFIG_PACK.fieldOf("pack").stable()
                                                          .forGetter(TerraBiomeSource::getPack))
                                        .apply(instance, instance.stable(
                                                TerraBiomeSource::new)));
    
    public static final Codec<FabricChunkGeneratorWrapper> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    TERRA_BIOME_SOURCE.fieldOf("biome_source")
                                      .forGetter(FabricChunkGeneratorWrapper::getBiomeSource),
                    Codec.LONG.fieldOf("seed").stable()
                              .forGetter(FabricChunkGeneratorWrapper::getSeed),
                    CONFIG_PACK.fieldOf("pack").stable()
                              .forGetter(FabricChunkGeneratorWrapper::getPack),
                    ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings")
                                                         .forGetter(FabricChunkGeneratorWrapper::getSettingsSupplier)
                                      ).apply(instance, instance.stable(FabricChunkGeneratorWrapper::new))
                                                                                            );
}
