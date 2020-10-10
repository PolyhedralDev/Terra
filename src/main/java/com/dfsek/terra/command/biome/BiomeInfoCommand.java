package com.dfsek.terra.command.biome;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.command.type.WorldCommand;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.genconfig.StructureConfig;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.generation.GenerationPhase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BiomeInfoCommand extends WorldCommand {
    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        String id = args[0];
        ConfigPack cfg = TerraWorld.getWorld(world).getConfig();
        UserDefinedBiome b;
        try {
            b = cfg.getBiome(id).getBiome();
        } catch(IllegalArgumentException | NullPointerException e) {
            LangUtil.send("command.biome.invalid", sender, id);
            return true;
        }
        sender.sendMessage("Biome info for \"" + b.getID() + "\".");
        sender.sendMessage("Vanilla biome: " + b.getVanillaBiome());
        sender.sendMessage("Erodible: " + b.isErodible());
        sender.sendMessage("-------Structures-------");

        List<StructureConfig> structureConfigs = cfg.getBiome(b).getStructures();

        for(StructureConfig c : structureConfigs) {
            sender.sendMessage(" - " + c.getID());
        }

        return true;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public List<com.dfsek.terra.command.type.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(!(sender instanceof  Player) || !(((Player) sender).getWorld().getGenerator() instanceof TerraChunkGenerator)) return Collections.emptyList();
        List<String> ids = TerraWorld.getWorld(((Player) sender).getWorld()).getConfig().getBiomeIDs();
        if(args.length == 1) return ids.stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
