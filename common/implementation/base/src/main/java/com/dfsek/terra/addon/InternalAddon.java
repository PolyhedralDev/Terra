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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.command.arguments.RegistryArgument;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;


public class InternalAddon implements BaseAddon {
    private static final Logger logger = LoggerFactory.getLogger(InternalAddon.class);
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    
    public InternalAddon(Platform platform) {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, CommandRegistrationEvent.class)
                .then(event -> {
                    CommandManager<CommandSender> manager = event.getCommandManager();
                    manager.command(
                                   manager.commandBuilder("addons", ArgumentDescription.of("List installed Terra addons"))
                                          .permission("terra.addons")
                                          .handler(context -> {
                                              StringBuilder addons = new StringBuilder("Installed addons:\n");
                                              platform.getAddons()
                                                      .forEach(addon -> addons
                                                              .append(" - ")
                                                              .append(addon.getID())
                                                              .append('@')
                                                              .append(addon.getVersion().getFormatted())
                                                              .append('\n'));
                                              context.getSender().sendMessage(addons.toString());
                                          })
                                   )
                           .command(
                                   manager.commandBuilder("addons")
                                          .argument(RegistryArgument.of("addon", platform.getAddons()))
                                          .permission("terra.addons.info")
                                          .handler(context -> {
                                              BaseAddon addon = context.get("addon");
                                              StringBuilder addonInfo = new StringBuilder("Addon ").append(addon.getID()).append('\n');
                        
                                              addonInfo.append("Version: ").append(addon.getVersion().getFormatted()).append('\n');
                        
                                              addonInfo.append("Dependencies:\n");
                                              addon.getDependencies().forEach((id, versions) -> addonInfo
                                                      .append(" - ")
                                                      .append(id)
                                                      .append('@')
                                                      .append(versions.getFormatted())
                                                      .append('\n'));
                                              context.getSender().sendMessage(addonInfo.toString());
                                          })
                                   )
                           .command(
                                   manager.commandBuilder("packs", ArgumentDescription.of("List installed config packs"))
                                          .permission("terra.packs")
                                          .handler(context -> {
                                              StringBuilder packs = new StringBuilder("Installed packs:\n");
                                              platform.getConfigRegistry().forEach(pack -> packs.append(" - ")
                                                                                                .append(pack.getID())
                                                                                                .append('@')
                                                                                                .append(pack.getVersion().getFormatted()));
                                              context.getSender().sendMessage(packs.toString());
                                          })
                                   )
                           .command(
                                   manager.commandBuilder("packs")
                                          .literal("info", ArgumentDescription.of("Get information about a pack"))
                                          .permission("terra.packs.info")
                                          .argument(RegistryArgument.of("pack", platform.getConfigRegistry()))
                                          .handler(context -> {
                                              ConfigPack pack = context.get("pack");
                                              StringBuilder packInfo = new StringBuilder("Pack ").append(pack.getID()).append('\n');
                        
                                              packInfo.append("Version: ").append(pack.getVersion().getFormatted()).append('\n');
                                              packInfo.append("Author: ").append(pack.getAuthor()).append('\n');
                        
                                              packInfo.append("Addon Dependencies:\n");
                                              pack.addons().forEach((id, versions) -> packInfo
                                                      .append(" - ")
                                                      .append(id.getID())
                                                      .append('@')
                                                      .append(versions.getFormatted())
                                                      .append('\n'));
                                              context.getSender().sendMessage(packInfo.toString());
                                          }))
                           .command(
                                   manager.commandBuilder("packs")
                                          .literal("reload", ArgumentDescription.of("Reload config packs"))
                                          .permission("terra.packs.reload")
                                          .handler(context -> {
                                              context.getSender().sendMessage("Reloading Terra...");
                                              logger.info("Reloading Terra...");
                                              if(platform.reload()) {
                                                  logger.info("Terra reloaded successfully.");
                                                  context.getSender().sendMessage("Terra reloaded successfully.");
                                              } else {
                                                  logger.error("Terra failed to reload.");
                                                  context.getSender().sendMessage(
                                                          "Terra failed to reload. See logs for more information.");
                                              }
                                          }))
                           .command(
                                   manager.commandBuilder("profiler", ArgumentDescription.of("Access the profiler"))
                                          .literal("start", ArgumentDescription.of("Start profiling"), "st")
                                          .permission("terra.profiler.start")
                                          .handler(context -> {
                                              platform.getProfiler().start();
                                              context.getSender().sendMessage("Profiling started.");
                                          }))
                           .command(
                                   manager.commandBuilder("profiler", ArgumentDescription.of("Access the profiler"))
                                          .literal("stop", ArgumentDescription.of("Stop profiling"), "s")
                                          .permission("terra.profiler.stop")
                                          .handler(context -> {
                                              platform.getProfiler().stop();
                                              context.getSender().sendMessage("Profiling stopped.");
                                          }))
                           .command(
                                   manager.commandBuilder("profiler", ArgumentDescription.of("Access the profiler"))
                                          .literal("query", ArgumentDescription.of("Query profiler results"), "q")
                                          .permission("terra.profiler.query")
                                          .handler(context -> {
                                              StringBuilder data = new StringBuilder("Terra Profiler data: \n");
                                              platform.getProfiler().getTimings().forEach((id, timings) -> data.append(id)
                                                                                                               .append(": ")
                                                                                                               .append(timings.toString())
                                                                                                               .append('\n'));
                                              logger.info(data.toString());
                                              context.getSender().sendMessage("Profiling data dumped to console.");
                                          }))
                           .command(
                                   manager.commandBuilder("profiler", ArgumentDescription.of("Access the profiler"))
                                          .literal("reset", ArgumentDescription.of("Reset the profiler"), "r")
                                          .permission("terra.profiler.reset")
                                          .handler(context -> {
                                              platform.getProfiler().reset();
                                              context.getSender().sendMessage("Profiler reset.");
                                          }))
                    ;
            
            
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
