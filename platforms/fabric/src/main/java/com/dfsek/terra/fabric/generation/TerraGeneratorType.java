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

package com.dfsek.terra.fabric.generation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import com.dfsek.terra.api.config.ConfigPack;


@Environment(EnvType.CLIENT)
public class TerraGeneratorType extends GeneratorType {
    private final ConfigPack pack;
    
    public TerraGeneratorType(ConfigPack pack) {
        super("terra." + pack.getID());
        this.pack = pack;
    }
    
    @Override
    protected ChunkGenerator getChunkGenerator(DynamicRegistryManager manager, long seed) {
        Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = manager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
        RegistryEntry<ChunkGeneratorSettings>
                settingsSupplier = chunkGeneratorSettingsRegistry.getEntry(ChunkGeneratorSettings.OVERWORLD).orElseThrow();
        Registry<StructureSet> noiseRegistry = manager.get(Registry.STRUCTURE_SET_KEY);
        return new FabricChunkGeneratorWrapper(noiseRegistry, new TerraBiomeSource(manager.get(Registry.BIOME_KEY), seed, pack), seed, pack,
                                               settingsSupplier);
    }
}
