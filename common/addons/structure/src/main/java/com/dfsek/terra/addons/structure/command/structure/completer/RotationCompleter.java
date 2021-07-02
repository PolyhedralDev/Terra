package com.dfsek.terra.addons.structure.command.structure.completer;

import com.dfsek.terra.api.command.tab.TabCompleter;
import com.dfsek.terra.api.entity.CommandSender;

import java.util.Arrays;
import java.util.List;

public class RotationCompleter implements TabCompleter {
    @Override
    public List<String> complete(CommandSender sender) {
        return Arrays.asList("0", "90", "180", "270");
    }
}
