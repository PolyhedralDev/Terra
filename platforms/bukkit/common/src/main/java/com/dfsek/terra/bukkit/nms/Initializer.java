package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.bukkit.BukkitAddon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.util.VersionUtil;


public interface Initializer {
    String NMS = VersionUtil.getMinecraftVersionInfo().toString().replace(".", "_");
    String TERRA_PACKAGE = Initializer.class.getPackageName();

    static boolean init(PlatformImpl platform) {
        Logger logger = LoggerFactory.getLogger(Initializer.class);

        Initializer initializer = constructInitializer();
        if(initializer != null) {
            initializer.initialize(platform);
        } else {
            logger.error("NMS bindings for version {} do not exist. Support for this version is limited.", NMS);
            logger.error("This is usually due to running Terra on an unsupported Minecraft version.");
            String bypassKey = "IKnowThereAreNoNMSBindingsFor" + NMS + "ButIWillProceedAnyway";
            if(System.getProperty(bypassKey) == null) {
                logger.error("Because of this **TERRA HAS BEEN DISABLED**.");
                logger.error("Do not come ask us why it is not working.");
                logger.error("If you wish to proceed anyways, you can add the JVM System Property \"{}\" to enable the plugin.", bypassKey);
                return false;
            } else {
                logger.error("");
                logger.error("");
                for(int i = 0; i < 20; i++) {
                    logger.error("PROCEEDING WITH AN EXISTING TERRA WORLD WILL RESULT IN CORRUPTION!!!");
                }
                logger.error("");
                logger.error("");
                logger.error("NMS bindings for version {} do not exist. Support for this version is limited.", NMS);
                logger.error("This is usually due to running Terra on an unsupported Minecraft version.");
                logger.error("We will not give you any support for issues that may arise.");
                logger.error("Since you enabled the \"{}\" flag, we won't disable Terra. But be warned.", bypassKey);
            }
        }

        return true;
    }

    static BukkitAddon nmsAddon(PlatformImpl platform) {
        Initializer initializer = constructInitializer();
        return initializer != null ? initializer.getNMSAddon(platform) : new BukkitAddon(platform);
    }

    private static Initializer constructInitializer() {
        try {
            String packageVersion = NMS;

            Class<?> initializerClass = Class.forName(TERRA_PACKAGE + "." + packageVersion + ".NMSInitializer");
            try {
                return (Initializer) initializerClass.getConstructor().newInstance();
            } catch(ReflectiveOperationException e) {
                throw new RuntimeException("Error initializing NMS bindings. Report this to Terra.", e);
            }
        } catch(ClassNotFoundException e) {
            return null;
        }
    }

    void initialize(PlatformImpl plugin);

    BukkitAddon getNMSAddon(PlatformImpl plugin);
}
