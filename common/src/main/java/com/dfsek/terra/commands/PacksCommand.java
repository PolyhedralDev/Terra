package com.dfsek.terra.commands;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.pack.ConfigPackTemplate;

@Command(
        usage = "/terra packs"
)
public class PacksCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        CheckedRegistry<ConfigPack> registry = main.getConfigRegistry();

        if(registry.entries().size() == 0) {
            LangUtil.send("command.packs.none", sender);
            return;
        }

        LangUtil.send("command.packs.main", sender);
        registry.entries().forEach(entry -> {
            ConfigPackTemplate template = entry.getTemplate();
            LangUtil.send("command.packs.pack", sender, template.getID(), template.getAuthor(), template.getVersion());
        });
    }
}
