/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events.config.pack;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;

import com.dfsek.terra.api.config.ConfigPack;


/**
 * Called when a config pack has finished loading.
 */
public class ConfigPackPostLoadEvent extends ConfigPackLoadEvent {
    public ConfigPackPostLoadEvent(ConfigPack pack, ExceptionalConsumer<ConfigTemplate> loader) {
        super(pack, loader);
    }
}
