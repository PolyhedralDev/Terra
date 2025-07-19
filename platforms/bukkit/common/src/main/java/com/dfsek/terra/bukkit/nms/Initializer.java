package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.bukkit.TerraBukkitPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.util.VersionUtil;


public interface Initializer {
    String NMS = VersionUtil.getMinecraftVersionInfo().toString().replace(".", "_");
    String TERRA_PACKAGE = Initializer.class.getPackageName();

    static PlatformImpl init(TerraBukkitPlugin plugin) {
        Logger logger = LoggerFactory.getLogger(Initializer.class);

        PlatformImpl platform = constructPlatform(plugin);
        if (platform == null) {
            logger.error("NMS bindings for version {} do not exist. Support for this version is limited.", NMS);
            logger.error("This is usually due to running Terra on an unsupported Minecraft version.");
            String bypassKey = "IKnowThereAreNoNMSBindingsFor" + NMS + "ButIWillProceedAnyway";
            if(System.getProperty(bypassKey) == null) {
                logger.error("Because of this **TERRA HAS BEEN DISABLED**.");
                logger.error("Do not come ask us why it is not working.");
                logger.error("If you wish to proceed anyways, you can add the JVM System Property \"{}\" to enable the plugin.", bypassKey);
                return null;
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

        return platform;
    }

    private static PlatformImpl constructPlatform(TerraBukkitPlugin plugin) {
        try {
            String packageVersion = NMS;
            if (NMS.equals("v1_21_5") || NMS.equals("v1_21_6") || NMS.equals("v1_21_7")) {
                packageVersion = "v1_21_8";
            }

            Class<?> platformClass = Class.forName(TERRA_PACKAGE + "." + packageVersion + ".NMSPlatform");
            try {
                return (PlatformImpl) platformClass
                    .getConstructor(TerraBukkitPlugin.class)
                    .newInstance(plugin);
            } catch(ReflectiveOperationException e) {
                throw new RuntimeException("Error initializing NMS bindings. Report this to Terra.", e);
            }
        } catch(ClassNotFoundException e) {
            return null;
        }
    }
}
