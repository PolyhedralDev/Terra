package com.dfsek.terra.command.image.gui;

import com.dfsek.terra.Terra;
import com.dfsek.terra.api.gaea.command.WorldCommand;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.image.ImageLoader;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class StepGUICommand extends WorldCommand {
    public StepGUICommand(com.dfsek.terra.api.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        if(!getMain().isDebug()) {
            LangUtil.send("command.image.gui.debug", sender);
            return true;
        }
        ImageLoader loader = ((Terra) getMain()).getWorld(world).getConfig().getTemplate().getImageLoader();
        if(loader != null) loader.debug(true, sender.getWorld(), (Terra) getMain());
        else ImageLoader.debugWorld(true, world, (Terra) getMain());
        return true;
    }

    @Override
    public String getName() {
        return "step";
    }

    @Override
    public List<com.dfsek.terra.api.gaea.command.Command> getSubCommands() {
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
