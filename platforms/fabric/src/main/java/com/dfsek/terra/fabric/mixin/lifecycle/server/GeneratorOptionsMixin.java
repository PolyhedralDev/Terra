/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.fabric.mixin.lifecycle.server;

import net.minecraft.structure.StructureSet;
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
import java.util.function.Supplier;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.PlatformImpl;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;


@Mixin(GeneratorOptions.class)
public abstract class GeneratorOptionsMixin {
    @Inject(method = "fromProperties(Lnet/minecraft/util/registry/DynamicRegistryManager;Ljava/util/Properties;)" +
                     "Lnet/minecraft/world/gen/GeneratorOptions;",
            at = @At("HEAD"),
            cancellable = true)
    private static void fromProperties(DynamicRegistryManager manager, Properties properties,
                                       CallbackInfoReturnable<GeneratorOptions> cir) {
        if(properties.get("level-type") == null) {
            return;
        }
        
        PlatformImpl main = FabricEntryPoint.getPlatform();
        
        String prop = properties.get("level-type").toString().trim();
        if(prop.startsWith("Terra")) {
            String seedProperty = (String) properties.get("level-seed");
            long seed = new Random().nextLong();
            if(seedProperty != null) {
                try {
                    long m = Long.parseLong(seedProperty);
                    if(m != 0L) {
                        seed = m;
                    }
                } catch(NumberFormatException exception) {
                    seed = seedProperty.hashCode();
                }
            }
            
            String generate_structures = (String) properties.get("generate-structures");
            boolean generateStructures = generate_structures == null || Boolean.parseBoolean(generate_structures);
            Registry<DimensionType> dimensionTypes = manager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<Biome> biomeRegistry = manager.get(Registry.BIOME_KEY);
            SimpleRegistry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(manager, seed, false);
            
            Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = manager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
            Supplier<ChunkGeneratorSettings>
                    settingsSupplier = () -> chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD);
            Registry<StructureSet> noiseRegistry = manager.get(Registry.STRUCTURE_SET_KEY);
            
            prop = prop.substring(prop.indexOf(":") + 1);
            
            String finalProp = prop;
            ConfigPack config = main.getConfigRegistry().getByID(prop).orElseThrow(() -> new IllegalArgumentException(
                    "No such pack " + finalProp));
            
            cir.setReturnValue(
                    new GeneratorOptions(seed,
                                         generateStructures,
                                         false,
                                         GeneratorOptions
                                                 .getRegistryWithReplacedOverworldGenerator(
                                                         dimensionTypes,
                                                         dimensionOptions,
                                                         new FabricChunkGeneratorWrapper(noiseRegistry,
                                                                                         new TerraBiomeSource(biomeRegistry, seed, config),
                                                                                         seed,
                                                                                         config,
                                                                                         settingsSupplier))));
        }
    }
}
