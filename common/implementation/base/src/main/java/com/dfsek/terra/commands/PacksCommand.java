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

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;


@Command(
        usage = "/terra packs"
)
public class PacksCommand implements CommandTemplate {
    @Inject
    private Platform platform;
    
    @Override
    public void execute(CommandSender sender) {
        CheckedRegistry<ConfigPack> registry = platform.getConfigRegistry();
        
        if(registry.entries().isEmpty()) {
            sender.sendMessage("No packs installed");
            return;
        }
        registry.entries().forEach(configPack -> sender.sendMessage(" - " + configPack.getID() + "@" + configPack.getVersion().getFormatted()));
    }
}
