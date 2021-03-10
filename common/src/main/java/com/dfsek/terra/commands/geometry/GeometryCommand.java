package com.dfsek.terra.commands.geometry;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Subcommand;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.config.lang.LangUtil;


@DebugCommand
@PlayerCommand
@Command(
        subcommands = {
                @Subcommand(
                        value = "sphere",
                        clazz = SphereCommand.class,
                        aliases = {"s"}
                ),
                @Subcommand(
                        value = "deformedsphere",
                        clazz = DeformedSphereCommand.class,
                        aliases = {"df"}
                ),
                @Subcommand(
                        value = "tube",
                        clazz = TubeCommand.class,
                        aliases = {"t"}
                )
        },
        usage = "/terra geometry"
)
public class GeometryCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {
        LangUtil.send("command.geometry.main-menu", sender);
    }
}
