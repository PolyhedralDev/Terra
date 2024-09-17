package com.dfsek.terra.addons.commands.packs;

import org.incendo.cloud.CommandManager;
import org.incendo.cloud.description.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.command.arguments.RegistryArgument;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


public class PacksCommandAddon implements AddonInitializer {
    private static final Logger logger = LoggerFactory.getLogger(PacksCommandAddon.class);

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
                        manager.commandBuilder("packs", Description.of("List installed config packs"))
                            .permission("terra.packs")
                            .handler(context -> {
                                StringBuilder packs = new StringBuilder("Installed packs:\n");
                                platform.getConfigRegistry().forEach(pack -> packs.append(" - ")
                                    .append(pack.getID())
                                    .append('@')
                                    .append(pack.getVersion().getFormatted()));
                                context.sender().sendMessage(packs.toString());
                            })
                    )
                    .command(
                        manager.commandBuilder("packs")
                            .literal("info", Description.of("Get information about a pack"))
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
                                context.sender().sendMessage(packInfo.toString());
                            }))
                    .command(
                        manager.commandBuilder("packs")
                            .literal("reload", Description.of("Reload config packs"))
                            .permission("terra.packs.reload")
                            .handler(context -> {
                                context.sender().sendMessage("Reloading Terra...");
                                logger.info("Reloading Terra...");
                                if(platform.reload()) {
                                    logger.info("Terra reloaded successfully.");
                                    context.sender().sendMessage("Terra reloaded successfully.");
                                } else {
                                    logger.error("Terra failed to reload.");
                                    context.sender().sendMessage(
                                        "Terra failed to reload. See logs for more information.");
                                }
                            }));
            });
    }
}
