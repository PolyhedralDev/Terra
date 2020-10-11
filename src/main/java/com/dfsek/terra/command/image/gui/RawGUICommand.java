package com.dfsek.terra.command.image.gui;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.image.ImageLoader;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.WorldCommand;

import java.util.Collections;
import java.util.List;

public class RawGUICommand extends WorldCommand {
    public RawGUICommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        if(! ConfigUtil.debug) {
            LangUtil.send("command.image.gui.debug", sender);
            return true;
        }
        ImageLoader loader = TerraWorld.getWorld(world).getWorldConfig().imageLoader;
        if(loader != null) loader.debug(false, sender.getWorld());
        else ImageLoader.debugWorld(false, world);
        return true;
    }

    @Override
    public String getName() {
        return "raw";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
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
