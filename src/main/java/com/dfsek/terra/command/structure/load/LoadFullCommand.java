package com.dfsek.terra.command.structure.load;

import com.dfsek.terra.api.gaea.command.DebugCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadFullCommand extends LoadCommand implements DebugCommand {
    private final boolean chunk;

    public LoadFullCommand(com.dfsek.terra.api.gaea.command.Command parent, boolean chunk) {
        super(parent);
        this.chunk = chunk;
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        /*
        try {
            Rotation r;
            try {
                r = Rotation.fromDegrees(Integer.parseInt(args[1]));
            } catch(NumberFormatException e) {
                LangUtil.send("command.structure.invalid-rotation", sender, args[1]);
                return true;
            }
            Structure struc = Structure.load(new File(getMain().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure"));
            if(chunk) struc.paste(sender.getLocation(), sender.getLocation().getChunk(), r, (TerraBukkitPlugin) getMain());
            else struc.paste(sender.getLocation(), r, (TerraBukkitPlugin) getMain());
            //sender.sendMessage(String.valueOf(struc.checkSpawns(sender.getLocation(), r)));
        } catch(IOException e) {
            e.printStackTrace();
            LangUtil.send("command.structure.invalid", sender, args[0]);
        }

         */
        return true;
    }

    @Override
    public String getName() {
        return chunk ? "chunk" : "full";
    }

    @Override
    public List<com.dfsek.terra.api.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        switch(args.length) {
            case 1:
                return getStructureNames().stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
            case 2:
                return Stream.of("0", "90", "180", "270").filter(string -> string.toUpperCase().startsWith(args[1].toUpperCase())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
