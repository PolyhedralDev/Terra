package com.dfsek.terra.commands.structure.completer;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.tab.TabCompleter;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StructureCompleter implements TabCompleter {
    @Inject
    private TerraPlugin main;

    @Override
    public List<String> complete(CommandSender sender) {
        Player player = (Player) sender;
        return new ArrayList<>(main.getWorld(player.getWorld()).getConfig().getStructureRegistry().keys());
    }
}
