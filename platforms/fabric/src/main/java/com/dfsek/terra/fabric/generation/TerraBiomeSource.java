package com.dfsek.terra.fabric.generation;

import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.util.FabricUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.Objects;
import java.util.stream.Collectors;

public class TerraBiomeSource extends BiomeSource {
    public static final Codec<ConfigPack> PACK_CODEC = (RecordCodecBuilder.create(config -> config.group(
            Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID())
    ).apply(config, config.stable(TerraFabricPlugin.getInstance().getConfigRegistry()::get))));
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
        super(biomes.stream()
                .filter(biome -> Objects.requireNonNull(biomes.getId(biome)).getNamespace().equals("terra")) // Filter out non-Terra biomes.
                .collect(Collectors.toList()));
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
        UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome(biomeX << 2, biomeZ << 2);
        return biomeRegistry.get(new Identifier("terra", FabricUtil.createBiomeID(pack, biome.getID())));
    }
}
