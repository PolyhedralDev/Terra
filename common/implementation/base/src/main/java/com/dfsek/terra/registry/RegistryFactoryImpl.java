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

package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.AnnotatedType;
import java.util.function.Function;

import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.meta.RegistryFactory;
import com.dfsek.terra.api.util.generic.Lazy;


public class RegistryFactoryImpl implements RegistryFactory {
    @Override
    public <T> OpenRegistry<T> create() {
        return new OpenRegistryImpl<>();
    }
    
    @Override
    public <T> OpenRegistry<T> create(Function<OpenRegistry<T>, TypeLoader<T>> loader) {
        return new OpenRegistryImpl<>() {
            private final Lazy<TypeLoader<T>> loaderCache = Lazy.lazy(() -> loader.apply(this));
            
            @Override
            public T load(AnnotatedType type, Object o, ConfigLoader configLoader) throws LoadException {
                return loaderCache.value().load(type, o, configLoader);
            }
        };
    }
}
