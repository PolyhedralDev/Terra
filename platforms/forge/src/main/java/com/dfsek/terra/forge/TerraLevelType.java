package com.dfsek.terra.forge;

import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.forge.world.TerraBiomeSource;
import com.dfsek.terra.forge.world.generator.ForgeChunkGeneratorWrapper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraftforge.common.world.ForgeWorldType;

public class TerraLevelType implements ForgeWorldType.IChunkGeneratorFactory {
    private final ConfigPack pack;

    public TerraLevelType(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public ChunkGenerator createChunkGenerator(Registry<Biome> biomeRegistry, Registry<DimensionSettings> dimensionSettingsRegistry, long seed, String generatorSettings) {
        return new ForgeChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
    }
}
