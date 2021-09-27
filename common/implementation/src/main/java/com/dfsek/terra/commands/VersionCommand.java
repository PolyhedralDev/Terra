package com.dfsek.terra.commands;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.config.lang.LangUtil;


@Command(
        usage = "/terra version"
)
public class VersionCommand implements CommandTemplate {
    @Inject
    private Platform platform;
    
    @Override
    public void execute(CommandSender sender) {
        String terraVersion = platform.getVersion();
        LangUtil.send("command.version", sender, terraVersion, platform.platformName());
    }
}
