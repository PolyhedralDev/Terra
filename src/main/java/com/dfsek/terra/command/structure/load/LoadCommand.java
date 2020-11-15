package com.dfsek.terra.command.structure.load;

import com.dfsek.terra.Terra;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.DebugCommand;
import org.polydev.gaea.command.PlayerCommand;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoadCommand extends PlayerCommand implements DebugCommand {
    public LoadCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }

    @Override
    public String getName() {
        return "load";
    }

    public static List<String> getStructureNames() {
        List<String> names = new ArrayList<>();
        File structureDir = new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures");
        if(!structureDir.exists()) return Collections.emptyList();
        Path structurePath = structureDir.toPath();

        FilenameFilter filter = (dir, name) -> name.endsWith(".tstructure");
        for(File f : structureDir.listFiles(filter)) {
            String path = structurePath.relativize(f.toPath()).toString();
            names.add(path.substring(0, path.length() - 11));
        }
        return names;
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Arrays.asList(new LoadRawCommand(this), new LoadFullCommand(this, true), new LoadFullCommand(this, false));
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 0;
    }
}
