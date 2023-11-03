/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.chunkgenerator.palette;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class PaletteHolder {
    private final Palette[] palettes;
    private final int offset;
    
    protected PaletteHolder(Palette[] palettes, int offset) {
        this.palettes = palettes;
        this.offset = offset;
    }
    
    public Palette getPalette(int y) {
        int index = y + offset;
        return index >= 0
               ? index < palettes.length
                 ? palettes[index]
                 : palettes[palettes.length - 1]
               : palettes[0];
    }
    
    public static PaletteHolder of(List<Map<Palette, Integer>> palettes) {
        PaletteHolderBuilder builder = new PaletteHolderBuilder();
        for(Map<Palette, Integer> layer : palettes) {
            for(Entry<Palette, Integer> entry : layer.entrySet()) {
                builder.add(entry.getValue(), entry.getKey());
            }
        }
        return builder.build();
    }
    
    private static class PaletteHolderBuilder {
        private final TreeMap<Integer, Palette> paletteMap = new TreeMap<>();
        
        public PaletteHolderBuilder add(int y, Palette palette) {
            paletteMap.put(y, palette);
            return this;
        }
        
        public PaletteHolder build() {
            
            int min = Math.min(paletteMap.keySet().stream().min(Integer::compareTo).orElse(0), 0);
            int max = Math.max(paletteMap.keySet().stream().max(Integer::compareTo).orElse(255), 255);
            
            Palette[] palettes = new Palette[paletteMap.lastKey() + 1 - min];
            for(int y = min; y <= Math.max(paletteMap.lastKey(), max); y++) {
                Palette d = null;
                for(Entry<Integer, Palette> e : paletteMap.entrySet()) {
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
}
