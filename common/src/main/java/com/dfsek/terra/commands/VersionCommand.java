package com.dfsek.terra.commands;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.config.lang.LangUtil;

@Command
public class VersionCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        String terraVersion = main.getVersion();
        LangUtil.send("command.version", sender, terraVersion, main.platformName());
    }
}
