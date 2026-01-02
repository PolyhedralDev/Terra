package com.dfsek.terra.bukkit.util;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Logs the detected runtime environment for supportability.
 */
public final class RuntimeEnvLogger {
    private RuntimeEnvLogger() {}

    public static void log(JavaPlugin plugin) {
        int feature = Runtime.version().feature();
        String vendor = System.getProperty("java.vendor", "unknown");
        String runtime = System.getProperty("java.runtime.name", "Java");
        String version = System.getProperty("java.version", "unknown");

        plugin.getLogger().info("Runtime: " + runtime + " " + version + " (" + vendor + "), feature=" + feature + ".");
        plugin.getLogger().info("Supported Java: 21-25. Recommended: 21 (LTS).");

        if(feature < 21 || feature > 25) {
            plugin.getLogger().warning("You are running Java " + feature + ". This fork targets Java 21-25. Unexpected issues may occur.");
        } else if(feature != 21) {
            plugin.getLogger().warning("Java " + feature + " detected. If you see odd reflection warnings, consider Java 21 (LTS).");
        }
    }
}
