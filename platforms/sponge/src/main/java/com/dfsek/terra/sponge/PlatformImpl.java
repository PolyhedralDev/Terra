package com.dfsek.terra.sponge;

import org.spongepowered.api.Sponge;

import java.io.File;


import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.sponge.handle.SpongeWorldHandle;


public class PlatformImpl extends AbstractPlatform {
    private final TerraSpongePlugin plugin;
    private final SpongeWorldHandle worldHandle = new SpongeWorldHandle();
    
    public PlatformImpl(TerraSpongePlugin plugin) {
        this.plugin = plugin;
        load();
    }
    
    @Override
    public boolean reload() {
        return false;
    }
    
    @Override
    public String platformName() {
        return "Sponge";
    }
    
    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }
    
    @Override
    public File getDataFolder() {
        return Sponge.configManager().pluginConfig(plugin.getPluginContainer()).directory().toFile();
    }
    
    @Override
    public ItemHandle getItemHandle() {
        return null;
    }
}
