package com.dfsek.terra.config.factories;

import com.dfsek.tectonic.config.ConfigTemplate;

public interface TerraFactory<C extends ConfigTemplate, O> {
    O build(C config);
}
