package com.dfsek.terra.addons.biome.command.biome.arg;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.arg.ArgumentParser;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.world.biome.TerraBiome;

public class BiomeArgumentParser implements ArgumentParser<TerraBiome> {
    @Inject
    private TerraPlugin main;

    @Override
    public TerraBiome parse(CommandSender sender, String arg) {
        Player player = (Player) sender;
        return main.getWorld(player.world()).getConfig().getRegistry(TerraBiome.class).get(arg);
    }
}
