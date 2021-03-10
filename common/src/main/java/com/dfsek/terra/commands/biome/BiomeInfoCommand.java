package com.dfsek.terra.commands.biome;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.commands.biome.arg.BiomeArgumentParser;
import com.dfsek.terra.commands.biome.tab.BiomeTabCompleter;

@Command(
        arguments = {
                @Argument(
                        value = "biome",
                        tabCompleter = BiomeTabCompleter.class,
                        argumentParser = BiomeArgumentParser.class
                )
        }
)
public class BiomeInfoCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {

    }
}
