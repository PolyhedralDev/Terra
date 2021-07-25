package com.dfsek.terra.fabric;

import com.dfsek.terra.AbstractTerraPlugin;
import com.dfsek.terra.api.Logger;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.lang.Language;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.fabric.handle.FabricItemHandle;
import com.dfsek.terra.fabric.handle.FabricWorldHandle;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;

import java.io.File;

public class TerraPluginImpl extends AbstractTerraPlugin {
    private final org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger();

    private final Logger logger = new Logger() {
        @Override
        public void info(String message) {
            log4jLogger.info(message);
        }

        @Override
        public void warning(String message) {
            log4jLogger.warn(message);
        }

        @Override
        public void severe(String message) {
            log4jLogger.error(message);
        }
    };

    private final ItemHandle itemHandle = new FabricItemHandle();
    private final WorldHandle worldHandle = new FabricWorldHandle();
    private final Lazy<File> dataFolder = Lazy.of(() -> new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra"));

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public File getDataFolder() {
        return dataFolder.value();
    }

    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        LangUtil.load(getTerraConfig().getLanguage(), this); // Load language.
        boolean succeed = getRawConfigRegistry().loadAll(this);
        return succeed;
    }

    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }

    @Override
    public String platformName() {
        return "Fabric";
    }
}
