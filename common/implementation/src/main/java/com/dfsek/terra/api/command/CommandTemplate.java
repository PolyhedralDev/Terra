package com.dfsek.terra.api.command;

import com.dfsek.terra.api.entity.CommandSender;

public interface CommandTemplate {
    void execute(CommandSender sender);
}
