package com.dfsek.terra.addons.structure.command.structure.completer;

import java.util.List;
import java.util.stream.Collectors;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.tab.TabCompleter;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.structure.Structure;


public class ScriptCompleter implements TabCompleter {
    @Inject
    private Platform main;
    
    @Override
    public List<String> complete(CommandSender sender) {
        return ((Player) sender).world().getConfig().getRegistry(Structure.class).entries().stream().map(Structure::getID).collect(
                Collectors.toList());
    }
}
