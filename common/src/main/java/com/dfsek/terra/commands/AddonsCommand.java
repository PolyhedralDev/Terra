package com.dfsek.terra.commands;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;

@Command(
        usage = "/terra addons"
)
public class AddonsCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        sender.sendMessage("Installed Addons:");
        main.getAddons().forEach(addon -> sender.sendMessage(" - " + addon.getName() + " v" + addon.getVersion() + " by " + addon.getAuthor()));
    }
}
