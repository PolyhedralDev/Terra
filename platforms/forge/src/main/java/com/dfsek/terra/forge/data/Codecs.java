package com.dfsek.terra.forge.data;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.forge.ForgeEntryPoint;
import com.dfsek.terra.forge.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.forge.generation.TerraBiomeSource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;


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
                                    .apply(config, config.stable(id -> ForgeEntryPoint.getPlatform()
                                                                                       .getConfigRegistry()
                                                                                       .get(id)
                                                                                       .orElseThrow(() -> new IllegalArgumentException(
                                                                                               "No such config pack " +
                                                                                               id)))));
    
    public static final Codec<TerraBiomeSource> TERRA_BIOME_SOURCE = RecordCodecBuilder
            .create(instance -> instance.group(RegistryOps.createRegistryCodec(Registry.BIOME_KEY)
                                                          .fieldOf("biome_registry")
                                                          .stable()
                                                          .forGetter(TerraBiomeSource::getBiomeRegistry),
                                               CONFIG_PACK.fieldOf("pack")
                                                          .stable()
                                                          .forGetter(TerraBiomeSource::getPack))
                                        .apply(instance, instance.stable(TerraBiomeSource::new)));
    
    public static final Codec<FabricChunkGeneratorWrapper> FABRIC_CHUNK_GENERATOR_WRAPPER = RecordCodecBuilder
            .create(
                    instance -> instance.group(
                            RegistryOps.createRegistryCodec(Registry.STRUCTURE_SET_KEY)
                                       .fieldOf("structure_registry")
                                       .stable()
                                       .forGetter(FabricChunkGeneratorWrapper::getNoiseRegistry),
                            TERRA_BIOME_SOURCE.fieldOf("biome_source")
                                              .stable()
                                              .forGetter(FabricChunkGeneratorWrapper::getBiomeSource),
                            CONFIG_PACK.fieldOf("pack")
                                       .stable()
                                       .forGetter(FabricChunkGeneratorWrapper::getPack),
                            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings")
                                                                 .stable()
                                                                 .forGetter(FabricChunkGeneratorWrapper::getSettings)
                                              ).apply(instance, instance.stable(FabricChunkGeneratorWrapper::new))
                   );
}
