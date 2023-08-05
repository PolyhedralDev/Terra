/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script;

import net.jafama.FastMath;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.Parser;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Executable;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope.ScopeBuilder;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.BinaryNumberFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.BiomeFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.BlockFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.CheckBlockFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.EntityFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.GetMarkFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.LootFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.PullFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.RandomFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.RecursionsFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.SetMarkFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.StateFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.StructureFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.UnaryBooleanFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.UnaryNumberFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.UnaryStringFunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.builders.ZeroArgFunctionBuilder;
import com.dfsek.terra.addons.terrascript.lexer.Lexer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;


public class StructureScript implements Structure, Keyed<StructureScript> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StructureScript.class);
    private final Executable executable;
    private final RegistryKey id;
    
    private final String profile;
    private final Platform platform;
    
    @SuppressWarnings("rawtypes")
    public StructureScript(InputStream source, RegistryKey id, Platform platform, Registry<Structure> structureRegistry,
                           Registry<LootTable> lootRegistry,
                           Registry<FunctionBuilder> functionRegistry) {
        Lexer lexer;
        try {
            lexer = new Lexer(IOUtils.toString(source, Charset.defaultCharset()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        Parser parser = new Parser(lexer);
        this.id = id;
        this.profile = "terrascript_direct:" + id;
        
        ScopeBuilder scope = new ScopeBuilder();
        
        functionRegistry.forEach((key, function) -> scope.registerFunction(key.getID(), function)); // Register registry functions.
        
        scope
                .registerFunction("block", new BlockFunctionBuilder(platform))
                .registerFunction("debugBlock", new BlockFunctionBuilder(platform))
                .registerFunction("structure", new StructureFunctionBuilder(structureRegistry, platform))
                .registerFunction("recursions", new RecursionsFunctionBuilder())
                .registerFunction("setMark", new SetMarkFunctionBuilder())
                .registerFunction("getMark", new GetMarkFunctionBuilder())
                .registerFunction("pull", new PullFunctionBuilder(platform))
                .registerFunction("loot", new LootFunctionBuilder(platform, lootRegistry, this))
                .registerFunction("entity", new EntityFunctionBuilder(platform))
                .registerFunction("getBiome", new BiomeFunctionBuilder(platform))
                .registerFunction("getBlock", new CheckBlockFunctionBuilder())
                .registerFunction("state", new StateFunctionBuilder(platform))
                .registerFunction("setWaterlog", new UnaryBooleanFunctionBuilder((waterlog, args) -> args.setWaterlog(waterlog)))
                .registerFunction("originX", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getOrigin().getX(),
                                                                                Type.NUMBER))
                .registerFunction("originY", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getOrigin().getY(),
                                                                                Type.NUMBER))
                .registerFunction("originZ", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getOrigin().getZ(),
                                                                                Type.NUMBER))
                .registerFunction("rotation", new ZeroArgFunctionBuilder<>(arguments -> arguments.getRotation().toString(),
                                                                           Type.STRING))
                .registerFunction("rotationDegrees", new ZeroArgFunctionBuilder<>(arguments -> arguments.getRotation().getDegrees(),
                                                                                  Type.NUMBER))
                
                
                .registerFunction("print",
                                  new UnaryStringFunctionBuilder(string -> LOGGER.info("[TerraScript:{}] {}", id, string)))
                .registerFunction("randomInt", new RandomFunctionBuilder())
                .registerFunction("abs", new UnaryNumberFunctionBuilder(number -> FastMath.abs(number.doubleValue())))
                .registerFunction("pow2", new UnaryNumberFunctionBuilder(number -> FastMath.pow2(number.doubleValue())))
                .registerFunction("pow", new BinaryNumberFunctionBuilder(
                        (number, number2) -> FastMath.pow(number.doubleValue(), number2.doubleValue())))
                .registerFunction("sqrt", new UnaryNumberFunctionBuilder(number -> FastMath.sqrt(number.doubleValue())))
                .registerFunction("floor", new UnaryNumberFunctionBuilder(number -> FastMath.floor(number.doubleValue())))
                .registerFunction("ceil", new UnaryNumberFunctionBuilder(number -> FastMath.ceil(number.doubleValue())))
                .registerFunction("log", new UnaryNumberFunctionBuilder(number -> FastMath.log(number.doubleValue())))
                .registerFunction("round", new UnaryNumberFunctionBuilder(number -> FastMath.round(number.doubleValue())))
                .registerFunction("sin", new UnaryNumberFunctionBuilder(number -> FastMath.sin(number.doubleValue())))
                .registerFunction("cos", new UnaryNumberFunctionBuilder(number -> FastMath.cos(number.doubleValue())))
                .registerFunction("tan", new UnaryNumberFunctionBuilder(number -> FastMath.tan(number.doubleValue())))
                .registerFunction("asin", new UnaryNumberFunctionBuilder(number -> FastMath.asin(number.doubleValue())))
                .registerFunction("acos", new UnaryNumberFunctionBuilder(number -> FastMath.acos(number.doubleValue())))
                .registerFunction("atan", new UnaryNumberFunctionBuilder(number -> FastMath.atan(number.doubleValue())))
                .registerFunction("max", new BinaryNumberFunctionBuilder(
                        (number, number2) -> FastMath.max(number.doubleValue(), number2.doubleValue())))
                .registerFunction("min", new BinaryNumberFunctionBuilder(
                        (number, number2) -> FastMath.min(number.doubleValue(), number2.doubleValue())));
        
        executable = parser.parse(scope);
        this.platform = platform;
    }
    
    @Override
    @SuppressWarnings("try")
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation) {
        platform.getProfiler().push(profile);
        boolean result = execute(new TerraImplementationArguments(location, rotation, random, world, 0));
        platform.getProfiler().pop(profile);
        return result;
    }
    
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation, int recursions) {
        platform.getProfiler().push(profile);
        boolean result = execute(new TerraImplementationArguments(location, rotation, random, world, recursions));
        platform.getProfiler().pop(profile);
        return result;
    }
    
    private boolean execute(TerraImplementationArguments arguments) {
        try {
            return executable.execute(arguments);
        } catch(RuntimeException e) {
            LOGGER.error("Failed to generate structure at {}", arguments.getOrigin(), e);
            return false;
        }
    }
    
    @Override
    public RegistryKey getRegistryKey() {
        return id;
    }
}
