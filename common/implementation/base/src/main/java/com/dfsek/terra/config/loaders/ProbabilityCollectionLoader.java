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

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dfsek.terra.api.util.collection.ProbabilityCollection;


@SuppressWarnings("unchecked")
public class ProbabilityCollectionLoader implements TypeLoader<ProbabilityCollection<Object>> {
    @Override
    public ProbabilityCollection<Object> load(@NotNull AnnotatedType type, @NotNull Object o, @NotNull ConfigLoader configLoader,
                                              DepthTracker depthTracker) throws LoadException {
        ProbabilityCollection<Object> collection = new ProbabilityCollection<>();
        
        if(type instanceof AnnotatedParameterizedType pType) {
            AnnotatedType generic = pType.getAnnotatedActualTypeArguments()[0];
            if(o instanceof Map) {
                Map<Object, Object> map = (Map<Object, Object>) o;
                for(Map.Entry<Object, Object> entry : map.entrySet()) {
                    collection.add(configLoader.loadType(generic, entry.getKey(), depthTracker.entry((String) entry.getKey())),
                                   configLoader.loadType(Integer.class, entry.getValue(), depthTracker.entry((String) entry.getKey())));
                }
            } else if(o instanceof List) {
                List<Map<Object, Object>> map = (List<Map<Object, Object>>) o;
                if(map.size() == 1) {
                    Map<Object, Object> entry = map.get(0);
                    if(entry.size() == 1) {
                        for(Object value : entry.keySet()) {
                            return new ProbabilityCollection.Singleton<>(configLoader.loadType(generic, value, depthTracker));
                        }
                    }
                }
                for(int i = 0; i < map.size(); i++) {
                    Map<Object, Object> l = map.get(i);
                    for(Entry<Object, Object> entry : l.entrySet()) {
                        if(entry.getValue() == null) throw new LoadException("No probability defined for entry \"" + entry.getKey() + "\"",
                                                                             depthTracker);
                        Object val = configLoader.loadType(generic, entry.getKey(), depthTracker.index(i).entry((String) entry.getKey()));
                        collection.add(val,
                                       configLoader.loadType(Integer.class, entry.getValue(), depthTracker.entry((String) entry.getKey())));
                    }
                }
            } else if(o instanceof String) {
                return new ProbabilityCollection.Singleton<>(configLoader.loadType(generic, o, depthTracker));
            } else {
                throw new LoadException("Malformed Probability Collection: " + o, depthTracker);
            }
        } else throw new LoadException("Unable to load config! Could not retrieve parameterized type: " + type, depthTracker);
        
        
        return collection;
    }
    
}
