package com.dfsek.terra.command.image;

import com.dfsek.terra.Terra;
import com.dfsek.terra.command.WorldCommand;
import com.dfsek.terra.image.WorldImageGenerator;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class RenderCommand extends WorldCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        try {
            WorldImageGenerator g = new WorldImageGenerator(world, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            g.drawWorld(sender.getLocation().getBlockX(), sender.getLocation().getBlockZ());
            File file = new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "map" + File.separator + "map_" + System.currentTimeMillis() + ".png");
            file.mkdirs();
            file.createNewFile();
            g.save(file);
            sender.sendMessage("Saved image to " + file.getPath());
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            sender.sendMessage("An error occurred while generating the image!");
            return true;
        }
    }

    @Override
    public String getName() {
        return "render";
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 2;
    }
}
