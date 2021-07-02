package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.addons.structure.structures.parser.lang.constants.NumericConstant;
import com.dfsek.terra.addons.structure.structures.script.TerraImplementationArguments;
import com.dfsek.terra.addons.structure.structures.script.functions.CheckFunction;
import com.dfsek.terra.addons.structure.structures.structure.buffer.StructureBuffer;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.vector.Vector3;

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
        Vector3 p = player.position();
        int x = p.getBlockX();
        int y = p.getBlockY();
        int z = p.getBlockZ();
        Position dummy = new Position(0, 0);

        String check = new CheckFunction(main, new NumericConstant(0, dummy), new NumericConstant(0, dummy), new NumericConstant(0, dummy), dummy).apply(new TerraImplementationArguments(new StructureBuffer(
                new Vector3(x, y, z)
        ), Rotation.NONE, new FastRandom(), player.world(), 0), new HashMap<>());

        sender.sendMessage("Found: " + check);
    }
}
