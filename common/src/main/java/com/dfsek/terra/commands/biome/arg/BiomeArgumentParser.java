package com.dfsek.terra.commands.biome.arg;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.arg.ArgumentParser;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.world.biome.TerraBiome;

public class BiomeArgumentParser implements ArgumentParser<TerraBiome> {
    @Inject
    private TerraPlugin main;

    @Override
    public TerraBiome parse(CommandSender sender, String arg) {
        Player player = (Player) sender;
        return main.getWorld(player.getWorld()).getConfig().getRegistry(TerraBiome.class).get(arg);
    }
}
