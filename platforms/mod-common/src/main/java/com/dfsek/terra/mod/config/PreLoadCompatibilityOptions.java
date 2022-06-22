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

package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.properties.Properties;


@SuppressWarnings("FieldMayBeFinal")
public class PreLoadCompatibilityOptions implements ConfigTemplate, Properties {
    @Value("minecraft.use-vanilla-biomes")
    @Default
    private boolean vanillaBiomes = false;
    
    @Value("minecraft.beard.enable")
    @Default
    private boolean beard = true;
    
    @Value("minecraft.beard.threshold")
    @Default
    private double beardThreshold = 0.5;
    
    @Value("minecraft.beard.air-threshold")
    @Default
    private double airThreshold = -0.5;
    
    public boolean useVanillaBiomes() {
        return vanillaBiomes;
    }
    
    public boolean isBeard() {
        return beard;
    }
    
    public double getBeardThreshold() {
        return beardThreshold;
    }
    
    public double getAirThreshold() {
        return airThreshold;
    }
}
