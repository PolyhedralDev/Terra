package com.dfsek.terra.addons.structure.command.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Argument;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.inject.ArgumentTarget;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.command.annotation.type.PlayerCommand;
import com.dfsek.terra.api.command.annotation.type.WorldCommand;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.vector.Vector3;


@PlayerCommand
@WorldCommand
@DebugCommand
@Command(arguments = @Argument("id"), usage = "/terra structure export <ID>")
public class StructureExportCommand implements CommandTemplate {
    private static final Logger logger = LoggerFactory.getLogger(StructureExportCommand.class);
    
    @Inject
    private TerraPlugin main;
    
    @ArgumentTarget("id")
    private String id;
    
    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        
        Pair<Vector3, Vector3> area = main.getWorldHandle().getSelectedLocation(player);
        
        Vector3 firstCorner = area.getLeft();
        Vector3 secondCorner = area.getRight();
        
        StringBuilder scriptBuilder = new StringBuilder("id \"" + id + "\";\nnum y = 0;\n");
        
        int centerX = 0;
        int centerY = 0;
        int centerZ = 0;
        
        for(int x = firstCorner.getBlockX(); x <= secondCorner.getBlockX(); x++) {
            for(int y = firstCorner.getBlockY(); y <= secondCorner.getBlockY(); y++) {
                for(int z = firstCorner.getBlockZ(); z <= secondCorner.getBlockZ(); z++) {
                    BlockEntity state = player.world().getBlockState(x, y, z);
                    if(state instanceof Sign sign) {
                        if("[TERRA]".equals(sign.getLine(0)) && "[CENTER]".equals(sign.getLine(1))) {
                            centerX = x - firstCorner.getBlockX();
                            centerY = y - firstCorner.getBlockY();
                            centerZ = z - firstCorner.getBlockZ();
                        }
                    }
                }
            }
        }
        
        for(int x = firstCorner.getBlockX(); x <= secondCorner.getBlockX(); x++) {
            for(int y = firstCorner.getBlockY(); y <= secondCorner.getBlockY(); y++) {
                for(int z = firstCorner.getBlockZ(); z <= secondCorner.getBlockZ(); z++) {
                    
                    BlockState data = player.world().getBlockData(x, y, z);
                    if(data.isStructureVoid()) continue;
                    BlockEntity state = player.world().getBlockState(x, y, z);
                    if(state instanceof Sign sign) {
                        if("[TERRA]".equals(sign.getLine(0))) {
                            data = main.getWorldHandle().createBlockData(sign.getLine(2) + sign.getLine(3));
                        }
                    }
                    if(!data.isStructureVoid()) {
                        scriptBuilder.append("block(").append(x - firstCorner.getBlockX() - centerX).append(", y + ").append(
                                             y - firstCorner.getBlockY() - centerY).append(", ").append(z - firstCorner.getBlockZ() - centerZ).append(", ")
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
            logger.error("Error creating file to export", e);
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(scriptBuilder.toString());
        } catch(IOException e) {
            logger.error("Error writing script file", e);
        }
        
        sender.sendMessage("Exported structure to " + file.getAbsolutePath());
    }
}
