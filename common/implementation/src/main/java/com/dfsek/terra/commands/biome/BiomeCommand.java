package com.dfsek.terra.commands.biome;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.config.lang.LangUtil;

@Command(
        subcommands = {
                @Subcommand(
                        value = "info",
                        aliases = {"i"},
                        clazz = BiomeInfoCommand.class
                ),
                @Subcommand(
                        value = "locate",
                        aliases = {"l"},
                        clazz = BiomeLocateCommand.class
                )
        },
        usage = "/terra biome"
)
@WorldCommand
@PlayerCommand
public class BiomeCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;

        BiomeProvider provider = main.getWorld(player.getWorld()).getBiomeProvider();
        UserDefinedBiome biome = (UserDefinedBiome) provider.getBiome(player.getLocation());
        LangUtil.send("command.biome.in", sender, biome.getID());
    }
}
