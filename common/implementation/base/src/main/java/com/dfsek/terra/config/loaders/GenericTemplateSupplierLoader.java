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

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.tectonic.impl.MapConfiguration;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedType;
import java.util.Map;
import java.util.function.Supplier;

import com.dfsek.terra.api.registry.Registry;


public class GenericTemplateSupplierLoader<T> implements TypeLoader<T> {
    private final Registry<Supplier<ObjectTemplate<T>>> registry;
    
    public GenericTemplateSupplierLoader(Registry<Supplier<ObjectTemplate<T>>> registry) {
        this.registry = registry;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public T load(@NotNull AnnotatedType t, @NotNull Object c, ConfigLoader loader, DepthTracker depthTracker) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) c;
        String type = (String) map.get("type");
        return loader
                .load(registry.getByID(type)
                              .orElseThrow(() -> new LoadException("No such entry: " + map.get("type"), depthTracker))
                              .get(), new MapConfiguration(map), depthTracker.intrinsic("With type \"" + type + "\"")).get();
        
    }
}
