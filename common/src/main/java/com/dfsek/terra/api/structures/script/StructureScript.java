package com.dfsek.terra.api.structures.script;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.Parser;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.script.builders.BlockFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.CheckFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.GetMarkFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.MarkFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.PullFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.RandomFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.RecursionsFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.StructureFunctionBuilder;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.structure.buffer.StructureBuffer;
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
    private final LinkedHashMap<Location, StructureBuffer> cache = new LinkedHashMap<Location, StructureBuffer>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Location, StructureBuffer> eldest) {
            return this.size() > 128;
        }
    };

    public StructureScript(InputStream inputStream, TerraPlugin main, ScriptRegistry registry) {
        Parser parser;
        try {
            parser = new Parser(IOUtils.toString(inputStream));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        parser.addFunction("block", new BlockFunctionBuilder(main))
                .addFunction("check", new CheckFunctionBuilder(main))
                .addFunction("structure", new StructureFunctionBuilder(registry, main))
                .addFunction("randomInt", new RandomFunctionBuilder())
                .addFunction("recursions", new RecursionsFunctionBuilder())
                .addFunction("setMark", new MarkFunctionBuilder())
                .addFunction("getMark", new GetMarkFunctionBuilder())
                .addFunction("pull", new PullFunctionBuilder(main));

        try {
            block = parser.parse();
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
        this.id = parser.getID();
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
        Block.ReturnLevel level = block.apply(buffer, rotation, random, 0);
        buffer.paste();
        return !level.equals(Block.ReturnLevel.FAIL);
    }

    public boolean execute(Location location, Chunk chunk, Random random, Rotation rotation) {
        StructureBuffer buffer = cache.computeIfAbsent(location, loc -> {
            System.out.println("Recalculating for (" + loc.getBlockX() + ", " + loc.getBlockZ() + "), chunk {" + chunk.getX() + ", " + chunk.getZ() + "}, cache size: " + cache.size());
            StructureBuffer buf = new StructureBuffer(loc);
            Block.ReturnLevel level = block.apply(buf, rotation, random, 0);
            buf.setSucceeded(!level.equals(Block.ReturnLevel.FAIL));
            return buf;
        });
        buffer.paste(chunk);
        return buffer.succeeded();
    }

    public void executeInBuffer(Buffer buffer, Random random, Rotation rotation, int recursions) {
        block.apply(buffer, rotation, random, recursions);
    }

    public String getId() {
        return id;
    }
}
