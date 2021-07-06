package com.dfsek.terra.commands.biome;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.commands.biome.arg.BiomeArgumentParser;
import com.dfsek.terra.commands.biome.tab.BiomeTabCompleter;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.world.population.items.TerraStructure;

import java.util.List;

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

            List<TerraStructure> structureConfigs = bio.getStructures();

            if(structureConfigs.size() == 0) {
                sender.sendMessage("No Structures");
            } else {
                sender.sendMessage("-------Structures-------");
                for(TerraStructure c : structureConfigs) {
                    sender.sendMessage(" - " + c.getTemplate().getID());
                }
            }
        }
    }
}
