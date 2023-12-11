package com.dfsek.terra.api.config;

import ca.solostudios.strata.version.Version;

import com.dfsek.terra.api.properties.PropertyHolder;
import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.meta.CheckedRegistryHolder;
import com.dfsek.terra.api.registry.meta.RegistryProvider;
import com.dfsek.terra.api.tectonic.ConfigLoadingDelegate;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;

import java.util.Map;


public interface MetaPack extends LoaderRegistrar,
                                  ConfigLoadingDelegate,
                                  CheckedRegistryHolder,
                                  RegistryProvider,
                                  Keyed<MetaPack>,
                                  PropertyHolder {

    Map<String, ConfigPack> packs();

    String getAuthor();

    Version getVersion();
}
