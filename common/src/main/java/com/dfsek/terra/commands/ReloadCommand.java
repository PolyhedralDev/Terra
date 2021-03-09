package com.dfsek.terra.commands;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.config.lang.LangUtil;

@Command()
public class ReloadCommand implements CommandTemplate {
    @Inject
    private TerraPlugin main;

    @Override
    public void execute(CommandSender sender) {
        if(!main.reload()) {
            LangUtil.send("command.reload-error", sender);
        } else {
            LangUtil.send("command.reload", sender);
        }
    }
}
