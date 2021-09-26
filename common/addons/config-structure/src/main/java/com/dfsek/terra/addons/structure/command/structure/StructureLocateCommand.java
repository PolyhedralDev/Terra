package com.dfsek.terra.addons.structure.command.structure;

import java.util.Locale;

import com.dfsek.terra.addons.structure.command.AsyncStructureFinder;
import com.dfsek.terra.addons.structure.command.structure.argument.StructureArgumentParser;
import com.dfsek.terra.addons.structure.command.structure.completer.StructureCompleter;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Switch;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.command.annotation.inject.SwitchTarget;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.command.arg.IntegerArgumentParser;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.util.vector.Vector3;


@PlayerCommand
@WorldCommand
@Command(arguments = {
        @Argument(
                value = "structure",
                tabCompleter = StructureCompleter.class,
                argumentParser = StructureArgumentParser.class
        ),
        @Argument(
                value = "radius",
                required = false,
                defaultValue = "100",
                argumentParser = IntegerArgumentParser.class
        )
}, switches = @Switch(
        value = "teleport",
        aliases = { "t", "tp" }
))
public class StructureLocateCommand implements CommandTemplate {
    @Inject
    private Platform platform;
    
    @ArgumentTarget("structure")
    private ConfiguredStructure structure;
    
    @ArgumentTarget("radius")
    private Integer radius;
    
    @SwitchTarget("teleport")
    private boolean teleport;
    
    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        
        new Thread(new AsyncStructureFinder(player.world().getBiomeProvider(), structure,
                                            player.position().clone().multiply((1D / platform.getTerraConfig().getBiomeSearchResolution())),
                                            player.world(), 0, radius, location -> {
            if(location != null) {
                sender.sendMessage(
                        String.format("The nearest %s is at [%d, ~, %d] (%.1f blocks away)", structure.getID().toLowerCase(Locale.ROOT),
                                      location.getBlockX(), location.getBlockZ(),
                                      location.add(new Vector3(0, player.position().getY(), 0)).distance(player.position())));
                if(teleport) {
                    platform.runPossiblyUnsafeTask(
                            () -> player.position(new Vector3(location.getX(), player.position().getY(), location.getZ())));
                }
            } //else LangUtil.send("command.biome.unable-to-locate", sender);
        }, platform), "Biome Location Thread").start();
    }
}
