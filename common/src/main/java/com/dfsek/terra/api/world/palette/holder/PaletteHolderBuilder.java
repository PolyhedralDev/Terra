package com.dfsek.terra.api.world.palette.holder;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.palette.Palette;
import net.jafama.FastMath;

import java.util.Map;
import java.util.TreeMap;

public class PaletteHolderBuilder {
    private final TreeMap<Integer, Palette<BlockData>> paletteMap = new TreeMap<>();

    public PaletteHolderBuilder add(int y, Palette<BlockData> palette) {
        paletteMap.put(y, palette);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    public PaletteHolder build() {

        int min = FastMath.min(paletteMap.keySet().stream().min(Integer::compareTo).orElse(0), 0);
        int max = FastMath.max(paletteMap.keySet().stream().max(Integer::compareTo).orElse(255), 255);

        Palette<BlockData>[] palettes = new Palette[paletteMap.lastKey() + 1 - min];
        for(int y = min; y <= FastMath.max(paletteMap.lastKey(), max); y++) {
            Palette<BlockData> d = null;
            for(Map.Entry<Integer, Palette<BlockData>> e : paletteMap.entrySet()) {
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
