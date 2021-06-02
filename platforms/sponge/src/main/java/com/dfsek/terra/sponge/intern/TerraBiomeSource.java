package com.dfsek.terra.sponge.intern;

import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.pack.WorldConfig;
import com.dfsek.terra.sponge.TerraSpongePlugin;
import com.dfsek.terra.sponge.intern.util.SpongeUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.jetbrains.annotations.NotNull;


import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TerraBiomeSource extends BiomeSource {
    public static final Codec<ConfigPack> PACK_CODEC = (RecordCodecBuilder.create(config -> config.group(
            Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID())
    ).apply(config, config.stable(TerraSpongePlugin.getInstance().getConfigRegistry()::get))));
    public static final Codec<TerraBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(source -> source.biomeRegistry),
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
    protected @NotNull Codec<TerraBiomeSource> codec() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new TerraBiomeSource(this.biomeRegistry, seed, pack);
    }

    @Override
    public @NotNull Biome getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome(biomeX << 2, biomeZ << 2);
        return Objects.requireNonNull(biomeRegistry.get(new ResourceLocation("terra", SpongeUtil.createBiomeID(pack, biome.getID()))));
    }
}
