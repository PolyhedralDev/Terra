package com.dfsek.terra.addons.commands.structure;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.LongArgument;
import cloud.commandframework.context.CommandContext;

import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Do;
import com.dfsek.terra.addons.manifest.api.monad.Get;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.command.arguments.RegistryArgument;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.util.reflection.TypeKey;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class StructureCommandAddon implements MonadAddonInitializer {
    private static Registry<Structure> getStructureRegistry(CommandContext<CommandSender> sender) {
        return sender.getSender().getEntity().orElseThrow().world().getPack().getRegistry(Structure.class);
    }
    
    @Override
    public Monad<?, Init<?>> initialize() {
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
                                               ((Long) context.get("seed") == 0)
                                               ? RandomGeneratorFactory.<RandomGenerator.SplittableGenerator>of("Xoroshiro128PlusPlus")
                                                                       .create()
                                               : RandomGeneratorFactory.<RandomGenerator.SplittableGenerator>of("Xoroshiro128PlusPlus")
                                                                       .create(context.get("seed")),
                                               context.get("rotation")
                                                         );
                                                  })
                                                  .permission("terra.structures.generate")
                                                  );
                               })))
                      );
    }
}
