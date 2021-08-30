package com.dfsek.terra.forge.generation;

import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.forge.ForgeUtil;
import com.dfsek.terra.forge.TerraForgePlugin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Collectors;


public class TerraBiomeSource extends net.minecraft.world.biome.provider.BiomeProvider {
    public static final Codec<ConfigPack> PACK_CODEC = (RecordCodecBuilder.create(config -> config.group(
                                                                                                          Codec.STRING.fieldOf("pack").forGetter(pack -> pack.getTemplate().getID())
                                                                                                        )
                                                                                                  .apply(config, config.stable(
                                                                                                          TerraForgePlugin.getInstance()
                                                                                                                          .getConfigRegistry()::get))));
    public static final Codec<TerraBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                                                                                                              RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(source -> source.biomeRegistry),
                                                                                                              Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed),
                                                                                                              PACK_CODEC.fieldOf("pack").stable().forGetter(source -> source.pack))
                                                                                                      .apply(instance, instance.stable(
                                                                                                              TerraBiomeSource::new)));
    
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
    protected @NotNull
    Codec<? extends net.minecraft.world.biome.provider.BiomeProvider> codec() {
        return CODEC;
    }
    
    @Override
    public net.minecraft.world.biome.provider.@NotNull BiomeProvider withSeed(long seed) {
        return new TerraBiomeSource(this.biomeRegistry, seed, pack);
    }
    
    @Override
    public @NotNull
    Biome getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome(biomeX << 2, biomeZ << 2);
        return Objects.requireNonNull(biomeRegistry.get(new ResourceLocation("terra", ForgeUtil.createBiomeID(pack, biome.getID()))));
    }
}
