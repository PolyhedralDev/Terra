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

package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.math.GridSpawn;


@SuppressWarnings("unchecked")
public class GridSpawnLoader implements TypeLoader<StructureSpawn> {
    @Override
    public StructureSpawn load(AnnotatedType type, Object o, ConfigLoader configLoader) {
        Map<String, Integer> map = (Map<String, Integer>) o;
        return new GridSpawn(map.get("width"), map.get("padding"), map.getOrDefault("salt", 0));
    }
}
