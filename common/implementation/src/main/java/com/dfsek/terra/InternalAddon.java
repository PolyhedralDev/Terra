package com.dfsek.terra;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;

import com.dfsek.terra.api.addon.BaseAddon;


public class InternalAddon implements BaseAddon {
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    @Override
    public String getID() {
        return "terra";
    }
    
    @Override
    public Version getVersion() {
        return VERSION;
    }
}
