package com.dfsek.terra.commands.structure;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.api.platform.block.state.Sign;
import com.dfsek.terra.api.platform.entity.Player;
import com.dfsek.terra.api.util.generic.pair.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@PlayerCommand
@WorldCommand
@DebugCommand
@Command(
        arguments = {
                @Argument(
                        value = "id"
                )
        },
        usage = "/terra structure export <ID>"
)
public class StructureExportCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @ArgumentTarget("id")
    private String id;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;

        Pair<Location, Location> l = main.getWorldHandle().getSelectedLocation(player);

        Location l1 = l.getLeft();
        Location l2 = l.getRight();

        StringBuilder scriptBuilder = new StringBuilder("id \"" + id + "\";\nnum y = 0;\n");

        int centerX = 0;
        int centerY = 0;
        int centerZ = 0;

        for(int x = l1.getBlockX(); x <= l2.getBlockX(); x++) {
            for(int y = l1.getBlockY(); y <= l2.getBlockY(); y++) {
                for(int z = l1.getBlockZ(); z <= l2.getBlockZ(); z++) {
                    Block block = new Location(l1.getWorld(), x, y, z).getBlock();
                    BlockState state = block.getState();
                    if(state instanceof Sign) {
                        Sign sign = (Sign) state;
                        if(sign.getLine(0).equals("[TERRA]") && sign.getLine(1).equals("[CENTER]")) {
                            centerX = x - l1.getBlockX();
                            centerY = y - l1.getBlockY();
                            centerZ = z - l1.getBlockZ();
                        }
                    }
                }
            }
        }

        for(int x = l1.getBlockX(); x <= l2.getBlockX(); x++) {
            for(int y = l1.getBlockY(); y <= l2.getBlockY(); y++) {
                for(int z = l1.getBlockZ(); z <= l2.getBlockZ(); z++) {

                    Block block = new Location(l1.getWorld(), x, y, z).getBlock();
                    BlockData data = block.getBlockData();
                    if(block.getBlockData().isStructureVoid()) continue;
                    BlockState state = block.getState();
                    if(state instanceof Sign) {
                        Sign sign = (Sign) state;
                        if(sign.getLine(0).equals("[TERRA]")) {
                            data = main.getWorldHandle().createBlockData(sign.getLine(2) + sign.getLine(3));
                        }
                    }
                    if(!data.isStructureVoid()) {
                        scriptBuilder.append("block(").append(x - l1.getBlockX() - centerX).append(", y + ").append(y - l1.getBlockY() - centerY).append(", ").append(z - l1.getBlockZ() - centerZ).append(", ")
                                .append("\"");
                        scriptBuilder.append(data.getAsString()).append("\");\n");
                    }
                }
            }
        }

        File file = new File(main.getDataFolder() + File.separator + "export" + File.separator + "structures", id + ".tesf");
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(scriptBuilder.toString());
        } catch(IOException e) {
            e.printStackTrace();
        }

        sender.sendMessage("Exported structure to " + file.getAbsolutePath());
    }
}
