package com.dfsek.terra.forge;

import com.dfsek.terra.api.platform.modloader.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

public class ForgeMod implements Mod {
    private final ModInfo mod;

    public ForgeMod(ModInfo mod) {
        this.mod = mod;
    }

    @Override
    public String getID() {
        return mod.getModId();
    }

    @Override
    public String getVersion() {
        return mod.getVersion().getQualifier();
    }

    @Override
    public String getName() {
        return mod.getDisplayName();
    }
}
