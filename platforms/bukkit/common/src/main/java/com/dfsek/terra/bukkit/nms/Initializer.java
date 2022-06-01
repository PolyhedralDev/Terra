package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.TerraBukkitPlugin;

import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;


public interface Initializer {
    String NMS = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    String TERRA_PACKAGE = Initializer.class.getPackageName();
    
    void initialize(PlatformImpl plugin);
    
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
            logger.warn("NMS bindings for version {} do not exist. Support for this version is limited.", NMS);
        }
    }
}
