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

package com.dfsek.terra.registry.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;
import com.dfsek.terra.registry.OpenRegistryImpl;


public class ConfigTypeRegistry extends OpenRegistryImpl<ConfigType<?, ?>> {
    private static final Logger logger = LoggerFactory.getLogger(ConfigTypeRegistry.class);
    
    private final BiConsumer<String, ConfigType<?, ?>> callback;
    
    private final Platform platform;
    
    public ConfigTypeRegistry(Platform platform, BiConsumer<String, ConfigType<?, ?>> callback) {
        super(new LinkedHashMap<>()); // Ordered
        this.callback = callback;
        this.platform = platform;
    }
    
    @Override
    public boolean register(String identifier, Entry<ConfigType<?, ?>> value) {
        callback.accept(identifier, value.getValue());
        logger.debug("Registered config registry with ID {} to type {}", identifier,
                     ReflectionUtil.typeToString(value.getValue().getTypeKey().getType()));
        return super.register(identifier, value);
    }
}
