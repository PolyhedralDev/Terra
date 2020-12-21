package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.api.structures.world.LandCheck;
import com.dfsek.terra.api.structures.world.OceanCheck;

public class CheckFunction implements Function<String> {
    private final TerraPlugin main;
    private final Returnable<Integer> x, y, z;
    private final Position position;

    public CheckFunction(TerraPlugin main, Returnable<Integer> x, Returnable<Integer> y, Returnable<Integer> z, Position position) {
        this.main = main;
        this.x = x;
        this.y = y;
        this.z = z;
        this.position = position;
    }

    @Override
    public String name() {
        return "check";
    }

    private Vector3 getVector(Location location, Chunk chunk) {
        return chunk == null ? new Vector3(x.apply(location) + location.getBlockX(), y.apply(location) + location.getBlockY(), z.apply(location) + location.getBlockZ())
                : new Vector3(x.apply(location, chunk) + location.getBlockX(), y.apply(location, chunk) + location.getBlockY(), z.apply(location, chunk) + location.getBlockZ());
    }

    @Override
    public String apply(Location location) {
        return apply(getVector(location, null), location.getWorld());
    }

    private String apply(Vector3 vector, World world) {
        if(new LandCheck(world, main).check(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()))
            return "LAND";
        if(new OceanCheck(world, main).check(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()))
            return "OCEAN";
        return "AIR";
    }

    @Override
    public String apply(Location location, Chunk chunk) {
        return apply(getVector(location, chunk), location.getWorld());
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.STRING;
    }
}
