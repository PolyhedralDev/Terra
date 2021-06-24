package com.dfsek.terra.forge.generation;

import com.dfsek.terra.config.pack.ConfigPack;
import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import org.jetbrains.annotations.NotNull;

public class TerraGeneratorType extends BiomeGeneratorTypeScreens {
    private final ConfigPack pack;

    public TerraGeneratorType(ConfigPack pack) {
        super(new StringTextComponent("Terra:" + pack.getTemplate().getID()));
        this.pack = pack;
    }

    @Override
    protected @NotNull
    ChunkGenerator generator(@NotNull Registry<Biome> biomeRegistry, @NotNull Registry<DimensionSettings> chunkGeneratorSettingsRegistry, long seed) {
        return new ForgeChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
    }
}
