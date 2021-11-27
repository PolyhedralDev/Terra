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
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.preprocessor.Result;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class MetaMapPreprocessor extends MetaPreprocessor<Meta> {
    private static final TypeKey<List<String>> STRING_LIST = new TypeKey<>() {
    };
    
    public MetaMapPreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, Meta annotation) {
        if(t.getType() instanceof ParameterizedType parameterizedType) {
            if(parameterizedType.getRawType() instanceof Class<?> baseClass) { // Should always be true but we check anyways
    
                if(Map.class.isAssignableFrom(baseClass) && c instanceof Map) { // Map metaconfig
                    Map<Object, Object> map = (Map<Object, Object>) c;
                    
                    Map<Object, Object> newMap = new HashMap<>(map);
                    
                    if(map.containsKey("<<")) {
                        newMap.putAll(map);
                        newMap.remove("<<"); // Remove placeholder
                        
                        List<String> keys = (List<String>) loader.loadType(STRING_LIST.getAnnotatedType(), map.get("<<"));
                        keys.forEach(key -> {
                            Object meta = getMetaValue(key);
                            if(!(meta instanceof Map)) {
                                throw new LoadException(
                                        "MetaMap injection candidate must be list, is type " + meta.getClass().getCanonicalName());
                            }
                            newMap.putAll((Map<?, ?>) meta);
                        });
                        return (Result<T>) Result.overwrite(newMap);
                    }
                    
                    
                }
            }
        }
        
        return Result.noOp();
    }
}
