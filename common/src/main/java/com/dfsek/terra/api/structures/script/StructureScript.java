package com.dfsek.terra.api.structures.script;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structures.loot.LootTable;
import com.dfsek.terra.api.structures.parser.Parser;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.BinaryNumberFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.BiomeFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.BlockFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.CheckBlockFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.CheckFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.EntityFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.GetMarkFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.LootFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.PullFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.RandomFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.RecursionsFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.SetMarkFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.StateFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.StructureFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.UnaryBooleanFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.UnaryNumberFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.UnaryStringFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.ZeroArgFunctionBuilder;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.structure.buffer.DirectBuffer;
import com.dfsek.terra.api.structures.structure.buffer.StructureBuffer;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.jafama.FastMath;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class StructureScript {
    private final Block block;
    private final String id;
    private final Cache<Location, StructureBuffer> cache;
    private final TerraPlugin main;
    private String tempID;

    public StructureScript(InputStream inputStream, TerraPlugin main, Registry<StructureScript> registry, Registry<LootTable> lootRegistry, Registry<FunctionBuilder<?>> functionRegistry) throws ParseException {
        Parser parser;
        try {
            parser = new Parser(IOUtils.toString(inputStream));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        functionRegistry.forEach(parser::registerFunction); // Register registry functions.

        parser.registerFunction("block", new BlockFunctionBuilder(main, false))
                .registerFunction("dynamicBlock", new BlockFunctionBuilder(main, true))
                .registerFunction("check", new CheckFunctionBuilder(main))
                .registerFunction("structure", new StructureFunctionBuilder(registry, main))
                .registerFunction("randomInt", new RandomFunctionBuilder())
                .registerFunction("recursions", new RecursionsFunctionBuilder())
                .registerFunction("setMark", new SetMarkFunctionBuilder())
                .registerFunction("getMark", new GetMarkFunctionBuilder())
                .registerFunction("pull", new PullFunctionBuilder(main))
                .registerFunction("loot", new LootFunctionBuilder(main, lootRegistry, this))
                .registerFunction("entity", new EntityFunctionBuilder(main))
                .registerFunction("getBiome", new BiomeFunctionBuilder(main))
                .registerFunction("getBlock", new CheckBlockFunctionBuilder())
                .registerFunction("state", new StateFunctionBuilder(main))
                .registerFunction("setWaterlog", new UnaryBooleanFunctionBuilder((waterlog, args) -> args.setWaterlog(waterlog)))
                .registerFunction("originX", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getBuffer().getOrigin().getX(), Returnable.ReturnType.NUMBER))
                .registerFunction("originY", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getBuffer().getOrigin().getY(), Returnable.ReturnType.NUMBER))
                .registerFunction("originZ", new ZeroArgFunctionBuilder<Number>(arguments -> arguments.getBuffer().getOrigin().getZ(), Returnable.ReturnType.NUMBER))
                .registerFunction("rotation", new ZeroArgFunctionBuilder<>(arguments -> arguments.getRotation().toString(), Returnable.ReturnType.STRING))
                .registerFunction("rotationDegrees", new ZeroArgFunctionBuilder<>(arguments -> arguments.getRotation().getDegrees(), Returnable.ReturnType.NUMBER))
                .registerFunction("print", new UnaryStringFunctionBuilder(string -> main.getDebugLogger().info("[" + tempID + "] " + string)))
                .registerFunction("abs", new UnaryNumberFunctionBuilder(number -> FastMath.abs(number.doubleValue())))
                .registerFunction("pow", new BinaryNumberFunctionBuilder((number, number2) -> FastMath.pow(number.doubleValue(), number2.doubleValue())))
                .registerFunction("sqrt", new UnaryNumberFunctionBuilder(number -> FastMath.sqrt(number.doubleValue())))
                .registerFunction("floor", new UnaryNumberFunctionBuilder(number -> FastMath.floor(number.doubleValue())))
                .registerFunction("ceil", new UnaryNumberFunctionBuilder(number -> FastMath.ceil(number.doubleValue())))
                .registerFunction("log", new UnaryNumberFunctionBuilder(number -> FastMath.log(number.doubleValue())))
                .registerFunction("round", new UnaryNumberFunctionBuilder(number -> FastMath.round(number.doubleValue())))
                .registerFunction("max", new BinaryNumberFunctionBuilder((number, number2) -> FastMath.max(number.doubleValue(), number2.doubleValue())))
                .registerFunction("min", new BinaryNumberFunctionBuilder((number, number2) -> FastMath.min(number.doubleValue(), number2.doubleValue())));

        block = parser.parse();
        this.id = parser.getID();
        tempID = id;
        this.main = main;
        this.cache = CacheBuilder.newBuilder().maximumSize(main.getTerraConfig().getStructureCache()).build();
    }

    /**
     * Paste the structure at a location
     *
     * @param location Location to paste structure
     * @param rotation Rotation of structure
     * @return Whether generation was successful
     */
    public boolean execute(Location location, Random random, Rotation rotation) {
        StructureBuffer buffer = new StructureBuffer(location);
        boolean level = applyBlock(new TerraImplementationArguments(buffer, rotation, random, 0));
        buffer.paste();
        return level;
    }

    public boolean execute(Location location, Chunk chunk, Random random, Rotation rotation) {
        StructureBuffer buffer = computeBuffer(location, random, rotation);
        buffer.paste(chunk);
        return buffer.succeeded();
    }

    public boolean test(Location location, Random random, Rotation rotation) {
        StructureBuffer buffer = computeBuffer(location, random, rotation);
        return buffer.succeeded();
    }

    private StructureBuffer computeBuffer(Location location, Random random, Rotation rotation) {
        try {
            return cache.get(location, () -> {
                StructureBuffer buf = new StructureBuffer(location);
                buf.setSucceeded(applyBlock(new TerraImplementationArguments(buf, rotation, random, 0)));
                return buf;
            });
        } catch(ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean executeInBuffer(Buffer buffer, Random random, Rotation rotation, int recursions) {
        return applyBlock(new TerraImplementationArguments(buffer, rotation, random, recursions));
    }

    public boolean executeDirect(Location location, Random random, Rotation rotation) {
        DirectBuffer buffer = new DirectBuffer(location);
        return applyBlock(new TerraImplementationArguments(buffer, rotation, random, 0));
    }

    public String getId() {
        return id;
    }

    private boolean applyBlock(TerraImplementationArguments arguments) {
        try {
            return block.apply(arguments).getLevel() != Block.ReturnLevel.FAIL;
        } catch(RuntimeException e) {
            main.logger().severe("Failed to generate structure at " + arguments.getBuffer().getOrigin() + ": " + e.getMessage());
            main.getDebugLogger().stack(e);
            return false;
        }
    }
}
