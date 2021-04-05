package com.dfsek.terra.config.prototype;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.config.pack.ConfigPack;

public interface ConfigType<T extends ConfigTemplate> {
    T getTemplate(ConfigPack pack, TerraPlugin main);

    void callback(ConfigPack pack, TerraPlugin main, T loadedConfig) throws LoadException;
}
