package com.dfsek.terra.api.structures.script;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.parser.Parser;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.script.builders.BlockFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.CheckFunctionBuilder;
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

public class StructureScript {
    private final Block block;
    private final String id;

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
                .addFunction("recursions", new RecursionsFunctionBuilder());

        try {
            block = parser.parse();
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
        this.id = parser.getID();
    }

    public void execute(Location location, Rotation rotation) {
        StructureBuffer buffer = new StructureBuffer(location);
        block.apply(buffer, rotation, 0);
        buffer.paste();
    }

    public void executeInBuffer(Buffer buffer, Rotation rotation, int recursions) {
        block.apply(buffer, rotation, recursions);
    }

    public String getId() {
        return id;
    }
}
