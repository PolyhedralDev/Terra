package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.structures.parser.lang.constants.NumericConstant;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.script.functions.CheckFunction;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.StructureBuffer;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.api.util.FastRandom;

import java.util.HashMap;

@DebugCommand
@PlayerCommand
@WorldCommand
@Command(
        usage = "/terra spawn"
)
public class SpawnCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        Location p = player.getLocation();
        int x = p.getBlockX();
        int y = p.getBlockY();
        int z = p.getBlockZ();
        Position dummy = new Position(0, 0);

        String check = new CheckFunction(main, new NumericConstant(0, dummy), new NumericConstant(0, dummy), new NumericConstant(0, dummy), dummy).apply(new TerraImplementationArguments(new StructureBuffer(
                new com.dfsek.terra.api.math.vector.Location(player.getWorld(), x, y, z)
        ), Rotation.NONE, new FastRandom(), 0), new HashMap<>());

        sender.sendMessage("Found: " + check);
    }
}
