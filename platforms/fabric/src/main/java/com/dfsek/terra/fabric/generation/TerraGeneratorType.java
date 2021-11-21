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
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.event.BiomeRegistrationEvent;


@Environment(EnvType.CLIENT)
public class TerraGeneratorType extends GeneratorType {
    private final ConfigPack pack;
    
    public TerraGeneratorType(ConfigPack pack) {
        super("terra." + pack.getID());
        this.pack = pack;
    }
    
    @Override
    public GeneratorOptions createDefaultOptions(DynamicRegistryManager.Impl registryManager, long seed, boolean generateStructures,
                                                 boolean bonusChest) {
        GeneratorOptions options = super.createDefaultOptions(registryManager, seed, generateStructures, bonusChest);
        FabricEntryPoint.getPlatform().getEventManager().callEvent(new BiomeRegistrationEvent(registryManager)); // register biomes
        return options;
    }
    
    @Override
    protected ChunkGenerator getChunkGenerator(DynamicRegistryManager manager, long seed) {
        return new FabricChunkGeneratorWrapper(new TerraBiomeSource(manager.get(Registry.BIOME_KEY), seed, pack), seed, pack);
    }
}
