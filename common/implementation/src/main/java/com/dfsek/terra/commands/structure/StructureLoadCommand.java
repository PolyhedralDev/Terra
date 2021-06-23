package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Switch;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.command.annotation.inject.SwitchTarget;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.command.arg.IntegerArgumentParser;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.commands.structure.argument.ScriptArgumentParser;
import com.dfsek.terra.commands.structure.completer.RotationCompleter;
import com.dfsek.terra.commands.structure.completer.ScriptCompleter;

import java.util.concurrent.ThreadLocalRandom;

@PlayerCommand
@DebugCommand
@WorldCommand
@Command(
        arguments = {
                @Argument(
                        value = "structure",
                        tabCompleter = ScriptCompleter.class,
                        argumentParser = ScriptArgumentParser.class
                ),
                @Argument(
                        value = "rotation",
                        required = false,
                        tabCompleter = RotationCompleter.class,
                        argumentParser = IntegerArgumentParser.class,
                        defaultValue = "0"
                )
        },
        switches = {
                @Switch(value = "chunk",
                        aliases = "c"
                )
        },
        usage = "/terra structure load [ROTATION] [-c]"
)
public class StructureLoadCommand implements CommandTemplate {
    @ArgumentTarget("rotation")
    private Integer rotation = 0;

    @SwitchTarget("chunk")
    private boolean chunk;

    @ArgumentTarget("structure")
    private StructureScript script;

    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;

        long t = System.nanoTime();
        FastRandom random = new FastRandom(ThreadLocalRandom.current().nextLong());
        Rotation r;
        try {
            r = Rotation.fromDegrees(rotation);
        } catch(Exception e) {
            sender.sendMessage("Invalid rotation: " + rotation);
            return;
        }
        if(script == null) {
            sender.sendMessage("Invalid structure.");
            return;
        }
        if(this.chunk) {
            script.execute(player.getLocation(), player.getWorld().getChunkAt(player.getLocation()), random, r);
        } else {
            script.execute(player.getLocation(), random, r);
        }
        long l = System.nanoTime() - t;

        sender.sendMessage("Took " + ((double) l) / 1000000 + "ms");
    }
}
