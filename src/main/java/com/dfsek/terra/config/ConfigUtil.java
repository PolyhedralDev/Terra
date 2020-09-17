package com.dfsek.terra.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConfigUtil {
    public static void loadConfig(JavaPlugin main) {
        Logger logger = main.getLogger();
        logger.info("Loading config values");

        OreConfig.loadOres(main);

        PaletteConfig.loadPalettes(main);

        CarverConfig.loadCaves(main);

        FaunaConfig.loadFauna(main);

        BiomeConfig.loadBiomes(main);

        BiomeGridConfig.loadBiomeGrids(main);

        WorldConfig.reloadAll();
    }

    public static <E extends Enum<E>> List<E> getElements(List<String> st, Class<E> clazz) {
        return st.stream().map((s) -> E.valueOf(clazz, s)).collect(Collectors.toList());
    }
}
