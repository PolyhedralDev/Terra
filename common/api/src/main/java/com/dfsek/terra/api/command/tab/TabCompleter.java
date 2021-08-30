package com.dfsek.terra.api.command.tab;

import java.util.List;

import com.dfsek.terra.api.entity.CommandSender;


public interface TabCompleter {
    List<String> complete(CommandSender sender);
}
