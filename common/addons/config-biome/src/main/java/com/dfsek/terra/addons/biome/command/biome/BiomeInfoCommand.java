/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.command.biome;

import com.dfsek.terra.addons.biome.BiomeTemplate;
import com.dfsek.terra.addons.biome.UserDefinedBiome;
import com.dfsek.terra.addons.biome.command.biome.arg.BiomeArgumentParser;
import com.dfsek.terra.addons.biome.command.biome.tab.BiomeTabCompleter;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.world.biome.TerraBiome;


@Command(arguments = @Argument(
        value = "biome",
        tabCompleter = BiomeTabCompleter.class,
        argumentParser = BiomeArgumentParser.class
))
public class BiomeInfoCommand implements CommandTemplate {
    @ArgumentTarget("biome")
    private TerraBiome biome;
    
    @Override
    public void execute(CommandSender sender) {
        sender.sendMessage("Biome info for \"" + biome.getID() + "\".");
        sender.sendMessage("Vanilla biome: " + biome.getVanillaBiomes());
        
        if(biome instanceof UserDefinedBiome) {
            BiomeTemplate bio = ((UserDefinedBiome) biome).getConfig();
            
            if(bio.getExtended().size() == 0) {
                sender.sendMessage("No Parent Biomes");
            } else {
                sender.sendMessage("------Parent Biomes-----");
                bio.getExtended().forEach(id -> sender.sendMessage(" - " + id));
            }
        }
    }
}
