package com.dfsek.terra.addons.terrascript.script;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.jafama.FastMath;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import com.dfsek.terra.addons.terrascript.buffer.DirectBuffer;
import com.dfsek.terra.addons.terrascript.buffer.StructureBuffer;
import com.dfsek.terra.addons.terrascript.parser.Parser;
import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.BinaryNumberFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.BiomeFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.BlockFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.CheckBlockFunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.builders.CheckFunctionBuilder;
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
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;


public class StructureScript implements Structure {
    private final Block block;
    private final String id;
    private final Cache<Vector3, StructureBuffer> cache;
    private final Platform platform;
    private String tempID;
    
    public StructureScript(InputStream inputStream, Platform platform, Registry<Structure> registry, Registry<LootTable> lootRegistry,
                           Registry<FunctionBuilder<?>> functionRegistry) throws ParseException {
        Parser parser;
        try {
            parser = new Parser(IOUtils.toString(inputStream, Charset.defaultCharset()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        
        functionRegistry.forEach(parser::registerFunction); // Register registry functions.
        
        parser
                .registerFunction("block", new BlockFunctionBuilder(platform))
                .registerFunction("debugBlock", new BlockFunctionBuilder(platform))
                .registerFunction("check", new CheckFunctionBuilder(platform))
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
                .registerFunction("originX", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getBuffer().getOrigin().getX(),
                                                                                Returnable.ReturnType.NUMBER))
                .registerFunction("originY", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getBuffer().getOrigin().getY(),
                                                                                Returnable.ReturnType.NUMBER))
                .registerFunction("originZ", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getBuffer().getOrigin().getZ(),
                                                                                Returnable.ReturnType.NUMBER))
                .registerFunction("rotation", new ZeroArgFunctionBuilder<>(arguments -> arguments.getRotation().toString(),
                                                                           Returnable.ReturnType.STRING))
                .registerFunction("rotationDegrees", new ZeroArgFunctionBuilder<>(arguments -> arguments.getRotation().getDegrees(),
                                                                                  Returnable.ReturnType.NUMBER))
                .registerFunction("print",
                                  new UnaryStringFunctionBuilder(string -> platform.getDebugLogger().info("[" + tempID + "] " + string)))
                .registerFunction("abs", new UnaryNumberFunctionBuilder(number -> FastMath.abs(number.doubleValue())))
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
        this.id = parser.getID();
        tempID = id;
        this.platform = platform;
        this.cache = CacheBuilder.newBuilder().maximumSize(platform.getTerraConfig().getStructureCache()).build();
    }
    
    @Override
    @SuppressWarnings("try")
    public boolean generate(Vector3 location, World world, Chunk chunk, Random random, Rotation rotation) {
        try(ProfileFrame ignore = platform.getProfiler().profile("terrascript_chunk:" + id)) {
            StructureBuffer buffer = computeBuffer(location, world, random, rotation);
            buffer.paste(location, chunk);
            return buffer.succeeded();
        }
    }
    
    @Override
    @SuppressWarnings("try")
    public boolean generate(Buffer buffer, World world, Random random, Rotation rotation, int recursions) {
        try(ProfileFrame ignore = platform.getProfiler().profile("terrascript_recursive:" + id)) {
            return applyBlock(new TerraImplementationArguments(buffer, rotation, random, world, recursions));
        }
    }
    
    @Override
    @SuppressWarnings("try")
    public boolean generate(Vector3 location, World world, Random random, Rotation rotation) {
        try(ProfileFrame ignore = platform.getProfiler().profile("terrascript_direct:" + id)) {
            DirectBuffer buffer = new DirectBuffer(location, world);
            return applyBlock(new TerraImplementationArguments(buffer, rotation, random, world, 0));
        }
    }
    
    @SuppressWarnings("try")
    public boolean test(Vector3 location, World world, Random random, Rotation rotation) {
        try(ProfileFrame ignore = platform.getProfiler().profile("terrascript_test:" + id)) {
            StructureBuffer buffer = computeBuffer(location, world, random, rotation);
            return buffer.succeeded();
        }
    }
    
    private StructureBuffer computeBuffer(Vector3 location, World world, Random random, Rotation rotation) {
        try {
            return cache.get(location, () -> {
                StructureBuffer buf = new StructureBuffer(location);
                buf.setSucceeded(applyBlock(new TerraImplementationArguments(buf, rotation, random, world, 0)));
                return buf;
            });
        } catch(ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    
    private boolean applyBlock(TerraImplementationArguments arguments) {
        try {
            return block.apply(arguments).getLevel() != Block.ReturnLevel.FAIL;
        } catch(RuntimeException e) {
            platform.logger().severe("Failed to generate structure at " + arguments.getBuffer().getOrigin() + ": " + e.getMessage());
            platform.getDebugLogger().stack(e);
            return false;
        }
    }
    
    @Override
    public String getID() {
        return id;
    }
}
