package com.dfsek.terra.carving;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;

import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
public class CarverPalette {
    private final boolean blacklist;
    private final MaterialSet replace;
    private final TreeMap<Integer, ProbabilityCollection<BlockData>> map = new TreeMap<>();
    private ProbabilityCollection<BlockData>[] layers;

    public CarverPalette(MaterialSet replaceable, boolean blacklist) {
        this.blacklist = blacklist;
        this.replace = replaceable;
    }

    public CarverPalette add(ProbabilityCollection<BlockData> collection, int y) {
        map.put(y, collection);
        return this;
    }

    public ProbabilityCollection<BlockData> get(int y) {
        return layers[y];
    }

    public boolean canReplace(MaterialData material) {
        return blacklist != replace.contains(material);
    }

    /**
     * Build the palette to an array.
     */
    public void build() {
        int size = map.lastKey() + 1;
        layers = new ProbabilityCollection[size];
        for(int y = 0; y < size; y++) {
            ProbabilityCollection<BlockData> d = null;
            for(Map.Entry<Integer, ProbabilityCollection<BlockData>> e : map.entrySet()) {
                if(e.getKey() >= y) {
                    d = e.getValue();
                    break;
                }
            }
            if(d == null) throw new IllegalArgumentException("Null collection at Y=" + y);
            layers[y] = d;
        }
    }
}
