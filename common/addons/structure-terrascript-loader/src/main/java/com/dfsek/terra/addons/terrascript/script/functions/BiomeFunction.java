/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.seismic.math.floatingpoint.FloatingPointFunctions;
import com.dfsek.seismic.type.vector.Vector2;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.seismic.type.vector.Vector3;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class BiomeFunction implements Function<String> {
    private final Returnable<Number> x, y, z;
    private final Position position;


    public BiomeFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Position position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.position = position;
    }


    @Override
    public String apply(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;

        Vector2 xz = Vector2.Mutable.of(x.apply(implementationArguments, scope).doubleValue(),
                z.apply(implementationArguments, scope).doubleValue()).rotate(arguments.getRotation());



        BiomeProvider grid = arguments.getWorld().getBiomeProvider();

        return grid.getBiome(arguments.getOrigin().toFloat()
            .mutable()
            .add(Vector3.of(FloatingPointFunctions.round(xz.getX()),
                y.apply(implementationArguments, scope).intValue(),
                FloatingPointFunctions.round(xz.getZ()))).immutable(), arguments.getWorld().getSeed()).getID();
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.STRING;
    }
}
