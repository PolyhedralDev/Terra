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
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.preprocessor.Result;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.generic.pair.Pair;


public class MetaValuePreprocessor extends MetaPreprocessor<Meta> {
    
    public MetaValuePreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader configLoader, Meta annotation, DepthTracker depthTracker) {
        if(c instanceof String) { // Can we do standard metaconfig?
            String value = ((String) c).trim();
            if(value.startsWith("$") // it's a meta value.
               && !value.startsWith("${")) { // it's not a meta string template.
                Pair<Configuration, Object> pair = getMetaValue(value.substring(1), depthTracker);
                
                String configName;
                if(pair.getLeft().getName() == null) {
                    configName = "Anonymous Configuration";
                } else {
                    configName = pair.getLeft().getName();
                }
                
                return (Result<T>) Result.overwrite(pair.getRight(), depthTracker.intrinsic("From configuration \"" + configName + "\""));
            }
        }
        return Result.noOp();
    }
}
