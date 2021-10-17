package com.dfsek.terra.fabric;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.TypeRegistry;

import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.util.FabricUtil;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.util.Logger;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.fabric.handle.FabricItemHandle;
import com.dfsek.terra.fabric.handle.FabricWorldHandle;
import com.dfsek.terra.fabric.util.ProtoBiome;


public class PlatformImpl extends AbstractPlatform {
    
    private final ItemHandle itemHandle = new FabricItemHandle();
    private final WorldHandle worldHandle = new FabricWorldHandle();
    private final Lazy<File> dataFolder = Lazy.lazy(() -> new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra"));
    
    private final Set<ServerWorld> worlds = new HashSet<>();
    
    public void addWorld(ServerWorld world) {
        worlds.add(world);
    }
    
    public PlatformImpl() {
        load();
    }
    
    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        LangUtil.load(getTerraConfig().getLanguage(), this); // Load language.
        boolean succeed = getRawConfigRegistry().loadAll(this);
    
        worlds.forEach(world -> {
            FabricChunkGeneratorWrapper chunkGeneratorWrapper = ((FabricChunkGeneratorWrapper) world.getChunkManager().getChunkGenerator());
            chunkGeneratorWrapper.setPack(getConfigRegistry().get(chunkGeneratorWrapper.getPack().getID()));
        });
        
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
        registry
                .registerLoader(com.dfsek.terra.api.world.biome.Biome.class, (t, o, l) -> parseBiome((String) o))
                .registerLoader(Identifier.class, (t, o, l) -> {
                    Identifier identifier = Identifier.tryParse((String) o);
                    if(identifier == null) throw new LoadException("Invalid identifier: " + o);
                    return identifier;
                });
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
    protected Optional<TerraAddon> getPlatformAddon() {
        return Optional.of(new FabricAddon(this));
    }
    
    private ProtoBiome parseBiome(String id) throws LoadException {
        Identifier identifier = Identifier.tryParse(id);
        if(BuiltinRegistries.BIOME.get(identifier) == null) throw new LoadException("Invalid Biome ID: " + identifier); // failure.
        return new ProtoBiome(identifier);
    }
}
