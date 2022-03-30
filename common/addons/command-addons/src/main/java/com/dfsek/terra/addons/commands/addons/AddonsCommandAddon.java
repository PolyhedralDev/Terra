package com.dfsek.terra.addons.commands.addons;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.command.arguments.RegistryArgument;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


public class AddonsCommandAddon implements AddonInitializer {
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, CommandRegistrationEvent.class)
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
                                   );
                });
    }
}
