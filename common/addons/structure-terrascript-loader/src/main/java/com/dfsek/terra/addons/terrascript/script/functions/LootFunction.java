/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.seismic.math.floatingpoint.FloatingPointFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.event.events.world.generation.LootPopulateEvent;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.seismic.type.vector.Vector2;
import com.dfsek.seismic.type.vector.Vector3;


public class LootFunction implements Function<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LootFunction.class);
    private final Registry<LootTable> registry;
    private final Returnable<String> data;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final Platform platform;
    private final StructureScript script;

    public LootFunction(Registry<LootTable> registry, Returnable<Number> x, Returnable<Number> y, Returnable<Number> z,
                        Returnable<String> data, Platform platform, Position position, StructureScript script) {
        this.registry = registry;
        this.position = position;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
        this.platform = platform;
        this.script = script;
    }

    @Override
    public Void apply(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = Vector2.Mutable.of(x.apply(implementationArguments, scope).doubleValue(),
                z.apply(implementationArguments, scope).doubleValue()).rotate(arguments.getRotation());


        String id = data.apply(implementationArguments, scope);


        registry.get(RegistryKey.parse(id))
            .ifPresentOrElse(table -> {
                    Vector3 apply = Vector3.of(FloatingPointFunctions.round(xz.getX()),
                        y.apply(implementationArguments, scope)
                            .intValue(),
                        FloatingPointFunctions.round(xz.getZ())).mutable().add(arguments.getOrigin().toFloat()).immutable();

                    try {
                        BlockEntity data = arguments.getWorld().getBlockEntity(apply);
                        if(!(data instanceof Container container)) {
                            LOGGER.error("Failed to place loot at {}; block {} is not a container",
                                apply, data);
                            return;
                        }

                        LootPopulateEvent event = new LootPopulateEvent(container, table,
                            arguments.getWorld().getPack(), script);
                        platform.getEventManager().callEvent(event);
                        if(event.isCancelled()) return;

                        event.getTable().fillInventory(container.getInventory(),
                            RandomGeneratorFactory.<RandomGenerator.SplittableGenerator>of(
                                "Xoroshiro128PlusPlus").create(apply.hashCode()));
                        data.update(false);
                    } catch(Exception e) {
                        LOGGER.error("Could not apply loot at {}", apply, e);
                        e.printStackTrace();
                    }
                },
                () -> LOGGER.error("No such loot table {}", id));
        return null;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
}
