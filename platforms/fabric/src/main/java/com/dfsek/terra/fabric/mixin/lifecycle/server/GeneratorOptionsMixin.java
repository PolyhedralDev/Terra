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

import com.dfsek.terra.api.registry.CheckedRegistry;

import net.minecraft.server.dedicated.ServerPropertiesHandler;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;
import java.util.Random;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.PlatformImpl;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;


@Mixin(GeneratorOptions.class)
public abstract class GeneratorOptionsMixin {
    @Inject(method = "fromProperties(Lnet/minecraft/util/registry/DynamicRegistryManager;" +
                     "Lnet/minecraft/server/dedicated/ServerPropertiesHandler$WorldGenProperties;)" +
                     "Lnet/minecraft/world/gen/GeneratorOptions;",
            at = @At("HEAD"),
            cancellable = true)
    private static void fromProperties(DynamicRegistryManager manager,
                                       ServerPropertiesHandler.WorldGenProperties properties,
                                       CallbackInfoReturnable<GeneratorOptions> cir) {
        if(properties.levelType() == null) {
            return;
        }
        
        PlatformImpl main = FabricEntryPoint.getPlatform();
        
        String levelType = properties.levelType();
        
        if(levelType.toLowerCase(Locale.ROOT).startsWith("terra")) {
            String seedProperty = properties.levelSeed();
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
            
            boolean generateStructures = properties.generateStructures();
            Registry<DimensionType> dimensionTypes = manager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<Biome> biomeRegistry = manager.get(Registry.BIOME_KEY);
            Registry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(manager, seed, false);
            
            Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = manager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
            RegistryEntry<ChunkGeneratorSettings>
                    settingsSupplier = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.OVERWORLD).orElseThrow();
            Registry<StructureSet> noiseRegistry = manager.get(Registry.STRUCTURE_SET_KEY);
            
            String pack = levelType.substring(levelType.indexOf(":") + 1);
            
            CheckedRegistry<ConfigPack> configRegistry = main.getConfigRegistry();
            ConfigPack config = configRegistry
                    .getByID(pack)
                    .or(() -> configRegistry.getByID(pack.toUpperCase(Locale.ROOT)))
                    .orElseThrow(() -> new IllegalArgumentException("No such pack " + pack));
            
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
