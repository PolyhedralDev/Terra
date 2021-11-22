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

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.preprocessor.ValuePreprocessor;

import java.lang.annotation.Annotation;
import java.util.Map;


public abstract class MetaPreprocessor<A extends Annotation> implements ValuePreprocessor<A> {
    private final Map<String, Configuration> configs;
    
    public MetaPreprocessor(Map<String, Configuration> configs) {
        this.configs = configs;
    }
    
    protected Object getMetaValue(String meta) {
        int sep = meta.indexOf(':');
        String file = meta.substring(0, sep);
        String key = meta.substring(sep + 1);
        
        if(!configs.containsKey(file)) throw new LoadException("Cannot fetch metavalue: No such config: " + file);
        
        Configuration config = configs.get(file);
        
        if(!config.contains(key)) {
            throw new LoadException("Cannot fetch metavalue: No such key " + key + " in configuration " + config.getName());
        }
        
        return config.get(key);
    }
}
