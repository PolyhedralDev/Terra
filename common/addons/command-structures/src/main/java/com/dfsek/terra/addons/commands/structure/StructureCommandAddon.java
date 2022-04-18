package com.dfsek.terra.addons.commands.structure;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.LongArgument;
import cloud.commandframework.context.CommandContext;

import java.util.Random;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.command.CommandSender;
import com.dfsek.terra.api.command.arguments.RegistryArgument;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class StructureCommandAddon implements AddonInitializer {
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    private static Registry<Structure> getStructureRegistry(CommandContext<CommandSender> sender) {
        return sender.getSender().getEntity().orElseThrow().world().getPack().getRegistry(Structure.class);
    }
    
    @Override
    public void initialize() {
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, CommandRegistrationEvent.class)
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
                                               ((Long) context.get("seed") == 0) ? new Random() : new Random(context.get("seed")),
                                               context.get("rotation")
                                                         );
                                   })
                                   .permission("terra.structures.generate")
                                   );
                });
    }
}
