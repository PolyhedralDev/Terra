package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.stream.Collectors;

public class TerraBiomeSource extends BiomeSource {
    public static final Codec<TerraBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry),
            Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed))
            .apply(instance, instance.stable(TerraBiomeSource::new)));

    private final Registry<Biome> biomeRegistry;
    private final long seed;
    private final TerraBiomeGrid grid;

    public TerraBiomeSource(Registry<Biome> biomes, long seed) {
        super(biomes.stream().collect(Collectors.toList()));
        this.biomeRegistry = biomes;
        this.seed = seed;
        this.grid = new TerraBiomeGrid.TerraBiomeGridBuilder(seed, TerraFabricPlugin.getInstance().getRegistry().get("DEFAULT"), TerraFabricPlugin.getInstance()).build();
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
        FabricBiome biome = ((FabricBiome) grid.getBiome(biomeX * 4, biomeZ * 4, GenerationPhase.BASE).getVanillaBiome());
        return biome.getHandle();
    }

    @Override
    public boolean hasStructureFeature(StructureFeature<?> feature) {
        return false;
    }


}
