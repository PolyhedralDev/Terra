package com.dfsek.terra.command.structure;

import com.dfsek.terra.Terra;
import com.dfsek.terra.util.structure.WorldEditUtil;
import com.dfsek.terra.command.type.PlayerCommand;
import com.dfsek.terra.structure.GaeaStructure;
import com.dfsek.terra.structure.InitializationException;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ExportCommand extends PlayerCommand {
    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Location[] l = WorldEditUtil.getSelectionLocations(sender);
        if(l == null) return true;
        Location l1 = l[0];
        Location l2 = l[1];
        GaeaStructure structure;
        try {
            structure = new GaeaStructure(l1, l2, args[0]);
        } catch(InitializationException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }
        try {
            File file = new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure");
            file.getParentFile().mkdirs();
            file.createNewFile();
            structure.save(file);
            sender.sendMessage("Saved structure with ID " + structure.getId() + ", UUID: " + structure.getUuid().toString() + " to " + file.getPath());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return "export";
    }

    @Override
    public List<com.dfsek.terra.command.type.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }
}
