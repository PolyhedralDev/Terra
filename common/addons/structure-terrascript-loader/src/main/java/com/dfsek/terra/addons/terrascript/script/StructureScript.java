/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script;

import net.jafama.FastMath;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

import com.dfsek.terra.addons.terrascript.parser.Parser;
import com.dfsek.terra.addons.terrascript.parser.lang.Executable;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.BinaryNumberFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.BiomeFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.BlockFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.CheckBlockFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.EntityFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.GetMarkFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.LootFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.PullFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.RandomFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.RecursionsFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.SetMarkFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.StateFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.StructureFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.UnaryBooleanFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.UnaryNumberFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.UnaryStringFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.ZeroArgFunctionBuilder;
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
    private final Executable block;
    private final RegistryKey id;
    
    private final String profile;
    private final Platform platform;
    
    @SuppressWarnings("rawtypes")
    public StructureScript(InputStream inputStream, RegistryKey id, Platform platform, Registry<Structure> registry,
                           Registry<LootTable> lootRegistry,
                           Registry<FunctionBuilder> functionRegistry) {
        Parser parser;
        try {
            parser = new Parser(IOUtils.toString(inputStream, Charset.defaultCharset()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        this.id = id;
        this.profile = "terrascript_direct:" + id;
        
        //noinspection unchecked
        functionRegistry.forEach((key, function) -> parser.registerFunction(key.getID(), function)); // Register registry functions.
        
        parser
                .registerFunction("block", new BlockFunctionBuilder(platform))
                .registerFunction("debugBlock", new BlockFunctionBuilder(platform))
                .registerFunction("structure", new StructureFunctionBuilder(registry, platform))
                .registerFunction("randomInt", new RandomFunctionBuilder())
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
                                                                                Returnable.ReturnType.NUMBER))
                .registerFunction("originY", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getOrigin().getY(),
                                                                                Returnable.ReturnType.NUMBER))
                .registerFunction("originZ", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getOrigin().getZ(),
                                                                                Returnable.ReturnType.NUMBER))
                .registerFunction("rotation", new ZeroArgFunctionBuilder<>(arguments -> arguments.getRotation().toString(),
                                                                           Returnable.ReturnType.STRING))
                .registerFunction("rotationDegrees", new ZeroArgFunctionBuilder<>(arguments -> arguments.getRotation().getDegrees(),
                                                                                  Returnable.ReturnType.NUMBER))
                .registerFunction("print",
                                  new UnaryStringFunctionBuilder(string -> LOGGER.info("[TerraScript:{}] {}", id, string)))
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
        
        if(!platform.getTerraConfig().isDebugScript()) {
            parser.ignoreFunction("debugBlock");
        }
        
        block = parser.parse();
        this.platform = platform;
    }
    
    @Override
    @SuppressWarnings("try")
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation) {
        platform.getProfiler().push(profile);
        boolean result = applyBlock(new TerraImplementationArguments(location, rotation, random, world, 0));
        platform.getProfiler().pop(profile);
        return result;
    }
    
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation, int recursions) {
        platform.getProfiler().push(profile);
        boolean result = applyBlock(new TerraImplementationArguments(location, rotation, random, world, recursions));
        platform.getProfiler().pop(profile);
        return result;
    }
    
    private boolean applyBlock(TerraImplementationArguments arguments) {
        try {
            return block.execute(arguments);
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
