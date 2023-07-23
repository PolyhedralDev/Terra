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

import java.util.Random;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.StructureScript;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.event.events.world.generation.LootPopulateEvent;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;


public class LootFunction implements Function<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LootFunction.class);
    private final Registry<LootTable> registry;
    private final Expression<String> data;
    private final Expression<Number> x, y, z;
    private final SourcePosition position;
    private final Platform platform;
    private final StructureScript script;
    
    public LootFunction(Registry<LootTable> registry, Expression<Number> x, Expression<Number> y, Expression<Number> z,
                        Expression<String> data, Platform platform, SourcePosition position, StructureScript script) {
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
    public Void evaluate(ImplementationArguments implementationArguments, Scope scope) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        Vector2 xz = RotationUtil.rotateVector(Vector2.of(x.evaluate(implementationArguments, scope).doubleValue(),
                                                          z.evaluate(implementationArguments, scope).doubleValue()),
                                               arguments.getRotation());
        
        
        String id = data.evaluate(implementationArguments, scope);
        
        
        registry.get(RegistryKey.parse(id))
                .ifPresentOrElse(table -> {
                                     Vector3 apply = Vector3.of(FastMath.roundToInt(xz.getX()),
                                                                y.evaluate(implementationArguments, scope)
                                                                 .intValue(),
                                                                FastMath.roundToInt(xz.getZ())).mutable().add(arguments.getOrigin()).immutable();
            
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
                                                                        new Random(apply.hashCode()));
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
    public SourcePosition getPosition() {
        return position;
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
}
