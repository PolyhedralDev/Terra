package com.dfsek.terra.config.factories;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.Terra;

public interface TerraFactory<C extends ConfigTemplate, O> {
    O build(C config, Terra main) throws LoadException;
}
