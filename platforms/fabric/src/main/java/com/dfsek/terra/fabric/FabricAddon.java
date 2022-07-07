package com.dfsek.terra.fabric;

import com.dfsek.terra.mod.MinecraftAddon;
import com.dfsek.terra.mod.ModPlatform;


public class FabricAddon extends MinecraftAddon {
    
    public FabricAddon(ModPlatform modPlatform) {
        super(modPlatform);
    }
    
    @Override
    public String getID() {
        return "terra-fabric";
    }
}