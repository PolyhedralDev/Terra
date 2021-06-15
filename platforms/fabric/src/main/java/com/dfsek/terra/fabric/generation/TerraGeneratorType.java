package com.dfsek.terra.fabric.generation;

import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.util.FabricUtil;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class TerraGeneratorType extends GeneratorType {
    private final ConfigPack pack;

    public TerraGeneratorType(ConfigPack pack) {
        super("terra." + pack.getTemplate().getID());
        this.pack = pack;
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
        TerraFabricPlugin.getInstance().getConfigRegistry().forEach(pack -> pack.getBiomeRegistry().forEach((id, biome) -> Registry.register(biomeRegistry, new Identifier("terra", FabricUtil.createBiomeID(pack, id)), FabricUtil.createBiome(TerraFabricPlugin.getInstance().getFabricAddon(), biome, pack)))); // Register all Terra biomes.
        return new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
    }
}
