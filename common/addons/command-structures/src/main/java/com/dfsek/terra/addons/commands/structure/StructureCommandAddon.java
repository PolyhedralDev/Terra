package com.dfsek.terra.addons.commands.structure;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.LongArgument;
import cloud.commandframework.context.CommandContext;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.command.arguments.RegistryArgument;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.util.reflection.TypeKey;

import org.jetbrains.annotations.NotNull;


public class StructureCommandAddon implements MonadAddonInitializer {
    private static Registry<Structure> getStructureRegistry(CommandContext<CommandSender> sender) {
        return sender.getSender().getEntity().orElseThrow().world().getPack().getRegistry(Structure.class);
    }
    
    @Override
    public @NotNull Monad<?, Init<?>> initialize() {
        return Do.with(
                Get.eventManager().map(eventManager -> eventManager.getHandler(FunctionalEventHandler.class)),
                Get.addon(),
                ((handler, base) -> Init.ofPure(
                        handler.register(base, CommandRegistrationEvent.class)
                               .then(event -> {
                                   CommandManager<CommandSender> manager = event.getCommandManager();
                            
                                   manager.command(
                                           manager.commandBuilder("structures", ArgumentDescription.of("Manage or generate structures"))
                                                  .literal("generate")
                                                  .argument(RegistryArgument.builder("structure",
                                                                                     StructureCommandAddon::getStructureRegistry,
                                                                                     TypeKey.of(Structure.class)))
                                                  .argument(LongArgument.optional("seed", 0))
                                                  .argument(EnumArgument.optional(Rotation.class, "rotation", Rotation.NONE))
                                                  .handler(context -> {
                                                      Structure structure = context.get("structure");
                                                      Entity sender = context.getSender().getEntity().orElseThrow();
                                                      structure.generate(
                                                              sender.position().toInt(),
                                                              sender.world(),
                                                              context.get("rotation"));
                                                  })
                                                  .permission("terra.structures.generate")
                                                  );
                               })))
                      );
    }
}
