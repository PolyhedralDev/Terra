package com.dfsek.terra.fabric;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;

import java.io.File;
import java.util.Optional;

import com.dfsek.terra.AbstractTerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.fabric.handle.FabricItemHandle;
import com.dfsek.terra.fabric.handle.FabricWorldHandle;
import com.dfsek.terra.fabric.util.ProtoBiome;


public class TerraPluginImpl extends AbstractTerraPlugin {
    
    private final ItemHandle itemHandle = new FabricItemHandle();
    private final WorldHandle worldHandle = new FabricWorldHandle();
    private final Lazy<File> dataFolder = Lazy.lazy(() -> new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra"));
    
    public TerraPluginImpl() {
        load();
    }
    
    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        LangUtil.load(getTerraConfig().getLanguage(), this); // Load language.
        boolean succeed = getRawConfigRegistry().loadAll(this);
        return succeed;
    }
    
    @Override
    public String platformName() {
        return "Fabric";
    }
    
    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }
    
    @Override
    public File getDataFolder() {
        return dataFolder.value();
    }
    
    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }
    
    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry.registerLoader(com.dfsek.terra.api.world.biome.Biome.class, (t, o, l) -> parseBiome((String) o))
                .registerLoader(Identifier.class, (t, o, l) -> {
                    Identifier identifier = Identifier.tryParse((String) o);
                    if(identifier == null)
                        throw new LoadException("Invalid identifier: " + o);
                    return identifier;
                });
    }
    
    @Override
    protected Optional<TerraAddon> getPlatformAddon() {
        return Optional.of(new FabricAddon(this));
    }
    
    private ProtoBiome parseBiome(String id) throws LoadException {
        Identifier identifier = Identifier.tryParse(id);
        if(BuiltinRegistries.BIOME.get(identifier) == null) throw new LoadException("Invalid Biome ID: " + identifier); // failure.
        return new ProtoBiome(identifier);
    }
}
