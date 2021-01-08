package com.dfsek.terra.bukkit.command.command.image.gui;

import com.dfsek.terra.bukkit.command.DebugCommand;
import com.dfsek.terra.bukkit.command.WorldCommand;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.image.ImageLoader;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class StepGUICommand extends WorldCommand implements DebugCommand {
    public StepGUICommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        ImageLoader loader = (getMain()).getWorld(BukkitAdapter.adapt(world)).getConfig().getTemplate().getImageLoader();
        if(loader != null) loader.debug(true, BukkitAdapter.adapt(sender.getWorld()), getMain());
        else ImageLoader.debugWorld(true, BukkitAdapter.adapt(world), getMain());
        return true;
    }

    @Override
    public String getName() {
        return "step";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
