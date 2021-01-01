package com.dfsek.terra.api.structures.script;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.Parser;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
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
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.structure.buffer.StructureBuffer;
import com.dfsek.terra.api.structures.world.CheckCache;
import com.dfsek.terra.registry.LootRegistry;
import com.dfsek.terra.registry.ScriptRegistry;
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

    public StructureScript(InputStream inputStream, TerraPlugin main, ScriptRegistry registry, LootRegistry lootRegistry, CheckCache cache) {
        Parser parser;
        try {
            parser = new Parser(IOUtils.toString(inputStream));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        parser.addFunction("block", new BlockFunctionBuilder(main))
                .addFunction("check", new CheckFunctionBuilder(main, cache))
                .addFunction("structure", new StructureFunctionBuilder(registry, main))
                .addFunction("randomInt", new RandomFunctionBuilder())
                .addFunction("recursions", new RecursionsFunctionBuilder())
                .addFunction("setMark", new MarkFunctionBuilder())
                .addFunction("getMark", new GetMarkFunctionBuilder())
                .addFunction("pull", new PullFunctionBuilder(main))
                .addFunction("loot", new LootFunctionBuilder(main, lootRegistry))
                .addFunction("entity", new EntityFunctionBuilder(main));

        try {
            block = parser.parse();
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
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
        Block.ReturnInfo<?> level = block.apply(buffer, rotation, random, 0);
        buffer.paste();
        return !level.getLevel().equals(Block.ReturnLevel.FAIL);
    }

    public boolean execute(Location location, Chunk chunk, Random random, Rotation rotation) {
        StructureBuffer buffer = cache.computeIfAbsent(location, loc -> {
            StructureBuffer buf = new StructureBuffer(loc);
            Block.ReturnInfo<?> level = block.apply(buf, rotation, random, 0);
            buf.setSucceeded(!level.getLevel().equals(Block.ReturnLevel.FAIL));
            return buf;
        });
        buffer.paste(chunk);
        return buffer.succeeded();
    }

    public boolean test(Location location, Random random, Rotation rotation) {
        StructureBuffer buffer = new StructureBuffer(location);
        return !block.apply(buffer, rotation, random, 0).equals(Block.ReturnLevel.FAIL);
    }

    public boolean executeInBuffer(Buffer buffer, Random random, Rotation rotation, int recursions) {
        return !block.apply(buffer, rotation, random, recursions).equals(Block.ReturnLevel.FAIL);
    }

    public String getId() {
        return id;
    }
}
