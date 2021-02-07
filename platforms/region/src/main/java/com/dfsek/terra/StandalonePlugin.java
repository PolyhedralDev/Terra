package com.dfsek.terra;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.debug.DebugLogger;
import com.dfsek.terra.platform.RawBiome;
import com.dfsek.terra.platform.RawWorldHandle;
import com.dfsek.terra.registry.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class StandalonePlugin implements TerraPlugin {
    private final ConfigRegistry registry = new ConfigRegistry();
    private final PluginConfig config = new PluginConfig();
    private final RawWorldHandle worldHandle = new RawWorldHandle();

    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public TerraWorld getWorld(World world) {
        return new TerraWorld(world, registry.get("DEFAULT"), this);
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger("Terra");
    }

    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return new File(".");
    }

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public Language getLanguage() {
        try {
            return new Language(new File(getDataFolder(), "lang/en_us.yml"));
        } catch(IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ConfigRegistry getRegistry() {
        return registry;
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemHandle getItemHandle() {
        return null;
    }

    @Override
    public void saveDefaultConfig() {

    }

    @Override
    public String platformName() {
        return "Standalone";
    }

    @Override
    public DebugLogger getDebugLogger() {
        return new DebugLogger(Logger.getLogger("Terra"));
    }

    @Override
    public void register(TypeRegistry registry) {
        registry
                .registerLoader(BlockData.class, (t, o, l) -> worldHandle.createBlockData((String) o))
                .registerLoader(Biome.class, (t, o, l) -> new RawBiome(o.toString()))
                .registerLoader(MaterialData.class, (t, o, l) -> worldHandle.createMaterialData((String) o));
        new GenericLoaders(this).register(registry);
    }

    public void load() {
        LangUtil.load("en_us", this);
        registry.loadAll(this);
        config.load(this);
    }
}
