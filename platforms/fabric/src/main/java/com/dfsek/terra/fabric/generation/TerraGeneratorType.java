package com.dfsek.terra.fabric.generation;

import com.dfsek.terra.config.pack.ConfigPackImpl;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.event.BiomeRegistrationEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Environment(EnvType.CLIENT)
public class TerraGeneratorType extends GeneratorType {
    private final ConfigPackImpl pack;

    public TerraGeneratorType(ConfigPackImpl pack) {
        super("terra." + pack.getTemplate().getID());
        this.pack = pack;
    }

    @Override
    public GeneratorOptions createDefaultOptions(DynamicRegistryManager.Impl registryManager, long seed, boolean generateStructures, boolean bonusChest) {
        GeneratorOptions options = super.createDefaultOptions(registryManager, seed, generateStructures, bonusChest);
        TerraFabricPlugin.getInstance().getEventManager().callEvent(new BiomeRegistrationEvent(registryManager)); // register biomes
        return options;
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
        return new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
    }
}
