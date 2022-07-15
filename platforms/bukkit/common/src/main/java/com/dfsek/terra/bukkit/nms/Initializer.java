package com.dfsek.terra.bukkit.nms;

import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;


public interface Initializer {
    String NMS = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    String TERRA_PACKAGE = Initializer.class.getPackageName();
    
    static PlatformImpl init(TerraBukkitPlugin plugin) {
        Logger logger = LoggerFactory.getLogger(Initializer.class);
        try {
            Class<?> initializerClass = Class.forName(TERRA_PACKAGE + "." + NMS + ".NMSPlatform");
            try {
                return (PlatformImpl) initializerClass.getConstructor().newInstance(plugin);
            } catch(ReflectiveOperationException e) {
                throw new RuntimeException("Error initializing NMS bindings. Report this to Terra.", e);
            }
        } catch(ClassNotFoundException e) {
            Runnable runnable = () -> { // scary big block of text
                logger.error("NMS bindings for version {} do not exist.", NMS);
                logger.error("This is usually due to running Terra on an unsupported Minecraft version.");
                logger.error("Terra will now be DISABLED.");
                logger.error("");
                logger.error("");
                for(int i = 0; i < 20; i++) {
                    logger.error("PROCEEDING WITH AN EXISTING TERRA WORLD WILL RESULT IN CORRUPTION!!!");
                }
                logger.error("");
                logger.error("");
                logger.error("NMS bindings for version {} do not exist.", NMS);
                logger.error("This is usually due to running Terra on an unsupported Minecraft version.");
                logger.error("Terra will now be DISABLED.");
            };
            runnable.run();
            Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, runnable, 200L);
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        return null;
    }
}
