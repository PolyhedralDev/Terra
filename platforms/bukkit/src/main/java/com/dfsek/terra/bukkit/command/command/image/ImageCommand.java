package com.dfsek.terra.bukkit.command.command.image;

import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.bukkit.command.command.image.gui.GUICommand;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImageCommand extends WorldCommand {
    public ImageCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        LangUtil.send("command.image.main-menu", new BukkitCommandSender(sender));
        return true;
    }

    @Override
    public String getName() {
        return "image";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Arrays.asList(new RenderCommand(this), new GUICommand(this));
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
