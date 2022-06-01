/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events.config.type;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.CheckedRegistry;


public class ConfigTypePostLoadEvent extends ConfigTypeLoadEvent {
    public ConfigTypePostLoadEvent(ConfigType<?, ?> type, CheckedRegistry<?> registry, ConfigPack pack) {
        super(type, registry, pack);
    }
}
