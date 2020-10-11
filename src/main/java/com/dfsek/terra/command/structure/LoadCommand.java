package com.dfsek.terra.command.structure;

import com.dfsek.terra.Terra;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.structure.Structure;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.DebugCommand;
import org.polydev.gaea.command.PlayerCommand;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LoadCommand extends PlayerCommand implements DebugCommand {
    public LoadCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            Structure.Rotation r;
            try {
                r = Structure.Rotation.fromDegrees(Integer.parseInt(args[1]));
            } catch(NumberFormatException e) {
                LangUtil.send("command.structure.invalid-rotation", sender, args[1]);
                return true;
            }
            Structure struc = Structure.load(new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure"));
            if("true".equals(args[2])) struc.paste(sender.getLocation(), r);
            else struc.paste(sender.getLocation(), sender.getLocation().getChunk(), r);
            //sender.sendMessage(String.valueOf(struc.checkSpawns(sender.getLocation(), r)));
        } catch(IOException e) {
            e.printStackTrace();
            LangUtil.send("command.structure.invalid", sender, args[0]);
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
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 3;
    }
}
