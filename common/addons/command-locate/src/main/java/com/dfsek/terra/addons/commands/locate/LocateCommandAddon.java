package com.dfsek.terra.addons.commands.locate;


import com.dfsek.seismic.type.vector.Vector2Int;
import com.dfsek.seismic.type.vector.Vector3Int;

import com.dfsek.terra.api.util.generic.data.types.Maybe;

import org.incendo.cloud.CommandManager;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.parser.standard.IntegerParser;

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
import com.dfsek.terra.api.util.generic.data.types.Either;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.Biome;

import static com.dfsek.terra.api.util.generic.data.types.Either.collapse;


public class LocateCommandAddon implements AddonInitializer {
    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;

    private static Registry<Biome> getBiomeRegistry(CommandContext<CommandSender> sender) {
        return sender.sender().entity().orThrow().world().getPack().getRegistry(Biome.class);
    }

    @Override
    public void initialize() {
        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, CommandRegistrationEvent.class)
            .then(event -> {
                CommandManager<CommandSender> manager = event.getCommandManager();
                manager.command(
                    manager.commandBuilder("search", Description.of("Locate things in the world"))
                        .literal("biome")
                        // Argument 1: The Biome to search for
                        .argument(RegistryArgument.builder("biome",
                            LocateCommandAddon::getBiomeRegistry,
                            TypeKey.of(Biome.class)))
                        // Argument 2: Radius (Optional, default 5000)
                        .optional("radius", IntegerParser.integerParser(100), DefaultValue.constant(5000))
                        // Argument 3: Step/Resolution (Optional, default 16)
                        .optional("step", IntegerParser.integerParser(1), DefaultValue.constant(16))
                        // Flag: Toggle 3D search (e.g., --3d or -3)
                        .flag(manager.flagBuilder("3d").withAliases("3").build())
                        // Flag: Auto resolution mode (e.g., --auto or -a)
                        .flag(manager.flagBuilder("auto").withAliases("a").build())
                        .handler(context -> {
                            // 1. Gather Context & Arguments
                            Biome targetBiome = context.get("biome");
                            Entity sender = context.sender().entity().orThrow(
                                () -> new Error("Only entities can run this command."));
                            World world = sender.world();

                            // Fetch properties needed for the locator
                            int radius = context.get("radius");
                            boolean search3D = context.flags().hasFlag("3d");
                            boolean autoMode = context.flags().hasFlag("auto");

                            // 2. Determine Initial Step
                            // If Auto: Start at radius / 2 (very coarse check).
                            // If Manual: Use provided step.
                            int stepArg = context.get("step");
                            int currentStep = autoMode ? Integer.highestOneBit(radius - 1) : stepArg;

                            // Notify player
                            String modeMsg = autoMode ? " (Auto Mode)" : " (Step: " + currentStep + ")";
                            context.sender().sendMessage(
                                "Searching for " + targetBiome.getID() + " within " + radius + " blocks" + modeMsg + "...");

                            Maybe<Either<Vector3Int, Vector2Int>> result;

                            // 3. Execute Search Loop
                            while(true) {
                                result = BiomeLocator.search(
                                    world.getBiomeProvider(),
                                    world,
                                    sender.position().getFloorX(),
                                    sender.position().getFloorZ(),
                                    radius,
                                    currentStep,
                                    found -> found.equals(targetBiome), // Match specific biome instance
                                    search3D
                                );

                                // Exit Conditions:
                                // 1. Found a result
                                if(result.isJust()) {
                                    break;
                                }
                                // 2. Not in auto mode (only run once)
                                if(!autoMode) {
                                    break;
                                }
                                // 3. We just ran a search at step arg and failed (lowest resolution)
                                if(currentStep <= stepArg) {
                                    break;
                                }

                                // Reduce step for next iteration (Adaptive Search)
                                currentStep /= 2;
                                context.sender().sendMessage("No result found, refining search (Step: " + currentStep + ")...");
                            }

                            // 4. Handle Result
                            context.sender().sendMessage(collapse(result.map(location -> location.collect(
                                    left -> String.format("%d, %d, %d", left.getX(), left.getY(), left.getZ()),
                                    right -> String.format("%d, ~, %d", right.getX(), right.getZ())))
                                .map(coords -> "Found " + targetBiome.getID() + " at [" + coords + "]")
                                .toEither("Could not find " + targetBiome.getID() + " within " + radius + " blocks.")));
                        })
                        .permission("terra.locate.biome")
                );
            });
    }
}
