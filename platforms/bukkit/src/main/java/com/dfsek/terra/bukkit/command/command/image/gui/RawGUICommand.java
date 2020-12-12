package com.dfsek.terra.bukkit.command.command.image.gui;

import com.dfsek.terra.bukkit.command.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class RawGUICommand extends WorldCommand {
    public RawGUICommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
       /*
        if(!getMain().isDebug()) {
            LangUtil.send("command.image.gui.debug", sender);
            return true;
        }
        ImageLoader loader = ((TerraBukkitPlugin) getMain()).getWorld(world).getConfig().getTemplate().getImageLoader();
        if(loader != null) loader.debug(false, sender.getWorld(), (TerraBukkitPlugin) getMain());
        else ImageLoader.debugWorld(false, world, (TerraBukkitPlugin) getMain());

        */
        return true;
    }

    @Override
    public String getName() {
        return "raw";
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
