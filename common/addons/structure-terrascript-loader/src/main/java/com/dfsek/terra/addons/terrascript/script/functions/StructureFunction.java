/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import net.jafama.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.terrascript.buffer.IntermediateBuffer;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


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
    public Boolean apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        
        if(arguments.getRecursions() > platform.getTerraConfig().getMaxRecursion())
            throw new RuntimeException("Structure recursion too deep: " + arguments.getRecursions());
        
        Vector2 xz = new Vector2(x.apply(implementationArguments, variableMap).doubleValue(),
                                 z.apply(implementationArguments, variableMap).doubleValue());
        
        RotationUtil.rotateVector(xz, arguments.getRotation());
        
        String app = id.apply(implementationArguments, variableMap);
        return registry.get(app).map(script -> {
            Rotation rotation1;
            String rotString = rotations.get(arguments.getRandom().nextInt(rotations.size())).apply(implementationArguments, variableMap);
            try {
                rotation1 = Rotation.valueOf(rotString);
            } catch(IllegalArgumentException e) {
                LOGGER.warn("Invalid rotation {}", rotString);
                return null;
            }

            Vector3 offset = new Vector3(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).doubleValue(),
                                         FastMath.roundToInt(xz.getZ()));

            return script.generate(new IntermediateBuffer(arguments.getBuffer(), offset), arguments.getWorld(), arguments.getRandom(),
                                   arguments.getRotation().rotate(rotation1), arguments.getRecursions() + 1);
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
