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
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.preprocessor.Result;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

import com.dfsek.terra.api.config.meta.Meta;


public class MetaStringPreprocessor extends MetaPreprocessor<Meta> {
    public MetaStringPreprocessor(Map<String, Configuration> configs) {
        super(configs);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Result<T> process(AnnotatedType t, T c, ConfigLoader loader, Meta annotation, DepthTracker depthTracker) {
        if(String.class.equals(t.getType()) && c instanceof String candidate) { // String is final so we use #equals
            StringSubstitutor substitutor = new StringSubstitutor(key -> {
                Object meta = getMetaValue(key, depthTracker).getRight();
                if(!(meta instanceof String) && !(meta instanceof Number) && !(meta instanceof Character) && !(meta instanceof Boolean)) {
                    throw new LoadException("MetaString template injection candidate must be string or primitive, is type " +
                                            meta.getClass().getCanonicalName(), depthTracker);
                }
                return meta.toString();
            });
            return (Result<T>) Result.overwrite(substitutor.replace(candidate), depthTracker);
        }
        return Result.noOp();
    }
    
    
}
