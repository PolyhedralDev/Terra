package com.dfsek.terra.api.structures.script;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.Parser;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.script.builders.BlockFunctionBuilder;
import com.dfsek.terra.api.structures.script.builders.CheckFunctionBuilder;
import com.dfsek.terra.api.structures.structure.Rotation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class StructureScript {
    private final Block block;
    private final String id;

    public StructureScript(InputStream inputStream, TerraPlugin main) {
        Parser parser;
        try {
            parser = new Parser(IOUtils.toString(inputStream));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        parser.addFunction("block", new BlockFunctionBuilder(main))
                .addFunction("check", new CheckFunctionBuilder(main));

        try {
            block = parser.parse();
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
        this.id = parser.getID();
    }

    public void execute(Location location, Rotation rotation) {
        block.apply(location, rotation);
    }

    public void execute(Location location, Chunk chunk, Rotation rotation) {
        block.apply(location, chunk, rotation);
    }

    public String getId() {
        return id;
    }
}
