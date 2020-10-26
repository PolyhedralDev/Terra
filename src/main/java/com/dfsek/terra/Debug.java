package com.dfsek.terra;

import com.dfsek.terra.config.base.ConfigUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class Debug {
    public static JavaPlugin main;

    public static void setMain(JavaPlugin main) {
        Debug.main = main;
    }

    public static void info(String message) {
        if(ConfigUtil.debug) main.getLogger().info(message);
    }

    public static void warn(String message) {
        if(ConfigUtil.debug) main.getLogger().warning(message);
    }

    public static void error(String message) {
        if(ConfigUtil.debug) main.getLogger().severe(message);
    }

    public static void stack(Exception e) {
        if(ConfigUtil.debug) e.printStackTrace();
    }
}
