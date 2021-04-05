package com.dfsek.terra.config.prototype;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.config.pack.ConfigPack;

@FunctionalInterface
public interface ConfigType<T extends ConfigTemplate> {
    T getTemplate(ConfigPack pack, TerraPlugin main);

}
