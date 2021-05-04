package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.Switch;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.command.annotation.inject.SwitchTarget;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.command.arg.IntegerArgumentParser;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.world.locate.AsyncStructureFinder;
import com.dfsek.terra.commands.structure.argument.StructureArgumentParser;
import com.dfsek.terra.commands.structure.completer.StructureCompleter;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.world.population.items.TerraStructure;

import java.util.Locale;

@PlayerCommand
@WorldCommand
@Command(
        arguments = {
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
        },
        switches = {
                @Switch(
                        value = "teleport",
                        aliases = {"t", "tp"}
                )
        }
)
public class StructureLocateCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @ArgumentTarget("structure")
    private TerraStructure structure;

    @ArgumentTarget("radius")
    private Integer radius;

    @SwitchTarget("teleport")
    private boolean teleport;

    @Override
    public void execute(CommandSender sender) {
        if (!(sender instanceof Player)) return; //fix to fix a ClassCastException; will implement something to tell the console the nearest location near worldspawn
        Player player = (Player) sender;

        new Thread(new AsyncStructureFinder(main.getWorld(player.getWorld()).getBiomeProvider(), structure, player.getLocation().clone().multiply((1D / main.getTerraConfig().getBiomeSearchResolution())), 0, radius, location -> {
            if(location != null) {
                sender.sendMessage(String.format("The nearest %s is at [%d, ~, %d] (%.1f blocks away)", structure.getTemplate().getID().toLowerCase(Locale.ROOT), location.getBlockX(), location.getBlockZ(), location.add(new Vector3(0, player.getLocation().getY(), 0)).distance(player.getLocation().toVector())));
                if(teleport) {
                    main.runPossiblyUnsafeTask(() -> player.setLocation(new Location(player.getWorld(), location.getX(), player.getLocation().getY(), location.getZ())));
                }
            } else LangUtil.send("command.biome.unable-to-locate", sender);
        }, main), "Biome Location Thread").start();
    }
}
