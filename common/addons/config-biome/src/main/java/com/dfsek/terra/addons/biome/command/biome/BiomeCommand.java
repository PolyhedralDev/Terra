/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.command.biome;

import com.dfsek.terra.addons.biome.UserDefinedBiome;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


@Command(
        subcommands = {
                @Subcommand(value = "info", aliases = "i", clazz = BiomeInfoCommand.class),
                @Subcommand(value = "locate", aliases = "l", clazz = BiomeLocateCommand.class)
        },
        usage = "/terra biome"
)
@WorldCommand
@PlayerCommand
public class BiomeCommand implements CommandTemplate {
    @Inject
    private Platform platform;
    
    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        
        BiomeProvider provider = player.world().getBiomeProvider();
        UserDefinedBiome biome = (UserDefinedBiome) provider.getBiome(player.position(), player.world().getSeed());
        sender.sendMessage("You are standing in " + biome.getID());
    }
}
