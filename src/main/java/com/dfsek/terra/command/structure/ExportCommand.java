package com.dfsek.terra.command.structure;

import com.dfsek.terra.Terra;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.structure.InitializationException;
import com.dfsek.terra.structure.Structure;
import com.dfsek.terra.util.structure.WorldEditUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.PlayerCommand;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ExportCommand extends PlayerCommand {
    public ExportCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Location[] l = WorldEditUtil.getSelectionLocations(sender);
        if(l == null) return true;
        Location l1 = l[0];
        Location l2 = l[1];
        Structure structure;
        try {
            structure = new Structure(l1, l2, args[0]);
        } catch(InitializationException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }
        try {
            File file = new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure");
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            structure.save(file);
            LangUtil.send("command.structure.export", sender, file.getAbsolutePath());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getName() {
        return "export";
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
        return Collections.emptyList();
    }
}
