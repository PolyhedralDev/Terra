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
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.math.voxel.Sphere;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;

@DebugCommand
@PlayerCommand
@Command(
        arguments = {
                @Argument(
                        value = "radius",
                        argumentParser = IntegerArgumentParser.class
                )
        },
        usage = "/terra geometry sphere <RADIUS>"
)
public class SphereCommand implements CommandTemplate {
    @ArgumentTarget("radius")
    private Integer radius;

    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;

        Sphere sphere = new Sphere(player.getLocation().toVector(), radius);
        for(Vector3 v : sphere.getGeometry()) {
            v.toLocation(player.getWorld()).getBlock().setBlockData(main.getWorldHandle().createBlockData("minecraft:stone"), false);
        }
    }
}
