package com.dfsek.terra.api.command.tab;

import com.dfsek.terra.api.platform.CommandSender;

import java.util.List;

public interface TabCompleter {
    List<String> complete(CommandSender sender);
}
