package com.dfsek.terra.fabric;

import com.dfsek.terra.api.platform.modloader.Mod;
import net.fabricmc.loader.api.ModContainer;


public class FabricMod implements Mod {
    private final ModContainer mod;

    public FabricMod(ModContainer mod) {
        this.mod = mod;
    }

    @Override
    public String getID() {
        return mod.getMetadata().getId();
    }

    @Override
    public String getVersion() {
        return mod.getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public String getName() {
        return mod.getMetadata().getName();
    }
}
