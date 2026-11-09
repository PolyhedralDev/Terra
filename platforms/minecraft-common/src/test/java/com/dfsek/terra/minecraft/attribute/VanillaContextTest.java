package com.dfsek.terra.minecraft.attribute;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.CONTEXT;
import static com.dfsek.terra.minecraft.attribute.MinecraftTestSupport.REGISTRIES;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class VanillaContextTest {
    static <T> MappedRegistry<T> copy(ResourceKey<? extends Registry<T>> key) {
        MappedRegistry<T> registry = new MappedRegistry<>(key, Lifecycle.stable());
        REGISTRIES.lookupOrThrow(key).listElements()
            .forEach(holder -> registry.register(holder.key(), holder.value(), RegistrationInfo.BUILT_IN));
        return registry;
    }

    @Test
    void unknownIdsKeepLoadFreezable() {
        MappedRegistry<DimensionType> dimensionTypes = copy(Registries.DIMENSION_TYPE);
        MappedRegistry<NoiseGeneratorSettings> noiseSettings = copy(Registries.NOISE_SETTINGS);
        MappedRegistry<Biome> biomes = copy(Registries.BIOME);
        VanillaContext context = VanillaContext.ofLoad(List.of(dimensionTypes, noiseSettings, biomes), MinecraftTestSupport.loadContext(),
                                                       key -> null);

        ResourceKey<DimensionType> nope = ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.parse("minecraft:nope"));
        assertFalse(context.dimensionTypes().get(nope).isPresent());
        assertFalse(context.noiseSettings().get(ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.parse("minecraft:nope")))
                        .isPresent());
        assertFalse(context.biomes().get(ResourceKey.create(Registries.BIOME, Identifier.parse("minecraft:nope"))).isPresent());
        assertFalse(context.ops().getter(Registries.DIMENSION_TYPE).orElseThrow().get(nope).isPresent());

        Holder.Reference<DimensionType> overworld = context.dimensionTypes().getOrThrow(BuiltinDimensionTypes.OVERWORLD);
        assertSame(dimensionTypes.get(BuiltinDimensionTypes.OVERWORLD).orElseThrow(), overworld);
        assertTrue(overworld.canSerializeIn(context.dimensionTypes()));
        assertTrue(overworld.canSerializeIn(context.ops().getter(Registries.DIMENSION_TYPE).orElseThrow()));

        assertDoesNotThrow(dimensionTypes::freeze);
        assertDoesNotThrow(noiseSettings::freeze);
        assertDoesNotThrow(biomes::freeze);
        assertEquals(REGISTRIES.lookupOrThrow(Registries.DIMENSION_TYPE).listElementIds().count(), dimensionTypes.size());
        assertSame(REGISTRIES.lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.OVERWORLD).value(),
                   overworld.value());
    }

    @Test
    void tagsAreCreatedOnDemand() {
        VanillaContext context = VanillaContext.ofLoad(List.of(copy(Registries.DIMENSION_TYPE), copy(Registries.NOISE_SETTINGS),
                                                               copy(Registries.BIOME)), MinecraftTestSupport.loadContext(),
                                                       key -> null);
        TagKey<Biome> tag = TagKey.create(Registries.BIOME, Identifier.parse("terra:pack"));
        assertTrue(context.ops().getter(Registries.BIOME).orElseThrow().get(tag).isPresent());
    }

    @Test
    void staticTagsResolveThroughLoadContext() {
        DimensionType overworld = REGISTRIES.lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.OVERWORLD).value();
        TagKey<Block> infiniburn = BlockTags.INFINIBURN_OVERWORLD;
        assertTrue(BuiltInRegistries.BLOCK.get(infiniburn).isEmpty(), "the built-in registry already has the tag");
        Registry.PendingTags<Block> pending = BuiltInRegistries.BLOCK.prepareTagReload(
            new TagLoader.LoadResult<>(Registries.BLOCK, Map.of(infiniburn, List.of(Blocks.NETHERRACK.builtInRegistryHolder()))));
        VanillaContext context = VanillaContext.ofLoad(List.of(copy(Registries.DIMENSION_TYPE), copy(Registries.NOISE_SETTINGS),
                                                               copy(Registries.BIOME), copy(Registries.WORLD_CLOCK),
                                                               copy(Registries.TIMELINE)),
                                                       MinecraftTestSupport.loadContext(pending), key -> null);
        Map<?, ?> encoded = (Map<?, ?>) VanillaCodecs.encode(DimensionType.DIRECT_CODEC, overworld, CONTEXT.ops());
        assertEquals("#minecraft:infiniburn_overworld", encoded.get("infiniburn"));

        DimensionType parsed = assertDoesNotThrow(() -> VanillaCodecs.parse(DimensionType.DIRECT_CODEC, encoded, context.ops()));
        assertEquals(infiniburn, parsed.infiniburn().unwrapKey().orElseThrow());
    }
}
