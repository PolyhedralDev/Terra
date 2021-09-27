package com.dfsek.terra.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(ReloadCommand.class);
    
    @Inject
    private Platform platform;
    
    @Override
    public void execute(CommandSender sender) {
        logger.info("Reloading Terra...");
        if(platform.reload()) {
            logger.info("Terra reloaded successfully.");
            LangUtil.send("command.reload", sender);
        } else {
            logger.warn("Terra failed to reload.");
            LangUtil.send("command.reload-error", sender);
        }
    }
}
