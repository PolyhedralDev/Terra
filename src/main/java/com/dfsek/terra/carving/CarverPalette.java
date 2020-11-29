package com.dfsek.terra.carving;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.ProbabilityCollection;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
public class CarverPalette {
    private final boolean blacklist;
    private final Set<Material> replace;
    private final TreeMap<Integer, ProbabilityCollection<BlockData>> map = new TreeMap<>();
    private ProbabilityCollection<BlockData>[] layers;

    public CarverPalette(Set<Material> replaceable, boolean blacklist) {
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

    public boolean canReplace(Material material) {
        return blacklist != replace.contains(material);
    }

    /**
     * Build the palette to an array.
     */
    public void build() {
        int size = map.lastKey() + 1;
        layers = new ProbabilityCollection[size];
        for(int y = 0; y < size; y++) {
            ProbabilityCollection<BlockData> d = new ProbabilityCollection<BlockData>().add(Material.AIR.createBlockData(), 1); // Blank layer
            for(Map.Entry<Integer, ProbabilityCollection<BlockData>> e : map.entrySet()) {
                if(e.getKey() >= y) {
                    d = e.getValue();
                    break;
                }
            }
            layers[y] = d;
        }
    }
}
