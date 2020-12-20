package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Function;
import com.dfsek.terra.api.structures.world.LandCheck;
import com.dfsek.terra.api.structures.world.OceanCheck;

public class CheckFunction implements Function<String> {
    private final TerraPlugin main;
    private final int x, y, z;

    public CheckFunction(TerraPlugin main, int x, int y, int z) {
        this.main = main;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String name() {
        return "check";
    }

    @Override
    public String apply(Location location) {
        if(new LandCheck(location.getWorld(), main).check(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z))
            return "LAND";
        if(new OceanCheck(location.getWorld(), main).check(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z))
            return "OCEAN";
        return "AIR";
    }

    @Override
    public String apply(Location location, Chunk chunk) {
        if(new LandCheck(location.getWorld(), main).check(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z))
            return "LAND";
        if(new OceanCheck(location.getWorld(), main).check(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z))
            return "OCEAN";
        return "AIR";
    }
}
