package com.dfsek.terra.commands;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.config.lang.LangUtil;


@Command(
        usage = "/terra packs"
)
public class PacksCommand implements CommandTemplate {
    @Inject
    private Platform platform;
    
    @Override
    public void execute(CommandSender sender) {
        CheckedRegistry<ConfigPack> registry = platform.getConfigRegistry();
        
        if(registry.entries().size() == 0) {
            LangUtil.send("command.packs.none", sender);
            return;
        }
        
        LangUtil.send("command.packs.main", sender);
        registry.entries().forEach(
                entry -> LangUtil.send("command.packs.pack", sender, entry.getID(), entry.getAuthor(), entry.getVersion()));
    }
}
