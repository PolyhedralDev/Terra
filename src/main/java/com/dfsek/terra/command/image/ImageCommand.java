package com.dfsek.terra.command.image;

import com.dfsek.terra.Terra;
import com.dfsek.terra.command.PlayerCommand;
import com.dfsek.terra.config.WorldConfig;
import com.dfsek.terra.image.WorldImageGenerator;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ImageCommand extends PlayerCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if("render".equals(args[0])) {
            if(args.length != 4) return false;
            try {
                WorldImageGenerator g = new WorldImageGenerator(sender.getWorld(), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                g.drawWorld(sender.getLocation().getBlockX(), sender.getLocation().getBlockZ());
                File file = new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "map" + File.separator + "map_" + System.currentTimeMillis() + ".png");
                file.mkdirs();
                file.createNewFile();
                g.save(file);
                sender.sendMessage("Saved image to " + file.getPath());
                return true;
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if("gui".equals(args[0])) {
            try {
                if("raw".equals(args[1]))
                    WorldConfig.fromWorld(sender.getWorld()).imageLoader.debug(false, sender.getWorld());
                else if("step".equals(args[1]))
                    WorldConfig.fromWorld(sender.getWorld()).imageLoader.debug(true, sender.getWorld());
                else {
                    return true;
                }
                return true;
            } catch(NullPointerException e) {
                return true;
            }
        }
        return true;
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return "image";
    }
}
