package com.dfsek.terra.api.world.palette.holder;

import com.dfsek.terra.api.world.palette.Palette;
import net.jafama.FastMath;

import java.util.Map;
import java.util.TreeMap;

public class PaletteHolderBuilder {
    private final TreeMap<Integer, Palette> paletteMap = new TreeMap<>();

    public PaletteHolderBuilder add(int y, Palette palette) {
        paletteMap.put(y, palette);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    public PaletteHolder build() {
        Palette[] palettes = new Palette[paletteMap.lastKey() + 1];
        for(int y = 0; y <= FastMath.max(paletteMap.lastKey(), 255); y++) {
            Palette d = null;
            for(Map.Entry<Integer, Palette> e : paletteMap.entrySet()) {
                if(e.getKey() >= y) {
                    d = e.getValue();
                    break;
                }
            }
            if(d == null) throw new IllegalArgumentException("No palette for Y=" + y);
            palettes[y] = d;
        }
        return new PaletteHolder(palettes);
    }
}
