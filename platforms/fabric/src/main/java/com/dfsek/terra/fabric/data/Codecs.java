package com.dfsek.terra.fabric.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryCodecs;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;


public final class Codecs {
    public static final Codec<RegistryKey> TERRA_REGISTRY_KEY = RecordCodecBuilder
            .create(registryKey -> registryKey.group(Codec.STRING.fieldOf("namespace")
                                                                 .forGetter(RegistryKey::getNamespace),
                                                     Codec.STRING.fieldOf("id")
                                                                 .forGetter(RegistryKey::getID))
                                              .apply(registryKey, registryKey.stable(RegistryKey::of)));
    
    public static final Codec<ConfigPack> CONFIG_PACK = RecordCodecBuilder
            .create(config -> config.group(TERRA_REGISTRY_KEY.fieldOf("pack")
                                                             .forGetter(ConfigPack::getRegistryKey))
                                    .apply(config, config.stable(id -> FabricEntryPoint.getPlatform()
                                                                                       .getConfigRegistry()
                                                                                       .get(id)
                                                                                       .orElseThrow(() -> new IllegalArgumentException(
                                                                                               "No such config pack " +
                                                                                               id)))));
    
    public static final Codec<TerraBiomeSource> TERRA_BIOME_SOURCE = RecordCodecBuilder
            .create(instance -> instance.group(RegistryCodecs.dynamicRegistry(Registry.BIOME_KEY, Lifecycle.stable(), Biome.CODEC)
                                                             .fieldOf("biome_registry")
                                                             .forGetter(TerraBiomeSource::getBiomeRegistry),
                                               Codec.LONG.fieldOf("seed").stable()
                                                         .forGetter(TerraBiomeSource::getSeed),
                                               CONFIG_PACK.fieldOf("pack").stable()
                                                          .forGetter(TerraBiomeSource::getPack))
                                        .apply(instance, instance.stable(TerraBiomeSource::new)));
    
    public static final Codec<FabricChunkGeneratorWrapper> FABRIC_CHUNK_GENERATOR_WRAPPER = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryCodecs.dynamicRegistry(Registry.STRUCTURE_SET_KEY, Lifecycle.stable(), StructureSet.CODEC)
                                  .fieldOf("structures")
                                  .forGetter(FabricChunkGeneratorWrapper::getNoiseRegistry),
                    TERRA_BIOME_SOURCE.fieldOf("biome_source")
                                      .forGetter(FabricChunkGeneratorWrapper::getBiomeSource),
                    Codec.LONG.fieldOf("seed").stable()
                              .forGetter(FabricChunkGeneratorWrapper::getSeed),
                    CONFIG_PACK.fieldOf("pack").stable()
                               .forGetter(FabricChunkGeneratorWrapper::getPack),
                    ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings")
                                                         .forGetter(FabricChunkGeneratorWrapper::getSettings)
                                      ).apply(instance, instance.stable(FabricChunkGeneratorWrapper::new))
                                                                                                                     );
}
