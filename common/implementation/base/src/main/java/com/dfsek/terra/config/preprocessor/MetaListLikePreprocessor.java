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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.api.config.meta.Meta;


public class MetaListLikePreprocessor extends MetaPreprocessor<Meta> {
    public MetaListLikePreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, Meta annotation) {
        if(t.getType() instanceof ParameterizedType parameterizedType) {
            if(parameterizedType.getRawType() instanceof Class<?> baseClass) { // Should always be true but we check anyways
    
                if((List.class.isAssignableFrom(baseClass) || Set.class.isAssignableFrom(baseClass)) &&
                   c instanceof List) { // List or set metaconfig
                    List<Object> list = (List<Object>) c;
                    
                    int offset = 0;
                    List<Object> newList = new ArrayList<>((List<Object>) c);
                    
                    for(int i = 0; i < list.size(); i++) {
                        Object o = list.get(i);
                        if(!(o instanceof String)) continue;
                        String s = ((String) o).trim();
                        if(!s.startsWith("<< ")) continue;
                        String meta = s.substring(3);
                        
                        Object metaValue = getMetaValue(meta);
                        
                        if(!(metaValue instanceof List)) {
                            throw new LoadException(
                                    "MetaList/Set injection candidate must be list, is type " + metaValue.getClass().getCanonicalName());
                        }
                        
                        List<Object> metaList = (List<Object>) metaValue;
                        
                        newList.remove(i + offset); // Remove placeholder
                        newList.addAll(i + offset, metaList); // Add metalist values where placeholder was
                        offset += metaList.size() - 1; // add metalist size to offset, subtract one to account for placeholder.
                    }
                    
                    return (Result<T>) Result.overwrite(newList);
                }
            }
        }
        
        return Result.noOp();
    }
}
