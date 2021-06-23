package com.dfsek.terra.commands.biome.tab;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.tab.TabCompleter;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.List;
import java.util.stream.Collectors;

public class BiomeTabCompleter implements TabCompleter {
    @Inject
    private TerraPlugin main;

    @Override
    public List<String> complete(CommandSender sender) {
        Player player = (Player) sender;
        return main.getWorld(player.world()).getConfig().getRegistry(TerraBiome.class).entries().stream().map(TerraBiome::getID).collect(Collectors.toList());
    }
}
