package com.dfsek.terra.fabric.mixin;

import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.TerraGeneratorOptions;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import com.google.common.base.MoreObjects;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Properties;
import java.util.Random;

import static net.minecraft.world.gen.GeneratorOptions.method_28608;

@Mixin(GeneratorOptions.class)
public abstract class GeneratorOptionsMixin {
    @Shadow
    public static NoiseChunkGenerator createOverworldGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
        return null;
    }

    @Inject(method = "fromProperties(Lnet/minecraft/util/registry/DynamicRegistryManager;Ljava/util/Properties;)Lnet/minecraft/world/gen/GeneratorOptions;", at = @At("HEAD"), cancellable = true)
    private static void fromProperties(DynamicRegistryManager dynamicRegistryManager, Properties properties, CallbackInfoReturnable<GeneratorOptions> cir) {
        if(properties.get("level-type") == null) {
            return;
        }

        String prop = properties.get("level-type").toString().trim();
        if(prop.startsWith("Terra")) {
            String seed = (String) MoreObjects.firstNonNull(properties.get("level-seed"), "");
            long l = new Random().nextLong();
            if(!seed.isEmpty()) {
                try {
                    long m = Long.parseLong(seed);
                    if(m != 0L) {
                        l = m;
                    }
                } catch(NumberFormatException exception) {
                    l = seed.hashCode();
                }
            }

            String generate_structures = (String) properties.get("generate-structures");
            boolean generateStructures = generate_structures == null || Boolean.parseBoolean(generate_structures);
            Registry<DimensionType> dimensionTypes = dynamicRegistryManager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<Biome> biomes = dynamicRegistryManager.get(Registry.BIOME_KEY);
            Registry<ChunkGeneratorSettings> chunkGeneratorSettings = dynamicRegistryManager.get(Registry.NOISE_SETTINGS_WORLDGEN);
            SimpleRegistry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(dimensionTypes, biomes, chunkGeneratorSettings, l);


            prop = prop.substring(prop.indexOf(":") + 1);

            ConfigPack pack = TerraFabricPlugin.getInstance().getConfigRegistry().get(prop);

            if(pack == null) throw new IllegalArgumentException("No such pack " + prop);

            cir.setReturnValue(new TerraGeneratorOptions(l, generateStructures, false, method_28608(dimensionTypes, dimensionOptions, new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomes, l, pack), l, pack)), biomes, chunkGeneratorSettings));
        }
    }

    @Inject(method = "getDefaultOptions(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/registry/Registry;)Lnet/minecraft/world/gen/GeneratorOptions;", at = @At("HEAD"), cancellable = true)
    private static void injectDefaultOptions(Registry<DimensionType> registry, Registry<Biome> registry2, Registry<ChunkGeneratorSettings> registry3, CallbackInfoReturnable<GeneratorOptions> cir) {
        long l = (new Random()).nextLong();
        cir.setReturnValue(new TerraGeneratorOptions(l, true, false, method_28608(registry, DimensionType.createDefaultDimensionOptions(registry, registry2, registry3, l), createOverworldGenerator(registry2, registry3, l)), registry2, registry3));
    }
}
