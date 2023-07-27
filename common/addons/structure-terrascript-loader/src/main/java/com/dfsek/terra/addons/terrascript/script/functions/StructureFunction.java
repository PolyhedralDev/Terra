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

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;


public class StructureFunction implements Function<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StructureFunction.class);
    private final Registry<Structure> registry;
    private final Expression<String> id;
    private final Expression<Number> x, y, z;
    private final SourcePosition position;
    private final Platform platform;
    private final List<Expression<String>> rotations;
    
    public StructureFunction(Expression<Number> x, Expression<Number> y, Expression<Number> z, Expression<String> id,
                             List<Expression<String>> rotations, Registry<Structure> registry, SourcePosition position, Platform platform) {
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
    public Boolean evaluate(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        
        if(arguments.getRecursions() > platform.getTerraConfig().getMaxRecursion())
            throw new RuntimeException("Structure recursion too deep: " + arguments.getRecursions());
        
        Vector2 xz = RotationUtil.rotateVector(Vector2.of(x.evaluate(implementationArguments, scope).doubleValue(),
                                                          z.evaluate(implementationArguments, scope).doubleValue()),
                                               arguments.getRotation());
        
        
        String app = id.evaluate(implementationArguments, scope);
        return registry.getByID(app).map(script -> {
            Rotation rotation1;
            String rotString = rotations.get(arguments.getRandom().nextInt(rotations.size())).evaluate(implementationArguments, scope);
            try {
                rotation1 = Rotation.valueOf(rotString);
            } catch(IllegalArgumentException e) {
                LOGGER.warn("Invalid rotation {}", rotString);
                return false;
            }
            
            if(script instanceof StructureScript structureScript) {
                return structureScript.generate(arguments.getOrigin(),
                                                arguments.getWorld()
                                                         .buffer(FastMath.roundToInt(xz.getX()),
                                                                 y.evaluate(implementationArguments, scope).intValue(),
                                                                 FastMath.roundToInt(xz.getZ())),
                                                arguments.getRandom(),
                                                arguments.getRotation().rotate(rotation1), arguments.getRecursions() + 1);
            }
            return script.generate(arguments.getOrigin(),
                                   arguments.getWorld()
                                            .buffer(FastMath.roundToInt(xz.getX()),
                                                    y.evaluate(implementationArguments, scope).intValue(),
                                                    FastMath.roundToInt(xz.getZ())),
                                   arguments.getRandom(),
                                   arguments.getRotation().rotate(rotation1));
        }).orElseGet(() -> {
            LOGGER.error("No such structure {}", app);
            return false;
        });
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
