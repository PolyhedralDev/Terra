package com.dfsek.terra.commands;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.inject.annotations.Inject;


@Command(
        usage = "/terra addons"
)
public class AddonsCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;
    
    @Override
    public void execute(CommandSender sender) {
        sender.sendMessage("Installed Addons:");
        main.getAddons().forEach(
                addon -> sender.sendMessage(" - " + addon.getName() + " v" + addon.getVersion() + " by " + addon.getAuthor()));
    }
}
