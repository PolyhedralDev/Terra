package com.dfsek.terra.fabric.mixin.lifecycle.server;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.TerraPluginImpl;
import com.dfsek.terra.fabric.event.BiomeRegistrationEvent;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Properties;
import java.util.Random;

@Mixin(GeneratorOptions.class)
public abstract class GeneratorOptionsMixin {
    @Inject(method = "fromProperties(Lnet/minecraft/util/registry/DynamicRegistryManager;Ljava/util/Properties;)Lnet/minecraft/world/gen/GeneratorOptions;", at = @At("HEAD"), cancellable = true)
    private static void fromProperties(DynamicRegistryManager registryManager, Properties properties, CallbackInfoReturnable<GeneratorOptions> cir) {
        if(properties.get("level-type") == null) {
            return;
        }

        TerraPluginImpl main = FabricEntryPoint.getTerraPlugin();

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
            Registry<DimensionType> dimensionTypes = registryManager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
            Registry<ChunkGeneratorSettings> chunkGeneratorSettings = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
            SimpleRegistry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(dimensionTypes, biomeRegistry, chunkGeneratorSettings, l);

            prop = prop.substring(prop.indexOf(":") + 1);

            ConfigPack config = main.getConfigRegistry().get(prop);

            if(config == null) throw new IllegalArgumentException("No such pack " + prop);

            main.getEventManager().callEvent(new BiomeRegistrationEvent(registryManager)); // register biomes

            cir.setReturnValue(new GeneratorOptions(l, generateStructures, false, GeneratorOptions.getRegistryWithReplacedOverworldGenerator(dimensionTypes, dimensionOptions, new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, l, config), l, config))));
        }
    }
}
