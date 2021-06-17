package com.dfsek.terra.fabric.generation;

import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.util.FabricUtil;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class TerraGeneratorType extends GeneratorType {
    private final ConfigPack pack;

    public TerraGeneratorType(ConfigPack pack) {
        super("terra." + pack.getTemplate().getID());
        this.pack = pack;
    }

    @Override
    public GeneratorOptions createDefaultOptions(DynamicRegistryManager.Impl registryManager, long seed, boolean generateStructures, boolean bonusChest) {
        GeneratorOptions options = super.createDefaultOptions(registryManager, seed, generateStructures, bonusChest);
        Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
        TerraFabricPlugin.getInstance().getConfigRegistry().forEach(pack -> pack.getBiomeRegistry().forEach((id, biome) -> Registry.register(biomeRegistry, new Identifier("terra", FabricUtil.createBiomeID(pack, id)), FabricUtil.createBiome(TerraFabricPlugin.getInstance().getFabricAddon(), biome, pack, registryManager)))); // Register all Terra biomes.
        return options;
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
        return new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
    }
}
