package com.dfsek.terra.api.config;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.TerraPlugin;

public interface ConfigFactory<C extends ConfigTemplate, O> {
    O build(C config, TerraPlugin main) throws LoadException;
}
