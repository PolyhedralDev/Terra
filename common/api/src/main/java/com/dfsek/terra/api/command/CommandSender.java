/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.command;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.util.generic.data.types.Maybe;


public interface CommandSender extends Handle {
    void sendMessage(String message);

    Maybe<Entity> entity();

    Maybe<Player> player();
}
