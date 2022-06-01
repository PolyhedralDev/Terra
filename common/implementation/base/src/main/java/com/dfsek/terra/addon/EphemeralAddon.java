package com.dfsek.terra.addon;

import ca.solostudios.strata.version.Version;

import com.dfsek.terra.api.addon.BaseAddon;


public class EphemeralAddon implements BaseAddon {
    private final Version version;
    private final String id;
    
    public EphemeralAddon(Version version, String id) {
        this.version = version;
        this.id = id;
    }
    
    @Override
    public Version getVersion() {
        return version;
    }
    
    @Override
    public String getID() {
        return id;
    }
}
