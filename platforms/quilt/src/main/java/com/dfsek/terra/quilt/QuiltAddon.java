package com.dfsek.terra.quilt;

import com.dfsek.terra.mod.MinecraftAddon;
import com.dfsek.terra.mod.ModPlatform;


public class QuiltAddon extends MinecraftAddon {

    public QuiltAddon(ModPlatform modPlatform) {
        super(modPlatform);
    }

    @Override
    public String getID() {
        return "terra-quilt";
    }
}