package com.dfsek.terra.command.profile;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.command.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.WorldProfiler;

import java.util.List;

public class ProfileCommand extends WorldCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        WorldProfiler profile = TerraProfiler.fromWorld(w);
        if(args.length > 1 && "query".equals(args[1])) {
            sender.sendMessage(profile.getResultsFormatted());
            return true;
        } else if(args.length > 1 && "reset".equals(args[1])) {
            profile.reset();
            sender.sendMessage("Profiler has been reset.");
            return true;
        } else if(args.length > 1 && "start".equals(args[1])) {
            profile.setProfiling(true);
            sender.sendMessage("Profiler has started.");
            return true;
        } else if(args.length > 1 && "stop".equals(args[1])) {
            profile.setProfiling(false);
            sender.sendMessage("Profiler has stopped.");
            return true;
        }
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return null;
    }
}
