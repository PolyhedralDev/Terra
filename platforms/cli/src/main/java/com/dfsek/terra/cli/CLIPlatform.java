package com.dfsek.terra.cli;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.cli.handle.CLIItemHandle;
import com.dfsek.terra.cli.handle.CLIWorldHandle;


public class CLIPlatform extends AbstractPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLIPlatform.class);
    
    private final CLIWorldHandle worldHandle = new CLIWorldHandle();
    private final CLIItemHandle itemHandle = new CLIItemHandle();
    
    public CLIPlatform() {
        LOGGER.info("Root directory: {}", getDataFolder().getAbsoluteFile());
        load();
        LOGGER.info("Initialized Terra platform.");
    }
    
    @Override
    public boolean reload() {
        return false;
    }
    
    @Override
    public @NotNull String platformName() {
        return "CLI";
    }
    
    @Override
    public @NotNull WorldHandle getWorldHandle() {
        return worldHandle;
    }
    
    @Override
    public @NotNull File getDataFolder() {
        return new File("./");
    }
    
    @Override
    public @NotNull ItemHandle getItemHandle() {
        return itemHandle;
    }
    
    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry.registerLoader(PlatformBiome.class, (TypeLoader<PlatformBiome>) (annotatedType, o, configLoader, depthTracker) -> () -> o);
    }
}
