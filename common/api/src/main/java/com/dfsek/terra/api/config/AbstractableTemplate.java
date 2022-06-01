/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;

import com.dfsek.terra.api.registry.key.StringIdentifiable;


/**
 * An abstractable config template
 */
public interface AbstractableTemplate extends ConfigTemplate, StringIdentifiable {
}
