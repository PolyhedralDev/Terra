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

package com.dfsek.terra.transform;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.transform.Transform;
import com.dfsek.terra.api.transform.exception.TransformException;


public class MapTransform<F, T> implements Transform<F, T> {
    private final Map<F, T> map;
    
    public MapTransform(Map<F, T> map) {
        this.map = map;
    }
    
    public MapTransform() {
        this.map = new HashMap<>();
    }
    
    public MapTransform<F, T> add(F from, T to) {
        map.put(from, to);
        return this;
    }
    
    public MapTransform<F, T> remove(F from) {
        map.remove(from);
        return this;
    }
    
    @Override
    public T transform(F input) throws TransformException {
        if(!map.containsKey(input)) throw new TransformException("No key matching " + input.toString() + " found in map.");
        return map.get(input);
    }
}
