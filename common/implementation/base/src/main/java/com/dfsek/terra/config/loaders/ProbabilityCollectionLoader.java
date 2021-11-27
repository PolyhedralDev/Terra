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

package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.util.collection.ProbabilityCollection;


@SuppressWarnings("unchecked")
public class ProbabilityCollectionLoader implements TypeLoader<ProbabilityCollection<Object>> {
    @Override
    public ProbabilityCollection<Object> load(AnnotatedType type, Object o, ConfigLoader configLoader) throws LoadException {
        ProbabilityCollection<Object> collection = new ProbabilityCollection<>();
        
        if(type instanceof AnnotatedParameterizedType pType) {
            AnnotatedType generic = pType.getAnnotatedActualTypeArguments()[0];
            if(o instanceof Map) {
                Map<Object, Integer> map = (Map<Object, Integer>) o;
                for(Map.Entry<Object, Integer> entry : map.entrySet()) {
                    collection.add(configLoader.loadType(generic, entry.getKey()), entry.getValue());
                }
            } else if(o instanceof List) {
                List<Map<Object, Integer>> map = (List<Map<Object, Integer>>) o;
                for(Map<Object, Integer> l : map) {
                    for(Map.Entry<Object, Integer> entry : l.entrySet()) {
                        if(entry.getValue() == null) throw new LoadException("No probability defined for entry \"" + entry.getKey() + "\"");
                        Object val = configLoader.loadType(generic, entry.getKey());
                        collection.add(val, entry.getValue());
                    }
                }
            } else if(o instanceof String) {
                return new ProbabilityCollection.Singleton<>(configLoader.loadType(generic, o));
            } else {
                throw new LoadException("Malformed Probability Collection: " + o);
            }
        } else throw new LoadException("Unable to load config! Could not retrieve parameterized type: " + type);
        
        
        return collection;
    }
    
}
