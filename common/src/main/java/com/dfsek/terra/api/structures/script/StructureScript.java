package com.dfsek.terra.api.structures.script;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.Parser;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.script.builders.BinaryNumberFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.BiomeFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.BlockFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.CheckFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.EntityFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.GetMarkFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.LootFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.MarkFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.PullFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.RandomFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.RecursionsFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.StructureFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.UnaryNumberFunctionBuilder;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.structure.buffer.StructureBuffer;
import com.dfsek.terra.generation.math.SamplerCache;
import com.dfsek.terra.registry.LootRegistry;
import com.dfsek.terra.registry.ScriptRegistry;
import net.jafama.FastMath;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class StructureScript {
    private final Block block;
    private final String id;
    private final LinkedHashMap<Location, StructureBuffer> cache;

    public StructureScript(InputStream inputStream, TerraPlugin main, ScriptRegistry registry, LootRegistry lootRegistry, SamplerCache cache) throws ParseException {
        Parser parser;
        try {
            parser = new Parser(IOUtils.toString(inputStream));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        parser.registerFunction("block", new BlockFunctionBuilder(main))
                .registerFunction("check", new CheckFunctionBuilder(main, cache))
                .registerFunction("structure", new StructureFunctionBuilder(registry, main))
                .registerFunction("randomInt", new RandomFunctionBuilder())
                .registerFunction("recursions", new RecursionsFunctionBuilder())
                .registerFunction("setMark", new MarkFunctionBuilder())
                .registerFunction("getMark", new GetMarkFunctionBuilder())
                .registerFunction("pull", new PullFunctionBuilder(main))
                .registerFunction("loot", new LootFunctionBuilder(main, lootRegistry))
                .registerFunction("entity", new EntityFunctionBuilder(main))
                .registerFunction("biome", new BiomeFunctionBuilder(main))
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
        this.cache = new LinkedHashMap<Location, StructureBuffer>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Location, StructureBuffer> eldest) {
                return this.size() > main.getTerraConfig().getStructureCache();
            }
        };
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
        Block.ReturnInfo<?> level = block.apply(new TerraImplementationArguments(buffer, rotation, random, 0));
        buffer.paste();
        return !level.getLevel().equals(Block.ReturnLevel.FAIL);
    }

    public boolean execute(Location location, Chunk chunk, Random random, Rotation rotation) {
        StructureBuffer buffer = cache.computeIfAbsent(location, loc -> {
            StructureBuffer buf = new StructureBuffer(loc);
            Block.ReturnInfo<?> level = block.apply(new TerraImplementationArguments(buf, rotation, random, 0));
            buf.setSucceeded(!level.getLevel().equals(Block.ReturnLevel.FAIL));
            return buf;
        });
        buffer.paste(chunk);
        return buffer.succeeded();
    }

    public boolean test(Location location, Random random, Rotation rotation) {
        StructureBuffer buffer = new StructureBuffer(location);
        return !block.apply(new TerraImplementationArguments(buffer, rotation, random, 0)).getLevel().equals(Block.ReturnLevel.FAIL);
    }

    public boolean executeInBuffer(Buffer buffer, Random random, Rotation rotation, int recursions) {
        return !block.apply(new TerraImplementationArguments(buffer, rotation, random, recursions)).getLevel().equals(Block.ReturnLevel.FAIL);
    }

    public String getId() {
        return id;
    }
}
