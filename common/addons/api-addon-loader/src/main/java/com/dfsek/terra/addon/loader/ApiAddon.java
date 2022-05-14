package com.dfsek.terra.addon.loader;

import ca.solostudios.strata.version.Version;

import com.dfsek.terra.api.addon.BaseAddon;


public class ApiAddon implements BaseAddon {
    private final Version version;
    private final String id;
    
    public ApiAddon(Version version, String id) {
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
