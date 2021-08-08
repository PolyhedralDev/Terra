package com.dfsek.terra.addons.terrascript.script;

import com.dfsek.terra.addons.terrascript.api.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.api.StructureScript;
import com.dfsek.terra.addons.terrascript.api.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.buffer.DirectBuffer;
import com.dfsek.terra.addons.terrascript.api.buffer.StructureBuffer;
import com.dfsek.terra.addons.terrascript.api.exception.ParseException;
import com.dfsek.terra.addons.terrascript.parser.Parser;
import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class StructureScriptImpl implements StructureScript {
    private final Block block;
    private final String id;
    private final Cache<Vector3, StructureBuffer> cache;
    private final TerraPlugin main;
    private String tempID;

    public StructureScriptImpl(InputStream inputStream, TerraPlugin main, Registry<Structure> registry, Registry<LootTable> lootRegistry, Registry<FunctionBuilder<?>> functionRegistry) throws ParseException {
        Parser parser;
        try {
            parser = new Parser(IOUtils.toString(inputStream, Charset.defaultCharset()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        functionRegistry.forEach(parser::registerFunction); // Register registry functions.

        block = parser.parse();
        this.id = parser.getID();
        tempID = id;
        this.main = main;
        this.cache = CacheBuilder.newBuilder().maximumSize(main.getTerraConfig().getStructureCache()).build();
    }

    @Override
    @SuppressWarnings("try")
    public boolean generate(Vector3 location, World world, Chunk chunk, Random random, Rotation rotation) {
        try(ProfileFrame ignore = main.getProfiler().profile("terrascript_chunk:" + id)) {
            StructureBuffer buffer = computeBuffer(location, world, random, rotation);
            buffer.paste(location, chunk);
            return buffer.succeeded();
        }
    }

    @Override
    @SuppressWarnings("try")
    public boolean test(Vector3 location, World world, Random random, Rotation rotation) {
        try(ProfileFrame ignore = main.getProfiler().profile("terrascript_test:" + id)) {
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

    @Override
    @SuppressWarnings("try")
    public boolean generate(Buffer buffer, World world, Random random, Rotation rotation, int recursions) {
        try(ProfileFrame ignore = main.getProfiler().profile("terrascript_recursive:" + id)) {
            return applyBlock(new TerraImplementationArguments(buffer, rotation, random, world, recursions));
        }
    }

    @Override
    @SuppressWarnings("try")
    public boolean generate(Vector3 location, World world, Random random, Rotation rotation) {
        try(ProfileFrame ignore = main.getProfiler().profile("terrascript_direct:" + id)) {
            DirectBuffer buffer = new DirectBuffer(location, world);
            return applyBlock(new TerraImplementationArguments(buffer, rotation, random, world, 0));
        }
    }

    @Override
    public String getID() {
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
