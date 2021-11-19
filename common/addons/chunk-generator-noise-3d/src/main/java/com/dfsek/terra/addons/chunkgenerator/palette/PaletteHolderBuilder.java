/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.palette;

import net.jafama.FastMath;

import java.util.Map;
import java.util.TreeMap;

import com.dfsek.terra.api.world.generator.Palette;


public class PaletteHolderBuilder {
    private final TreeMap<Integer, Palette> paletteMap = new TreeMap<>();
    
    public PaletteHolderBuilder add(int y, Palette palette) {
        paletteMap.put(y, palette);
        return this;
    }
    
    public PaletteHolder build() {
        
        int min = FastMath.min(paletteMap.keySet().stream().min(Integer::compareTo).orElse(0), 0);
        int max = FastMath.max(paletteMap.keySet().stream().max(Integer::compareTo).orElse(255), 255);
        
        Palette[] palettes = new Palette[paletteMap.lastKey() + 1 - min];
        for(int y = min; y <= FastMath.max(paletteMap.lastKey(), max); y++) {
            Palette d = null;
            for(Map.Entry<Integer, Palette> e : paletteMap.entrySet()) {
                if(e.getKey() >= y) {
                    d = e.getValue();
                    break;
                }
            }
            if(d == null) throw new IllegalArgumentException("No palette for Y=" + y);
            palettes[y - min] = d;
        }
        return new PaletteHolder(palettes, -min);
    }
}
