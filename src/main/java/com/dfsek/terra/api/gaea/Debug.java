package com.dfsek.terra.api.gaea;

import org.bukkit.plugin.java.JavaPlugin;

public class Debug {
    public static JavaPlugin main;

    public static void setMain(JavaPlugin main) {
        Debug.main = main;
    }

    public static void info(String message) {
        if(Gaea.isDebug()) main.getLogger().info(message);
    }
    public static void warn(String message) {
        if(Gaea.isDebug()) main.getLogger().warning(message);
    }

    public static void error(String message) {
        if(Gaea.isDebug()) main.getLogger().severe(message);
    }

    public static void stack(Exception e) {
        if(Gaea.isDebug()) e.printStackTrace();
    }
}
