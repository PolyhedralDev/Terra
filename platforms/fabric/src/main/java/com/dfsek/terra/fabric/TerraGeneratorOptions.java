package com.dfsek.terra.fabric;

import com.dfsek.terra.api.util.generic.pair.ImmutablePair;
import com.dfsek.terra.fabric.mixin.access.SimpleRegistryAccessor;
import com.google.common.collect.Iterators;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.Random;

public class TerraGeneratorOptions extends GeneratorOptions {
    private final Map<DimensionType, ImmutablePair<GeneratorType, ChunkGenerator>> override = new HashMap<>();
    private final Registry<Biome> biomeRegistry;
    private final Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry;

    public TerraGeneratorOptions(long seed, boolean generateStructures, boolean bonusChest, SimpleRegistry<DimensionOptions> simpleRegistry,
                                 Registry<Biome> registry2, Registry<ChunkGeneratorSettings> registry3) {
        super(seed, generateStructures, bonusChest, simpleRegistry);
        System.out.println("Creating Terra options");
        Thread.dumpStack();
        this.biomeRegistry = registry2;
        this.chunkGeneratorSettingsRegistry = registry3;
    }

    public Registry<ChunkGeneratorSettings> getChunkGeneratorSettingsRegistry() {
        return chunkGeneratorSettingsRegistry;
    }

    public Registry<Biome> getBiomeRegistry() {
        return biomeRegistry;
    }

    public void overrideChunkGenerator(DimensionType dimensionType, GeneratorType identifier, ChunkGenerator newGenerator) {
        override.put(dimensionType, ImmutablePair.of(identifier, newGenerator));
    }

    @Override
    public TerraGeneratorOptions toggleGenerateStructures() {
        return new TerraGeneratorOptions(getSeed(), !shouldGenerateStructures(), hasBonusChest(), getDimensions(), biomeRegistry, chunkGeneratorSettingsRegistry);
    }

    @Override
    public TerraGeneratorOptions toggleBonusChest() {
        return new TerraGeneratorOptions(getSeed(), shouldGenerateStructures(), !hasBonusChest(), getDimensions(), biomeRegistry, chunkGeneratorSettingsRegistry);
    }

    @Override
    public GeneratorOptions withHardcore(boolean hardcore, OptionalLong seed) {
        long l = seed.orElse(this.getSeed());
        SimpleRegistry<DimensionOptions> simpleRegistry2;
        if(seed.isPresent()) {
            simpleRegistry2 = new SimpleRegistry<>(Registry.DIMENSION_OPTIONS, Lifecycle.experimental());
            long longSeed = seed.getAsLong();

            for(Map.Entry<RegistryKey<DimensionOptions>, DimensionOptions> registryKeyDimensionOptionsEntry : getDimensions().getEntries()) {
                RegistryKey<DimensionOptions> registryKey = registryKeyDimensionOptionsEntry.getKey();
                simpleRegistry2.add(registryKey, new DimensionOptions(registryKeyDimensionOptionsEntry.getValue().getDimensionTypeSupplier(), registryKeyDimensionOptionsEntry.getValue().getChunkGenerator().withSeed(longSeed)), getDimensions().getEntryLifecycle(registryKeyDimensionOptionsEntry.getValue()));
            }
        } else {
            simpleRegistry2 = getDimensions();
        }

        GeneratorOptions generatorOptions2;

        if(this.isDebugWorld()) {
            generatorOptions2 = new TerraGeneratorOptions(l, false, false, simpleRegistry2, biomeRegistry, chunkGeneratorSettingsRegistry);
        } else {
            generatorOptions2 = new TerraGeneratorOptions(l, this.shouldGenerateStructures(), hasBonusChest() && !hardcore, simpleRegistry2, biomeRegistry, chunkGeneratorSettingsRegistry);
        }

        return generatorOptions2;
    }

    public Map<DimensionType, ImmutablePair<GeneratorType, ChunkGenerator>> getOverrides() {
        return override;
    }

    @Override
    public ChunkGenerator getChunkGenerator() {
        System.out.println("Getting chunk generator");
        Thread.dumpStack();
        return super.getChunkGenerator();
    }

    @Override
    public SimpleRegistry<DimensionOptions> getDimensions() {
        new SimpleRegistry<DimensionOptions>(Registry.DIMENSION_OPTIONS, Lifecycle.stable()) {
            @Nullable
            @Override
            public DimensionOptions get(int index) {
                DimensionOptions options = super.get(index);
                if(options == null) return null;
                return new DimensionOptionsOverride(options);
            }

            @Nullable
            @Override
            public DimensionOptions get(@Nullable RegistryKey<DimensionOptions> key) {
                DimensionOptions options = super.get(key);
                if(options == null) return null;
                return new DimensionOptionsOverride(options);
            }

            @Nullable
            @Override
            public DimensionOptions get(@Nullable Identifier id) {
                DimensionOptions options = super.get(id);
                if(options == null) return null;
                return new DimensionOptionsOverride(options);
            }

            @Nullable
            @Override
            public DimensionOptions getRandom(Random random) {
                DimensionOptions options = super.getRandom(random);
                if(options == null) return null;
                return new DimensionOptionsOverride(options);
            }

            @SuppressWarnings({"unchecked", "ConstantConditions"})
            @Override
            public Iterator<DimensionOptions> iterator() {
                return Iterators.filter(((SimpleRegistryAccessor<DimensionOptions>) (Object) this).getRawIdToEntry().stream().map(DimensionOptionsOverride::new).map(dimensionOptionsOverride -> (DimensionOptions) dimensionOptionsOverride).iterator(), Objects::nonNull);
            }
        };
        return super.getDimensions();
    }

    public final class DimensionOptionsOverride extends DimensionOptions {

        public DimensionOptionsOverride(DimensionOptions options) {
            super(options.getDimensionTypeSupplier(), options.getChunkGenerator());
            System.out.println("Overriding " + getDimensionType());
        }

        @Override
        public ChunkGenerator getChunkGenerator() {
            ChunkGenerator overridden = override.get(getDimensionType()).getRight();
            if(overridden != null) return overridden;
            return super.getChunkGenerator();
        }
    }
}
