package com.dfsek.terra.api.command.tab;

import java.util.Collections;
import java.util.List;

import com.dfsek.terra.api.entity.CommandSender;


public class NothingCompleter implements TabCompleter {
    @Override
    public List<String> complete(CommandSender sender) {
        return Collections.emptyList();
    }
}
