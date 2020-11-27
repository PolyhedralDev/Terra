package com.dfsek.terra.command.biome;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.structure.TerraStructure;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.WorldCommand;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BiomeInfoCommand extends WorldCommand {
    public BiomeInfoCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        String id = args[0];
        ConfigPack cfg = TerraWorld.getWorld(world).getConfig();
        UserDefinedBiome b;
        try {
            b = cfg.getBiome(id);
        } catch(IllegalArgumentException | NullPointerException e) {
            LangUtil.send("command.biome.invalid", sender, id);
            return true;
        }
        sender.sendMessage("Biome info for \"" + b.getID() + "\".");
        sender.sendMessage("Vanilla biome: " + b.getVanillaBiome());
        sender.sendMessage("Erodible: " + b.isErodible());


        BiomeTemplate bio = b.getConfig();
        List<TerraStructure> structureConfigs = bio.getStructures();

        if(structureConfigs.size() == 0) sender.sendMessage("No Structures");
        else {
            sender.sendMessage("-------Structures-------");
            for(TerraStructure c : structureConfigs) {
                sender.sendMessage(" - " + c.getID());
            }
        }

        // Get snow info
        /*
        BiomeSnowConfig snowConfig = bio.getSnow();
        StringBuilder snowMessage = new StringBuilder("----------Snow----------\n");
        int comp = snowConfig.getSnowChance(0);
        int compHeight = 0;
        boolean changed = false;
        // Rebuild original snow data (rather than simply getting it, since it may have changed during initial assembly, if any overlaps occurred)
        for(int i = 0; i <= 255; i++) {
            int snow = snowConfig.getSnowChance(i);
            if(snow != comp) {
                changed = true;
                snowMessage.append("Y=")
                        .append(compHeight)
                        .append("-")
                        .append(i)
                        .append(": ")
                        .append(comp)
                        .append("% snow\n");
                comp = snow;
                compHeight = i;
            }
        }
        if(!changed) {
            snowMessage.append("Y=0")
                    .append("-255")
                    .append(": ")
                    .append(comp)
                    .append("% snow\n");
        }
        sender.sendMessage(snowMessage.toString());

        sender.sendMessage("Do snow: " + snowConfig.doSnow());

         */
        // TODO: implementation
        return true;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(!(sender instanceof Player) || !(((Player) sender).getWorld().getGenerator() instanceof TerraChunkGenerator))
            return Collections.emptyList();
        List<String> ids = TerraWorld.getWorld(((Player) sender).getWorld()).getConfig().getBiomeIDs();
        if(args.length == 1)
            return ids.stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
