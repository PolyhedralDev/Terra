package com.dfsek.terra.addons.commands.locate;


import com.dfsek.seismic.type.vector.Vector2Int;
import com.dfsek.seismic.type.vector.Vector3Int;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.parser.standard.IntegerParser;

import java.util.Optional;

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
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.Biome;


public class LocateCommandAddon implements AddonInitializer {
    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;

    private static Registry<Biome> getBiomeRegistry(CommandContext<CommandSender> sender) {
        return sender.sender().getEntity().orElseThrow().world().getPack().getRegistry(Biome.class);
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
                            Entity sender = context.sender().getEntity().orElseThrow(
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

                            Optional<Either<Vector3Int, Vector2Int>> result;

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
                                if(result.isPresent()) {
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
                            if(result.isPresent()) {
                                Either<Vector3Int, Vector2Int> location = result.get();
                                String coords;

                                if(location.hasLeft()) { // 3D Result
                                    Vector3Int vec = location.getLeft().get();
                                    coords = String.format("%d, %d, %d", vec.getX(), vec.getY(), vec.getZ());
                                } else { // 2D Result
                                    Vector2Int vec = location.getRight().get();
                                    coords = String.format("%d, ~, %d", vec.getX(), vec.getZ());
                                }

                                context.sender().sendMessage("Found " + targetBiome.getID() + " at [" + coords + "]");
                            } else {
                                context.sender().sendMessage("Could not find " + targetBiome.getID() + " within " + radius + " blocks.");
                            }
                        })
                        .permission("terra.locate.biome")
                );
            });
    }
}
