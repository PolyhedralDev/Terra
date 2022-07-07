package com.dfsek.terra.bukkit.nms;

import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.bukkit.PlatformImpl;


public interface Initializer {
    String NMS = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    String TERRA_PACKAGE = Initializer.class.getPackageName();
    
    static void init(PlatformImpl platform) {
        Logger logger = LoggerFactory.getLogger(Initializer.class);
        try {
            Class<?> initializerClass = Class.forName(TERRA_PACKAGE + "." + NMS + ".NMSInitializer");
            try {
                Initializer initializer = (Initializer) initializerClass.getConstructor().newInstance();
                initializer.initialize(platform);
            } catch(ReflectiveOperationException e) {
                throw new RuntimeException("Error initializing NMS bindings. Report this to Terra.", e);
            }
        } catch(ClassNotFoundException e) {
            logger.error("NMS bindings for version {} do not exist. Support for this version is limited.", NMS);
            logger.error("This is usually due to running Terra on an unsupported Minecraft version.");
            logger.error("");
            logger.error("");
            for(int i = 0; i < 20; i++) {
                logger.error("PROCEEDING WITH AN EXISTING TERRA WORLD WILL RESULT IN CORRUPTION!!!");
            }
            logger.error("");
            logger.error("");
            logger.error("NMS bindings for version {} do not exist. Support for this version is limited.", NMS);
            logger.error("This is usually due to running Terra on an unsupported Minecraft version.");
        }
    }
    
    void initialize(PlatformImpl plugin);
}
