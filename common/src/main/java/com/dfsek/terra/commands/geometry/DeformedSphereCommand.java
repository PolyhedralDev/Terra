package com.dfsek.terra.commands.geometry;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.arg.DoubleArgumentParser;
import com.dfsek.terra.api.command.arg.IntegerArgumentParser;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.math.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.math.voxel.DeformedSphere;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

@DebugCommand
@PlayerCommand
@Command(
        arguments = {
                @Argument(
                        value = "radius",
                        argumentParser = IntegerArgumentParser.class
                ),
                @Argument(
                        value = "deform",
                        argumentParser = DoubleArgumentParser.class
                ),
                @Argument(
                        value = "frequency",
                        argumentParser = DoubleArgumentParser.class
                )
        },
        usage = "/terra geometry deformedsphere <RADIUS> <DEFORM> <FREQUENCY>"
)
public class DeformedSphereCommand implements CommandTemplate {
    @ArgumentTarget("radius")
    private Integer radius;

    @ArgumentTarget("deform")
    private Double deform;

    @ArgumentTarget("frequency")
    private Double frequency;

    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;

        OpenSimplex2Sampler noise = new OpenSimplex2Sampler(ThreadLocalRandom.current().nextInt());
        noise.setFrequency(frequency);

        DeformedSphere sphere = new DeformedSphere(player.getLocation().toVector(), radius, deform, noise);
        for(Vector3 v : sphere.getGeometry()) {
            v.toLocation(player.getWorld()).getBlock().setBlockData(main.getWorldHandle().createBlockData("minecraft:stone"), false);
        }
    }
}
