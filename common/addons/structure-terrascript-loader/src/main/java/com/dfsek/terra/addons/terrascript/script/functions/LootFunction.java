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

import java.util.Map;

import com.dfsek.terra.addons.terrascript.buffer.items.BufferedLootApplication;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class LootFunction implements Function<Void> {
    private final Registry<LootTable> registry;
    private final Returnable<String> data;
    private final Returnable<Number> x, y, z;
    private final Position position;
    private final Platform platform;
    private final StructureScript script;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LootFunction.class);
    
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
    public Void apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = new Vector2(x.apply(implementationArguments, variableMap).doubleValue(),
                                 z.apply(implementationArguments, variableMap).doubleValue());
        
        RotationUtil.rotateVector(xz, arguments.getRotation());
        
        String id = data.apply(implementationArguments, variableMap);
        registry.get(id).ifPresentOrElse(table -> arguments.getBuffer().addItem(new BufferedLootApplication(table, platform, script),
                                                                                new Vector3(FastMath.roundToInt(xz.getX()),
                                                                                            y.apply(implementationArguments, variableMap)
                                                                                             .intValue(),
                                                                                            FastMath.roundToInt(xz.getZ()))),
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
