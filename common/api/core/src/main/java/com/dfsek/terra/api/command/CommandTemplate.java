/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.command;

import com.dfsek.terra.api.entity.CommandSender;


public interface CommandTemplate {
    void execute(CommandSender sender);
}
