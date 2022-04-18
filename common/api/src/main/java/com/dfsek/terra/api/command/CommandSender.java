/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.command;

import java.util.Optional;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.Player;


public interface CommandSender extends Handle {
    void sendMessage(String message);
    
    Optional<Entity> getEntity();
    
    Optional<Player> getPlayer();
}
