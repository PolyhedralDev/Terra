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

package com.dfsek.terra.config.preprocessor;

import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.preprocessor.ValuePreprocessor;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.dfsek.terra.api.util.generic.pair.Pair;


public abstract class MetaPreprocessor<A extends Annotation> implements ValuePreprocessor<A> {
    private final Map<String, Configuration> configs;
    
    public MetaPreprocessor(Map<String, Configuration> configs) {
        this.configs = configs;
    }
    
    protected Pair<Configuration, Object> getMetaValue(String meta, DepthTracker depthTracker) {
        int sep = meta.indexOf(':');
        String file = meta.substring(0, sep);
        String key = meta.substring(sep + 1);
        
        if(!configs.containsKey(file)) throw new LoadException("Cannot fetch metavalue: No such config: " + file, depthTracker);
        
        Configuration config = configs.get(file);
        
        if(!config.contains(key)) {
            throw new LoadException("Cannot fetch metavalue: No such key " + key + " in configuration " + config.getName(), depthTracker);
        }
        
        return Pair.of(config, config.get(key));
    }
}
