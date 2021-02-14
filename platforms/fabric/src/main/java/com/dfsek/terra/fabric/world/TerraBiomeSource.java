package com.dfsek.terra.fabric.world;

import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.provider.BiomeProvider;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.stream.Collectors;

public class TerraBiomeSource extends BiomeSource {
    public static final Codec<ConfigPack> PACK_CODEC = (RecordCodecBuilder.create(config -> config.group(
            Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID())
    ).apply(config, config.stable(TerraFabricPlugin.getInstance().getRegistry()::get))));
    public static final Codec<TerraBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry),
            Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed),
            PACK_CODEC.fieldOf("pack").stable().forGetter(source -> source.pack))
            .apply(instance, instance.stable(TerraBiomeSource::new)));

    private final Registry<Biome> biomeRegistry;
    private final long seed;
    private final BiomeProvider grid;
    private final ConfigPack pack;

    public TerraBiomeSource(Registry<Biome> biomes, long seed, ConfigPack pack) {
        super(biomes.stream().collect(Collectors.toList()));
        this.biomeRegistry = biomes;
        this.seed = seed;
        this.grid = pack.getBiomeProviderBuilder().build(seed);
        this.pack = pack;
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new TerraBiomeSource(this.biomeRegistry, seed, pack);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome(biomeX * 4, biomeZ * 4);
        return biomeRegistry.get(new Identifier("terra", TerraFabricPlugin.createBiomeID(pack, biome)));
    }


    @Override
    public boolean hasStructureFeature(StructureFeature<?> feature) {
        return false;
    }


}
