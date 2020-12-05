package com.dfsek.terra.biome.palette;

import com.dfsek.terra.util.DataUtil;
import net.jafama.FastMath;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.world.palette.Palette;

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
        Palette<BlockData>[] palettes = new Palette[paletteMap.lastKey() + 1];
        for(int y = 0; y <= FastMath.max(paletteMap.lastKey(), 255); y++) {
            Palette<BlockData> d = DataUtil.BLANK_PALETTE;
            for(Map.Entry<Integer, Palette<BlockData>> e : paletteMap.entrySet()) {
                if(e.getKey() >= y) {
                    d = e.getValue();
                    break;
                }
            }
            palettes[y] = d;
        }
        return new PaletteHolder(palettes);
    }
}
