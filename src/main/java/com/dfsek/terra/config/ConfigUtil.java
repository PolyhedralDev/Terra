package com.dfsek.terra.config;

import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.config.genconfig.AbstractBiomeConfig;
import com.dfsek.terra.config.genconfig.BiomeConfig;
import com.dfsek.terra.config.genconfig.BiomeGridConfig;
import com.dfsek.terra.config.genconfig.CarverConfig;
import com.dfsek.terra.config.genconfig.FloraConfig;
import com.dfsek.terra.config.genconfig.OreConfig;
import com.dfsek.terra.config.genconfig.PaletteConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConfigUtil {
    public static boolean debug;
    public static void loadConfig(JavaPlugin main) {
        main.saveDefaultConfig();
        FileConfiguration config = main.getConfig();

        debug = config.getBoolean("debug", false);

        Logger logger = main.getLogger();
        logger.info("Loading config values");

        new ConfigLoader("ores").load(main, OreConfig.class);

        new ConfigLoader("palettes").load(main, PaletteConfig.class);

        new ConfigLoader("carving").load(main, CarverConfig.class);

        new ConfigLoader("flora").load(main, FloraConfig.class);

        new ConfigLoader("abstract" + File.separator + "biomes").load(main, AbstractBiomeConfig.class);

        TerraBiomeGrid.invalidate();
        BiomeZone.invalidate(); // Invalidate BiomeZone and BiomeGrid caches to prevent old instances from being accessed.
        new ConfigLoader("biomes").load(main, BiomeConfig.class);

        new ConfigLoader("grids").load(main, BiomeGridConfig.class);

        WorldConfig.reloadAll();
    }

    public static <E extends Enum<E>> List<E> getElements(List<String> st, Class<E> clazz) {
        return st.stream().map((s) -> E.valueOf(clazz, s)).collect(Collectors.toList());
    }
}
