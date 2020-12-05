package com.dfsek.terra.config.factories;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.LoadException;

public interface TerraFactory<C extends ConfigTemplate, O> {
    O build(C config) throws LoadException;
}
