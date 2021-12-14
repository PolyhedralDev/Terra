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

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.commands.profiler.ProfileCommand;


public final class CommandUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommandUtil.class);
    
    public static void registerAll(CommandManager manager) throws MalformedCommandException {
        logger.info("Registering Terra commands...");
        manager.register("profile", ProfileCommand.class);
        manager.register("reload", ReloadCommand.class);
        manager.register("addons", AddonsCommand.class);
        manager.register("version", VersionCommand.class);
        manager.register("getblock", GetBlockCommand.class);
        manager.register("packs", PacksCommand.class);
    }
}
