package com.dfsek.terra.fabric;

import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.PopulatorFeature;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

public class FabricEntryPoint implements ModInitializer {
    public static final PopulatorFeature POPULATOR_FEATURE = new PopulatorFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredFeature<?, ?> POPULATOR_CONFIGURED_FEATURE = POPULATOR_FEATURE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(NopeDecoratorConfig.INSTANCE));
    private static final TerraPluginImpl TERRA_PLUGIN = new TerraPluginImpl();

    public static TerraPluginImpl getTerraPlugin() {
        return TERRA_PLUGIN;
    }

    @Override
    public void onInitialize() {
        // register the things
        Registry.register(Registry.FEATURE, new Identifier("terra", "populator"), POPULATOR_FEATURE);
        RegistryKey<ConfiguredFeature<?, ?>> floraKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier("terra", "populator"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, floraKey.getValue(), POPULATOR_CONFIGURED_FEATURE);

        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("terra:terra"), FabricChunkGeneratorWrapper.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new Identifier("terra:terra"), TerraBiomeSource.CODEC);
    }
}
