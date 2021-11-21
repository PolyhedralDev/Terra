/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.lang;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.dfsek.terra.api.entity.CommandSender;


public interface Language {
    void log(String messageID, Level level, Logger logger, String... args);
    
    void send(String messageID, CommandSender sender, String... args);
    
    Message getMessage(String id);
}
