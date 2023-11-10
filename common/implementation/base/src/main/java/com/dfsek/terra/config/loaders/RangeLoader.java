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

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.tectonic.impl.MapConfiguration;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.api.util.Range;


public class RangeLoader implements TypeLoader<Range> {
    @Override
    public Range load(@NotNull AnnotatedType type, @NotNull Object o, @NotNull ConfigLoader configLoader, DepthTracker depthTracker)
    throws LoadException {
        if(o instanceof Map) {
            return configLoader.load(new RangeMapTemplate(), new MapConfiguration((Map<String, Object>) o), depthTracker).get();
        } else {
            int h = configLoader.loadType(Integer.class, o, depthTracker);
            return new ConstantRange(h, h + 1);
        }
    }
    
    /*
     * Template needed so keys can be meta annotated, otherwise the loader could just grab keys directly from the object
     */
    private static class RangeMapTemplate implements ObjectTemplate<Range> {
        @Value("min")
        private @Meta int min;
        
        @Value("max")
        private @Meta int max;
        
        @Override
        public Range get() {
            return new ConstantRange(min, max);
        }
    }
}
