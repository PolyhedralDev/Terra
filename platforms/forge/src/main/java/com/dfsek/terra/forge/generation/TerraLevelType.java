package com.dfsek.terra.forge.generation;

import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.forge.TerraForgePlugin;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraftforge.common.world.ForgeWorldType;

public class TerraLevelType implements ForgeWorldType.IChunkGeneratorFactory {
    public static final TerraLevelType TERRA_LEVEL_TYPE = new TerraLevelType();
    public static final ForgeWorldType FORGE_WORLD_TYPE = new ForgeWorldType(TERRA_LEVEL_TYPE).setRegistryName("terra", "world");
    @Override
    public ChunkGenerator createChunkGenerator(Registry<Biome> biomeRegistry, Registry<DimensionSettings> dimensionSettingsRegistry, long seed, String generatorSettings) {
        System.out.println(generatorSettings);
        dimensionSettingsRegistry.forEach(System.out::println);
        ConfigPack pack = TerraForgePlugin.getInstance().getConfigRegistry().get("DEFAULT");
        TerraForgePlugin.getInstance().logger().info("Creating generator for config pack " + pack.getTemplate().getID());
        return new ForgeChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
    }
}
