package com.dfsek.terra.config.factories;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.TerraPlugin;

public interface TerraFactory<C extends ConfigTemplate, O> {
    O build(C config, TerraPlugin main) throws LoadException;
}
