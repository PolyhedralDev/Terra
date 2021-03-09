package com.dfsek.terra.commands.profiler;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.platform.CommandSender;

@Command(
        subcommands = {
                @Subcommand(value = "query", aliases = {"q"}, clazz = ProfileQueryCommand.class),
                @Subcommand(value = "start", aliases = {"s"}, clazz = ProfileStartCommand.class),
                @Subcommand(value = "stop", aliases = {"st"}, clazz = ProfileStopCommand.class),
                @Subcommand(value = "reset", aliases = {"r"}, clazz = ProfileResetCommand.class)
        },
        usage = "Commands to enable/disable/query/reset the profiler."
)
@WorldCommand
@PlayerCommand
@DebugCommand
public class ProfileCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {

    }
}
