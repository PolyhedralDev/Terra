package com.dfsek.terra.addons.manifest.impl;

import com.dfsek.terra.addons.manifest.impl.config.AddonManifest;
import com.dfsek.terra.api.addon.BaseAddon;


public class ManifestAddon implements BaseAddon {
    private final AddonManifest manifest;
    
    public ManifestAddon(AddonManifest manifest) {
        this.manifest = manifest;
    }
    
    @Override
    public String getID() {
        return manifest.getID();
    }
    
    public void initialize() {
    
    }
}
