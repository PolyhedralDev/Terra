package com.dfsek.terra.commands;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.config.lang.LangUtil;


@Command(
        usage = "/terra reload"
)
public class ReloadCommand implements CommandTemplate {
    @Inject
    private Platform platform;
    
    @Override
    public void execute(CommandSender sender) {
        if(!platform.reload()) {
            LangUtil.send("command.reload-error", sender);
        } else {
            LangUtil.send("command.reload", sender);
        }
    }
}
