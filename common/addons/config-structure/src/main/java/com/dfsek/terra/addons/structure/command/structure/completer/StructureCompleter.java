package com.dfsek.terra.addons.structure.command.structure.completer;

import java.util.ArrayList;
import java.util.List;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.tab.TabCompleter;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;


public class StructureCompleter implements TabCompleter {
    @Inject
    private Platform platform;
    
    @Override
    public List<String> complete(CommandSender sender) {
        Player player = (Player) sender;
        return new ArrayList<>(player.world().getConfig().getRegistry(ConfiguredStructure.class).keys());
    }
}
