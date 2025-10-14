package com.dfsek.terra.bukkit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.bukkit.util.VersionUtil;

import java.util.List;


public interface NMSInitializer {
    List<String> SUPPORTED_VERSIONS = List.of("v1.21.9", "v1.21.10");
    String MINECRAFT_VERSION = VersionUtil.getMinecraftVersionInfo().toString();
    String TERRA_PACKAGE = NMSInitializer.class.getPackageName();

    static PlatformImpl init(TerraBukkitPlugin plugin) {
        Logger logger = LoggerFactory.getLogger(NMSInitializer.class);

        if (!SUPPORTED_VERSIONS.contains(MINECRAFT_VERSION)) {
            logger.error("You are running your server on Minecraft version {} which is not supported by this version of Terra.", MINECRAFT_VERSION);

            String bypassKey = "IKnowThereAreNoNMSBindingsFor" + MINECRAFT_VERSION.replace(".", "_") + "ButIWillProceedAnyway";
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
                logger.error("We will not give you any support for issues that may arise.");
                logger.error("Since you enabled the \"{}\" flag, we won't disable Terra. But be warned.", bypassKey);
            }
        }

        return constructPlatform(plugin);
    }

    private static PlatformImpl constructPlatform(TerraBukkitPlugin plugin) {
        try {
            Class<?> platformClass = Class.forName(TERRA_PACKAGE + ".nms.NMSPlatform");
            return (PlatformImpl) platformClass
                .getConstructor(TerraBukkitPlugin.class)
                .newInstance(plugin);
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException("Error initializing NMS bindings. Report this to Terra.", e);
        }
    }
}
