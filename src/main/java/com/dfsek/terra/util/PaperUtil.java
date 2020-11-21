package com.dfsek.terra.util;

import com.dfsek.terra.config.lang.LangUtil;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

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
