/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.inject.annotations.Inject;


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
            sender.sendMessage("Terra reloaded successfully.");
        } else {
            logger.error("Terra failed to reload.");
            sender.sendMessage("Terra failed to reload. See logs for more information.");
        }
    }
}
