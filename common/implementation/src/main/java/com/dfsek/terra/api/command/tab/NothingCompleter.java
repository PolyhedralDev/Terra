package com.dfsek.terra.api.command.tab;

import com.dfsek.terra.api.entity.CommandSender;

import java.util.Collections;
import java.util.List;

public class NothingCompleter implements TabCompleter {
    @Override
    public List<String> complete(CommandSender sender) {
        return Collections.emptyList();
    }
}
