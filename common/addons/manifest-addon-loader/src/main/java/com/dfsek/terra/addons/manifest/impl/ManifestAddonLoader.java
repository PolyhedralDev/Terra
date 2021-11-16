package com.dfsek.terra.addons.manifest.impl;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import com.dfsek.tectonic.loading.ConfigLoader;

import java.nio.file.Path;
import java.util.Collections;

import com.dfsek.terra.addons.manifest.impl.config.WebsiteConfig;
import com.dfsek.terra.addons.manifest.impl.config.loaders.VersionLoader;
import com.dfsek.terra.addons.manifest.impl.config.loaders.VersionRangeLoader;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.addon.bootstrap.BootstrapBaseAddon;


public class ManifestAddonLoader implements BootstrapBaseAddon {
    @Override
    public Iterable<BaseAddon> loadAddons(Path addonsFolder, ClassLoader parent) {
        ConfigLoader manifestLoader = new ConfigLoader();
        manifestLoader.registerLoader(Version.class, new VersionLoader())
                      .registerLoader(VersionRange.class, new VersionRangeLoader())
                .registerLoader(WebsiteConfig.class, WebsiteConfig::new);
        return Collections.emptySet();
    }
    
    @Override
    public String getID() {
        return "MANIFEST";
    }
}
