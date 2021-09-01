package com.dfsek.terra.bukkit.util;

import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.lib.PaperLib.suggestPaper;


public final class PaperUtil {
    public static void checkPaper(JavaPlugin main) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            if(!PaperLib.isPaper()) {
                suggestPaper(main);
            }
        }, 100L);
    }
}
