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
import java.util.LinkedHashMap;
import java.util.Map;


@SuppressWarnings("unchecked")
public class LinkedHashMapLoader implements TypeLoader<LinkedHashMap<Object, Object>> {
    @Override
    public LinkedHashMap<Object, Object> load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> config = (Map<String, Object>) c;
        LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
        if(t instanceof AnnotatedParameterizedType) {
            AnnotatedParameterizedType pType = (AnnotatedParameterizedType) t;
            AnnotatedType key = pType.getAnnotatedActualTypeArguments()[0];
            AnnotatedType value = pType.getAnnotatedActualTypeArguments()[1];
            for(Map.Entry<String, Object> entry : config.entrySet()) {
                map.put(loader.loadType(key, entry.getKey()), loader.loadType(value, entry.getValue()));
            }
        } else throw new LoadException("Unable to load config");
        
        return map;
    }
}
