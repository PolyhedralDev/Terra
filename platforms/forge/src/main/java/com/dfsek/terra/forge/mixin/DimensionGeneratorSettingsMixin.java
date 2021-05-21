package com.dfsek.terra.forge.mixin;

import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.forge.TerraForgePlugin;
import com.dfsek.terra.forge.generation.ForgeChunkGeneratorWrapper;
import com.dfsek.terra.forge.generation.TerraBiomeSource;
import com.google.common.base.MoreObjects;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Properties;
import java.util.Random;

@Mixin(DimensionGeneratorSettings.class)
public abstract class DimensionGeneratorSettingsMixin {
    @Inject(method = "create(Lnet/minecraft/util/registry/DynamicRegistries;Ljava/util/Properties;)Lnet/minecraft/world/gen/settings/DimensionGeneratorSettings;", at = @At("HEAD"), cancellable = true)
    private static void fromProperties(DynamicRegistries dynamicRegistries, Properties properties, CallbackInfoReturnable<DimensionGeneratorSettings> cir) {
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
            Registry<DimensionType> dimensionTypes = dynamicRegistries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
            Registry<Biome> biomes = dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY);
            Registry<DimensionSettings> chunkGeneratorSettings = dynamicRegistries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
            SimpleRegistry<Dimension> dimensionOptions = DimensionType.defaultDimensions(dimensionTypes, biomes, chunkGeneratorSettings, l);

            prop = prop.substring(prop.indexOf(":") + 1);

            ConfigPack pack = TerraForgePlugin.getInstance().getConfigRegistry().get(prop);

            if(pack == null) throw new IllegalArgumentException("No such pack " + prop);

            TerraForgePlugin.getInstance().logger().info("Using config pack " + pack.getTemplate().getID());
            cir.setReturnValue(new DimensionGeneratorSettings(l, generateStructures, false, DimensionGeneratorSettings.withOverworld(dimensionTypes, dimensionOptions, new ForgeChunkGeneratorWrapper(new TerraBiomeSource(biomes, l, pack), l, pack))));
        }
    }
}
