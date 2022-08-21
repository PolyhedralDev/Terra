package com.dfsek.terra.addons.commands.addons;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.command.arguments.RegistryArgument;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.function.monad.Monad;


public class AddonsCommandAddon implements MonadAddonInitializer {
    @Override
    public Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((functionalEventHandler, base, platform) -> Init.ofPure(
                        functionalEventHandler
                                .register(base, CommandRegistrationEvent.class)
                                .then(event -> {
                                    CommandManager<CommandSender> manager = event.getCommandManager();
                                    
                                    manager.command(
                                                   manager.commandBuilder("addons", ArgumentDescription.of(
                                                                  "List installed Terra addons"))
                                                          .permission("terra.addons")
                                                          .handler(context -> {
                                                              StringBuilder addons = new StringBuilder(
                                                                      "Installed addons:\n");
                                                              platform.addons()
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
                                                          .argument(
                                                                  RegistryArgument.of("addon", platform.addons()))
                                                          .permission("terra.addons.info")
                                                          .handler(context -> {
                                                              BaseAddon addon = context.get("addon");
                                                              StringBuilder addonInfo = new StringBuilder(
                                                                      "Addon ").append(addon.getID()).append('\n');
                                                
                                                              addonInfo.append("Version: ").append(
                                                                      addon.getVersion().getFormatted()).append('\n');
                                                
                                                              addonInfo.append("Dependencies:\n");
                                                              addon.getDependencies().forEach(
                                                                      (id, versions) -> addonInfo
                                                                              .append(" - ")
                                                                              .append(id)
                                                                              .append('@')
                                                                              .append(versions.getFormatted())
                                                                              .append('\n'));
                                                              context.getSender().sendMessage(addonInfo.toString());
                                                          })
                                                   );
                                })))
                      );
    }
}
