package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.command.tab.TabCompleter;
import com.dfsek.terra.api.platform.CommandSender;

import java.util.Arrays;
import java.util.List;

public class RotationCompleter implements TabCompleter {
    @Override
    public List<String> complete(CommandSender sender) {
        return Arrays.asList("0", "90", "180", "270");
    }
}
