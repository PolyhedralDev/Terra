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
import com.dfsek.tectonic.api.depth.IndexLevel;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.preprocessor.Result;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class MetaListLikePreprocessor extends MetaPreprocessor<Meta> {
    public MetaListLikePreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, Meta annotation, DepthTracker depthTracker) {
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
                        
                        
                        Pair<Configuration, Object> pair = getMetaValue(meta, depthTracker);
                        Object metaValue = pair.getRight();
                        
                        if(!(metaValue instanceof List)) {
                            throw new LoadException(
                                    "MetaList/Set injection candidate must be list, is type " + metaValue.getClass().getCanonicalName(),
                                    depthTracker);
                        }
                        
                        List<Object> metaList = (List<Object>) metaValue;
                        
                        newList.remove(i + offset); // Remove placeholder
                        newList.addAll(i + offset, metaList); // Add metalist values where placeholder was
                        
                        int begin = i + offset;
                        offset += metaList.size() - 1; // add metalist size to offset, subtract one to account for placeholder.
                        int end = i + offset;
                        depthTracker.addIntrinsicLevel(level -> {
                            if(level instanceof IndexLevel indexLevel &&
                               indexLevel.getIndex() >= begin &&
                               indexLevel.getIndex() <= end) {
                                String configName;
                                if(pair.getLeft().getName() == null) {
                                    configName = "Anonymous Configuration";
                                } else {
                                    configName = pair.getLeft().getName();
                                }
                                return Optional.of("From configuration \"" + configName + "\"");
                            }
                            return Optional.empty();
                        });
                    }
                    
                    return (Result<T>) Result.overwrite(newList, depthTracker);
                }
            }
        }
        
        return Result.noOp();
    }
}
