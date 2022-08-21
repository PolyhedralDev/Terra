package com.dfsek.terra.addons.commands.profiler;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;

import com.dfsek.terra.api.util.function.monad.Monad;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;


public class ProfilerCommandAddon implements MonadAddonInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ProfilerCommandAddon.class);
    
    @Override
    public @NotNull Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                Get.platform(),
                ((handler, base, platform) -> Init.ofPure(
                        handler.register(base, CommandRegistrationEvent.class)
                               .then(event -> {
                                   CommandManager<CommandSender> manager = event.getCommandManager();
                                   manager
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
                                                          }));
                               })))
                      );
    }
}
