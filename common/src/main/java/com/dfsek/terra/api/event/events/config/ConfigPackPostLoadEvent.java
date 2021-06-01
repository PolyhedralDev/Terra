package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.config.fileloaders.Loader;
import com.dfsek.terra.config.pack.ConfigPack;

/**
 * Called when a config pack has finished loading.
 */
public class ConfigPackPostLoadEvent extends ConfigPackLoadEvent {
    public ConfigPackPostLoadEvent(ConfigPack pack, ExceptionalConsumer<ConfigTemplate> configTemplateLoader, Loader loader) {
        super(pack, configTemplateLoader, loader);
    }
}
