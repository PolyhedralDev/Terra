package com.dfsek.terra.config;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.commons.io.FilenameUtils;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.BlockPalette;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class PaletteConfig extends YamlConfiguration {
    private static final Map<String, PaletteConfig> palettes = new HashMap<>();
    private BlockPalette palette;
    private String paletteID;
    private boolean isEnabled = false;
    private String friendlyName;
    public PaletteConfig(File file) throws IOException, InvalidConfigurationException {
        load(file);
    }

    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
        palette = getPalette(getMapList("blocks"));
        if(!contains("id")) throw new InvalidConfigurationException("Grid ID unspecified!");
        this.paletteID = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Grid Name unspecified!");
        this.friendlyName = getString("name");
        isEnabled = true;
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

    public String getPaletteID() {
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

    protected static void loadPalettes(JavaPlugin main) {
        // TODO: Merge all load methods
        Logger logger = main.getLogger();
        palettes.clear();
        File paletteFolder = new File(main.getDataFolder() + File.separator + "palettes");
        paletteFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(paletteFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        try {
                            PaletteConfig palette = new PaletteConfig(path.toFile());
                            palettes.put(palette.getPaletteID(), palette);
                            logger.info("Loaded BlockPalette with name: " + palette.getFriendlyName() + ", ID " + palette.getPaletteID() + "with " + palette.getPalette().getSize() + " layers from " + path.toString());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            logger.severe("Configuration error for BlockPalette. ");
                            logger.severe(e.getMessage());
                            logger.severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
        main.getLogger().info("Loaded " + palettes.size() + " palettes.");
    }

    public static PaletteConfig fromID(String id) {
        return palettes.get(id);
    }
}
