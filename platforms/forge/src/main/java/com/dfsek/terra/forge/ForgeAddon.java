package com.dfsek.terra.forge;

import com.dfsek.terra.mod.MinecraftAddon;
import com.dfsek.terra.mod.ModPlatform;


public class ForgeAddon extends MinecraftAddon {
    
    public ForgeAddon(ModPlatform modPlatform) {
        super(modPlatform);
    }
    
    @Override
    public String getID() {
        return "terra-forge";
    }
}