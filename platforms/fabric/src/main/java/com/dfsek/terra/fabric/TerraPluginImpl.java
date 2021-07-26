package com.dfsek.terra.fabric;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.AbstractTerraPlugin;
import com.dfsek.terra.api.Logger;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.fabric.handle.FabricItemHandle;
import com.dfsek.terra.fabric.handle.FabricWorldHandle;
import com.dfsek.terra.fabric.util.ProtoBiome;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.Optional;

public class TerraPluginImpl extends AbstractTerraPlugin {

    private final ItemHandle itemHandle = new FabricItemHandle();
    private final WorldHandle worldHandle = new FabricWorldHandle();
    private final Lazy<File> dataFolder = Lazy.lazy(() -> new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra"));

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    protected Optional<TerraAddon> getPlatformAddon() {
        return Optional.of(new FabricAddon(this));
    }

    @Override
    protected Logger createLogger() {
        final org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger();
        return new Logger() {
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

    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry
                .registerLoader(com.dfsek.terra.api.world.biome.Biome.class, (t, o, l) -> parseBiome((String) o))
                .registerLoader(Identifier.class, (t, o, l) -> {
                    Identifier identifier = Identifier.tryParse((String) o);
                    if(identifier == null) throw new LoadException("Invalid identifier: " + o);
                    return identifier;
                });
    }

    private ProtoBiome parseBiome(String id) throws LoadException {
        Identifier identifier = Identifier.tryParse(id);
        if(BuiltinRegistries.BIOME.get(identifier) == null) throw new LoadException("Invalid Biome ID: " + identifier); // failure.
        return new ProtoBiome(identifier);
    }
}
