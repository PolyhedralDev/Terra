package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.stream.Collectors;

public class TerraBiomeSource extends BiomeSource {
    public static final Codec<TerraBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry),
            Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed))
            .apply(instance, instance.stable(TerraBiomeSource::new)));

    private final Registry<Biome> biomeRegistry;
    private final long seed;
    private final TerraPlugin main;

    public TerraBiomeSource(Registry<Biome> biomes, long seed) {
        super(biomes.stream().collect(Collectors.toList()));
        this.biomeRegistry = biomes;
        this.seed = seed;
        this.main = TerraFabricPlugin.getInstance();
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new TerraBiomeSource(this.biomeRegistry, seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return null;
    }
}
