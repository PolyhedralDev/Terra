package com.dfsek.terra.fabric.mixin;

import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.world.TerraBiomeSource;
import com.dfsek.terra.fabric.world.generator.FabricChunkGeneratorWrapper;
import com.google.common.base.MoreObjects;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Properties;
import java.util.Random;

// Mixins commented out until loom fixes multi-project builds.

//@Mixin(GeneratorOptions.class)
public class MixinGeneratorOptions {
    //@Inject(method = "fromProperties(Lnet/minecraft/util/registry/DynamicRegistryManager;Ljava/util/Properties;)Lnet/minecraft/world/gen/GeneratorOptions;", at = @At("HEAD"), cancellable = true)
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

            cir.setReturnValue(new GeneratorOptions(l, generateStructures, false, GeneratorOptions.method_28608(dimensionTypes, dimensionOptions, new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomes, l, pack), l, pack))));
        }
    }
}
