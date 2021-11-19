/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure.command.structure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.dfsek.terra.api.Platform;
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
    @Inject
    private Platform platform;
    
    @ArgumentTarget("id")
    private String id;
    
    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        
        Pair<Vector3, Vector3> l = platform.getWorldHandle().getSelectedLocation(player);
        
        Vector3 l1 = l.getLeft();
        Vector3 l2 = l.getRight();
        
        StringBuilder scriptBuilder = new StringBuilder("id \"" + id + "\";\nnum y = 0;\n");
        
        int centerX = 0;
        int centerY = 0;
        int centerZ = 0;
        
        for(int x = l1.getBlockX(); x <= l2.getBlockX(); x++) {
            for(int y = l1.getBlockY(); y <= l2.getBlockY(); y++) {
                for(int z = l1.getBlockZ(); z <= l2.getBlockZ(); z++) {
                    BlockEntity state = player.world().getBlockState(x, y, z);
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
                    
                    BlockState data = player.world().getBlockData(x, y, z);
                    if(data.isStructureVoid()) continue;
                    BlockEntity state = player.world().getBlockState(x, y, z);
                    if(state instanceof Sign) {
                        Sign sign = (Sign) state;
                        if(sign.getLine(0).equals("[TERRA]")) {
                            data = platform.getWorldHandle().createBlockData(sign.getLine(2) + sign.getLine(3));
                        }
                    }
                    if(!data.isStructureVoid()) {
                        scriptBuilder.append("block(").append(x - l1.getBlockX() - centerX).append(", y + ").append(
                                             y - l1.getBlockY() - centerY).append(", ").append(z - l1.getBlockZ() - centerZ).append(", ")
                                     .append("\"");
                        scriptBuilder.append(data.getAsString()).append("\");\n");
                    }
                }
            }
        }
        
        File file = new File(platform.getDataFolder() + File.separator + "export" + File.separator + "structures", id + ".tesf");
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
