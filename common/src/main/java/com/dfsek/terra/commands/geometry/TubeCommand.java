package com.dfsek.terra.commands.geometry;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.arg.IntegerArgumentParser;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.math.voxel.Tube;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.util.generic.pair.Pair;

@DebugCommand
@PlayerCommand
@Command(
        arguments = {
                @Argument(
                        value = "radius",
                        argumentParser = IntegerArgumentParser.class
                )
        },
        usage = "/terra geometry tube <RADIUS>"
)
public class TubeCommand implements CommandTemplate {
    @ArgumentTarget("radius")
    private Integer radius;

    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;

        Pair<Location, Location> locations = main.getWorldHandle().getSelectedLocation(player);

        Tube tube = new Tube(locations.getLeft().toVector(), locations.getRight().toVector(), radius);

        for(Vector3 v : tube.getGeometry()) {
            v.toLocation(player.getWorld()).getBlock().setBlockData(main.getWorldHandle().createBlockData("minecraft:stone"), false);
        }
    }
}
