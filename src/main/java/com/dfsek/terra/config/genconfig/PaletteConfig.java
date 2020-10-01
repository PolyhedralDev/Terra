package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.TerraConfigObject;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.palette.Palette;
import org.polydev.gaea.world.palette.RandomPalette;
import org.polydev.gaea.world.palette.SimplexPalette;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PaletteConfig extends TerraConfigObject {
    private static final Map<String, PaletteConfig> palettes = new HashMap<>();
    private Palette<BlockData> palette;
    private String paletteID;
    private boolean useNoise = false;
    public PaletteConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    @Override
    public void init() throws InvalidConfigurationException {
        if(!contains("id")) throw new ConfigException("Palette ID unspecified!", "null");
        this.paletteID = getString("id");
        Palette<BlockData> pal;
        if(getBoolean("simplex", false)) {
            useNoise = true;
            FastNoise pNoise = new FastNoise(getInt("seed", 3));
            pNoise.setNoiseType(FastNoise.NoiseType.SimplexFractal);
            pNoise.setFractalOctaves(4);
            pNoise.setFrequency((float) getDouble("frequency", 0.02));
            pal = new SimplexPalette<>(pNoise);
        } else pal = new RandomPalette<>(new Random(getInt("seed", 3)));
        palette = getPalette(getMapList("blocks"), pal);
        palettes.put(paletteID, this);
    }

    public Palette<BlockData> getPalette() {
        return palette;
    }

    public String getID() {
        return paletteID;
    }

    @SuppressWarnings("unchecked")
    protected static Palette<BlockData> getPalette(List<Map<?, ?>> maps, Palette<BlockData> p) throws InvalidConfigurationException {
        for(Map<?, ?> m : maps) {
            try {
                ProbabilityCollection<BlockData> layer = new ProbabilityCollection<>();
                List<Map<?, ?>> map = (List<Map<?, ?>>) m.get("materials");
                if(map.size() > 1) {
                    for(Map<?, ?> entry : map) {
                        for(Map.Entry<?, ?> type : entry.entrySet()) {
                            layer.add(Bukkit.createBlockData((String) type.getKey()), (Integer) type.getValue());
                        }
                    }
                    p.add(layer, (Integer) m.get("layers"));
                } else {
                    Bukkit.getLogger().info("One-block palette layer!");
                    String data = "null";
                    for(Map.Entry<?, ?> e: map.get(0).entrySet()) {
                        data = (String) e.getKey();
                    }
                    p.add(Bukkit.createBlockData(data), (Integer) m.get("layers"));
                }
            } catch(ClassCastException e) {
                throw new InvalidConfigurationException("SEVERE configuration error for Palette: \n\n" + e.getMessage());
            }
        }
        return p;
    }

    @Override
    public String toString() {
        return "Palette with ID " + getID() + " with " + getPalette().getSize() + " layers, using Simplex: " + useNoise;
    }

    public static PaletteConfig fromID(String id) {
        return palettes.get(id);
    }
}
