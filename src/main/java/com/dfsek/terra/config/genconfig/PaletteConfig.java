package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.ConfigLoader;
import com.dfsek.terra.config.TerraConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.BlockPalette;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaletteConfig extends TerraConfigObject {
    private static final Map<String, PaletteConfig> palettes = new HashMap<>();
    private BlockPalette palette;
    private String paletteID;
    private boolean isEnabled = false;
    private String friendlyName;
    public PaletteConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    @Override
    public void init() throws InvalidConfigurationException {
        palette = getPalette(getMapList("blocks"));
        if(!contains("id")) throw new InvalidConfigurationException("Grid ID unspecified!");
        this.paletteID = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Grid Name unspecified!");
        this.friendlyName = getString("name");
        isEnabled = true;
        palettes.put(paletteID, this);
    }

    public BlockPalette getPalette() {
        return palette;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getID() {
        return paletteID;
    }

    protected static BlockPalette getPalette(List<Map<?, ?>> maps) throws InvalidConfigurationException {
        BlockPalette p  = new BlockPalette();
        for(Map<?, ?> m : maps) {
            try {
                ProbabilityCollection<BlockData> layer = new ProbabilityCollection<>();
                for(Map.Entry<String, Integer> type : ((Map<String, Integer>) m.get("materials")).entrySet()) {
                    layer.add(Bukkit.createBlockData(type.getKey()), type.getValue());
                }
                p.addBlockData(layer, (Integer) m.get("layers"));
            } catch(ClassCastException e) {
                throw new InvalidConfigurationException("SEVERE configuration error for BlockPalette: \n\n" + e.getMessage());
            }
        }
        return p;
    }

    @Override
    public String toString() {
        return "BlockPalette with name: " + getFriendlyName() + ", ID " + getID() + " with " + getPalette().getSize() + " layers";
    }

    public static PaletteConfig fromID(String id) {
        return palettes.get(id);
    }
}
