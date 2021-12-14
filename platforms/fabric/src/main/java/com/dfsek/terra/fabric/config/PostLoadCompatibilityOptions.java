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

package com.dfsek.terra.fabric.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.api.world.biome.Biome;


@SuppressWarnings("FieldMayBeFinal")
public class PostLoadCompatibilityOptions implements ConfigTemplate {
    @Value("structures.inject-biome.exclude-biomes")
    @Default
    private Map<Biome, Set<Identifier>> excludedPerBiomeStructures = new HashMap<>();
    
    @Value("features.inject-biome.exclude-biomes")
    @Default
    private Map<Biome, Set<Identifier>> excludedPerBiomeFeatures = new HashMap<>();
    
    public Map<Biome, Set<Identifier>> getExcludedPerBiomeFeatures() {
        return excludedPerBiomeFeatures;
    }
    
    public Map<Biome, Set<Identifier>> getExcludedPerBiomeStructures() {
        return excludedPerBiomeStructures;
    }
}
