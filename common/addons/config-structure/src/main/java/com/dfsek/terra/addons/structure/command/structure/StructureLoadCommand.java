package com.dfsek.terra.addons.structure.command.structure;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.dfsek.terra.addons.structure.command.structure.argument.ScriptArgumentParser;
import com.dfsek.terra.addons.structure.command.structure.completer.RotationCompleter;
import com.dfsek.terra.addons.structure.command.structure.completer.ScriptCompleter;
import com.dfsek.terra.api.Platform;
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
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.rotation.Rotation;


@PlayerCommand
@DebugCommand
@WorldCommand
@Command(arguments = {
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
}, switches = @Switch(value = "chunk",
                      aliases = "c"
), usage = "/terra structure load [ROTATION] [-c]")
public class StructureLoadCommand implements CommandTemplate {
    @ArgumentTarget("rotation")
    private final Integer rotation = 0;
    
    @SwitchTarget("chunk")
    private boolean chunk;
    
    @ArgumentTarget("structure")
    private Structure script;
    
    @Inject
    private Platform main;
    
    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        
        long t = System.nanoTime();
        Random random = new Random(ThreadLocalRandom.current().nextLong());
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
            script.generate(player.position(), player.world(), player.world().getChunkAt(player.position()), random, r);
        } else {
            script.generate(player.position(), player.world(), random, r);
        }
        long l = System.nanoTime() - t;
        
        sender.sendMessage("Took " + ((double) l) / 1000000 + "ms");
    }
}
