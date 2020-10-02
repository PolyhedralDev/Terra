package com.dfsek.terra.command.structure;

import com.dfsek.terra.Terra;
import com.dfsek.terra.command.type.PlayerCommand;
import com.dfsek.terra.structure.GaeaStructure;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LoadCommand extends PlayerCommand {
    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            GaeaStructure.Rotation r = GaeaStructure.Rotation.fromDegrees(Integer.parseInt(args[1]));
            GaeaStructure struc = GaeaStructure.load(new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure"));
            if("true".equals(args[2])) struc.paste(sender.getLocation(), r, Collections.emptyList());
            else struc.paste(sender.getLocation(), sender.getLocation().getChunk(), r, Collections.emptyList());
        } catch(IOException e) {
            e.printStackTrace();
            sender.sendMessage("Structure not found.");
        }
        return true;
    }

    @Override
    public String getName() {
        return "load";
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public List<com.dfsek.terra.command.type.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 3;
    }
}
