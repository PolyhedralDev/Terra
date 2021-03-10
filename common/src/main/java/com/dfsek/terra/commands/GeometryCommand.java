package com.dfsek.terra.commands;

import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.config.lang.LangUtil;

public class GeometryCommand implements CommandTemplate {
    @Override
    public void execute(CommandSender sender) {
        LangUtil.send("command.geometry.main-menu", sender);
    }
}
