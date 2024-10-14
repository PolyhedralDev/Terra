package com.dfsek.terra.addons.commands.profiler;

import org.incendo.cloud.CommandManager;
import org.incendo.cloud.description.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


public class ProfilerCommandAddon implements AddonInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ProfilerCommandAddon.class);

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
                manager
                    .command(
                        manager.commandBuilder("profiler", Description.of("Access the profiler"))
                            .literal("start", Description.of("Start profiling"), "st")
                            .permission("terra.profiler.start")
                            .handler(context -> {
                                platform.getProfiler().start();
                                context.sender().sendMessage("Profiling started.");
                            }))
                    .command(
                        manager.commandBuilder("profiler", Description.of("Access the profiler"))
                            .literal("stop", Description.of("Stop profiling"), "s")
                            .permission("terra.profiler.stop")
                            .handler(context -> {
                                platform.getProfiler().stop();
                                context.sender().sendMessage("Profiling stopped.");
                            }))
                    .command(
                        manager.commandBuilder("profiler", Description.of("Access the profiler"))
                            .literal("query", Description.of("Query profiler results"), "q")
                            .permission("terra.profiler.query")
                            .handler(context -> {
                                StringBuilder data = new StringBuilder("Terra Profiler data: \n");
                                platform.getProfiler().getTimings().forEach((id, timings) -> data.append(id)
                                    .append(": ")
                                    .append(timings.toString())
                                    .append('\n'));
                                logger.info(data.toString());
                                context.sender().sendMessage("Profiling data dumped to console.");
                            }))
                    .command(
                        manager.commandBuilder("profiler", Description.of("Access the profiler"))
                            .literal("reset", Description.of("Reset the profiler"), "r")
                            .permission("terra.profiler.reset")
                            .handler(context -> {
                                platform.getProfiler().reset();
                                context.sender().sendMessage("Profiler reset.");
                            }));
            });
    }
}
