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

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("FieldMayBeFinal")
public class PreLoadCompatibilityOptions implements ConfigTemplate {
    @Value("features.inject-registry.enable")
    @Default
    private boolean doRegistryInjection = false;
    
    @Value("features.inject-biome.enable")
    @Default
    private boolean doBiomeInjection = false;
    
    @Value("features.inject-registry.excluded-features")
    @Default
    private Set<Identifier> excludedRegistryFeatures = new HashSet<>();
    
    @Value("features.inject-biome.excluded-features")
    @Default
    private Set<Identifier> excludedBiomeFeatures = new HashSet<>();
    
    @Value("structures.inject-biome.excluded-features")
    @Default
    private Set<Identifier> excludedBiomeStructures = new HashSet<>();
    
    public boolean doBiomeInjection() {
        return doBiomeInjection;
    }
    
    public boolean doRegistryInjection() {
        return doRegistryInjection;
    }
    
    public Set<Identifier> getExcludedBiomeFeatures() {
        return excludedBiomeFeatures;
    }
    
    public Set<Identifier> getExcludedRegistryFeatures() {
        return excludedRegistryFeatures;
    }
    
    public Set<Identifier> getExcludedBiomeStructures() {
        return excludedBiomeStructures;
    }
}
