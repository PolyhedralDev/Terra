/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.seismic.math.floatingpoint.FloatingPointFunctions;
import com.dfsek.seismic.type.Rotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.seismic.type.vector.Vector2;


public class StructureFunction implements Function<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StructureFunction.class);
    private final Registry<Structure> registry;
    private final Returnable<String> id;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final Platform platform;
    private final List<Returnable<String>> rotations;

    public StructureFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> id,
                             List<Returnable<String>> rotations, Registry<Structure> registry, Position position, Platform platform) {
        this.registry = registry;
        this.id = id;
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
        this.platform = platform;
        this.rotations = rotations;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }

    @Override
    public Boolean apply(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;

        if(arguments.getRecursions() > platform.getTerraConfig().getMaxRecursion())
            throw new RuntimeException("Structure recursion too deep: " + arguments.getRecursions());

        Vector2 xz = Vector2.Mutable.of(x.apply(implementationArguments, scope).doubleValue(),
            z.apply(implementationArguments, scope).doubleValue()).rotate(arguments.getRotation());


        String app = id.apply(implementationArguments, scope);
        return registry.getByID(app).map(script -> {
            Rotation rotation1;
            String rotString = rotations.get(arguments.getRandom().nextInt(rotations.size())).apply(implementationArguments, scope);
            try {
                rotation1 = Rotation.valueOf(rotString);
            } catch(IllegalArgumentException e) {
                LOGGER.warn("Invalid rotation {}", rotString);
                return false;
            }

            if(script instanceof StructureScript structureScript) {
                return structureScript.generate(arguments.getOrigin(),
                    arguments.getWorld()
                        .buffer(FloatingPointFunctions.round(xz.getX()),
                            y.apply(implementationArguments, scope).intValue(),
                            FloatingPointFunctions.round(xz.getZ())),
                    arguments.getRandom(),
                    arguments.getRotation().rotate(rotation1), arguments.getRecursions() + 1);
            }
            return script.generate(arguments.getOrigin(),
                arguments.getWorld()
                    .buffer(FloatingPointFunctions.round(xz.getX()),
                        y.apply(implementationArguments, scope).intValue(),
                        FloatingPointFunctions.round(xz.getZ())),
                arguments.getRandom(),
                arguments.getRotation().rotate(rotation1));
        }).orElseGet(() -> {
            LOGGER.error("No such structure {}", app);
            return false;
        });
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
