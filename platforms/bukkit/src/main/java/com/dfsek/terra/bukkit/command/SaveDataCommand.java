package com.dfsek.terra.bukkit.command;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.config.lang.LangUtil;

@Command
public class SaveDataCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {
        BukkitChunkGeneratorWrapper.saveAll();
        LangUtil.send("debug.data-save", sender);
    }
}
