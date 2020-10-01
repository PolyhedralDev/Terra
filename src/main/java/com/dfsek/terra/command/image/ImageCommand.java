package com.dfsek.terra.command.image;

import com.dfsek.terra.command.type.WorldCommand;
import com.dfsek.terra.command.image.gui.GUICommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ImageCommand extends WorldCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        sender.sendMessage("---------------Terra/image---------------");
        sender.sendMessage("render - Render an image with a given width and height, that can later be imported as a world.");
        sender.sendMessage("gui    - Open debug GUI (Must be enabled in config)");
        return true;
    }

    @Override
    public List<com.dfsek.terra.command.type.Command> getSubCommands() {
        return Arrays.asList(new RenderCommand(), new GUICommand());
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public String getName() {
        return "image";
    }
}
