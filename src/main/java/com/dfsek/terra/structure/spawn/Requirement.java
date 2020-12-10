package com.dfsek.terra.structure.spawn;

import com.dfsek.terra.Terra;
import com.dfsek.terra.api.gaea.math.FastNoiseLite;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.World;

import java.util.Objects;

public abstract class Requirement {
    protected final World world;
    protected final Terra main;

    public Requirement(World world, Terra main) {
        this.world = world;
        this.main = main;
    }

    public abstract boolean matches(int x, int y, int z);

    protected FastNoiseLite getNoise() {
        return ((TerraChunkGenerator) Objects.requireNonNull(world.getGenerator())).getNoiseGenerator();
    }

    public World getWorld() {
        return world;
    }
}
