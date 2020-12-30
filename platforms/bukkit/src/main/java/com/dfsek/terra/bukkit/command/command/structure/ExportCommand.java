package com.dfsek.terra.bukkit.command.command.structure;

import com.dfsek.terra.bukkit.command.PlayerCommand;
import com.dfsek.terra.bukkit.structure.WorldEditUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ExportCommand extends PlayerCommand {
    public ExportCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Location[] l = WorldEditUtil.getSelectionLocations(sender);
        if(l == null) return true;

        Location l1 = l[0];
        Location l2 = l[1];

        StringBuilder scriptBuilder = new StringBuilder("id \"" + args[0] + "\";\nnum y = 0;\n");

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
                    String data;
                    Block block = new Location(l1.getWorld(), x, y, z).getBlock();
                    if(block.getType().equals(Material.STRUCTURE_VOID)) continue;
                    scriptBuilder.append("block(").append(x - l1.getBlockX() - centerX).append(", y + ").append(y - l1.getBlockY() - centerY).append(", ").append(z - l1.getBlockZ() - centerZ).append(", ")
                            .append("\"");
                    BlockState state = block.getState();
                    if(state instanceof Sign) {
                        Sign sign = (Sign) state;
                        if(sign.getLine(0).equals("[TERRA]")) {
                            data = sign.getLine(2) + sign.getLine(3);
                            BlockData data1 = Bukkit.createBlockData(sign.getLine(2) + sign.getLine(3));
                            if(data1.getMaterial().equals(Material.STRUCTURE_VOID)) continue;
                            scriptBuilder.append(data1.getAsString(false));
                        } else {
                            data = block.getBlockData().getAsString(false);
                        }
                    } else {
                        data = block.getBlockData().getAsString(false);
                    }
                    scriptBuilder.append(data).append("\");\n");
                }
            }
        }

        File file = new File(getMain().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tesf");
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



        /*
        Structure structure;
        try {
            structure = new Structure(l1, l2, args[0]);
        } catch(InitializationException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }
        try {
            File file = new File(getMain().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure");
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            structure.save(file);
            LangUtil.send("command.structure.export", sender, file.getAbsolutePath());
        } catch(IOException e) {
            e.printStackTrace();
        }

         */
        return true;
    }

    @Override
    public String getName() {
        return "export";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
