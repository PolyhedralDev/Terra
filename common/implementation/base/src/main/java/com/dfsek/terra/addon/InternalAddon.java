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

package com.dfsek.terra.addon;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;
import cloud.commandframework.Description;

import cloud.commandframework.arguments.standard.StringArgument;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;


public class InternalAddon implements BaseAddon {
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    
    public InternalAddon(Platform platform) {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, CommandRegistrationEvent.class)
                .then(event -> {
                    CommandManager<CommandSender> manager = event.getCommandManager();
                    manager.command(
                            manager.commandBuilder("addons", ArgumentDescription.of("Get information about installed Terra addons"))
                                   .handler(context -> {
                                       StringBuilder addons = new StringBuilder("Installed addons:\n");
                                       platform.getAddons()
                                               .forEach(addon -> addons
                                                       .append("- ")
                                                       .append(addon.getID())
                                                       .append('@')
                                                       .append(addon.getVersion())
                                                       .append('\n'));
                                       context.getSender().sendMessage(addons.toString());
                                   })
                                   );
            
            
                });
        
    }
    
    @Override
    public String getID() {
        return "terra";
    }
    
    @Override
    public Version getVersion() {
        return VERSION;
    }
}
