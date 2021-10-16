package com.dfsek.terra.fabric.generation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.event.BiomeRegistrationEvent;


@Environment(EnvType.CLIENT)
public class TerraGeneratorType extends GeneratorType {
    private final ConfigPack pack;
    
    public TerraGeneratorType(ConfigPack pack) {
        super("terra." + pack.getID());
        this.pack = pack;
    }
    
    @Override
    public GeneratorOptions createDefaultOptions(DynamicRegistryManager.Impl registryManager, long seed, boolean generateStructures,
                                                 boolean bonusChest) {
        GeneratorOptions options = super.createDefaultOptions(registryManager, seed, generateStructures, bonusChest);
        FabricEntryPoint.getPlatform().getEventManager().callEvent(new BiomeRegistrationEvent(registryManager)); // register biomes
        return options;
    }
    
    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry,
                                               Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
        return new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
    }
}
