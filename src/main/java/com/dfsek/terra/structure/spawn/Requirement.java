package com.dfsek.terra.structure.spawn;

import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.World;
import org.polydev.gaea.math.FastNoiseLite;

import java.util.Objects;

public abstract class Requirement {
    private final World world;

    public Requirement(World world) {
        this.world = world;
    }

    public abstract boolean matches(int x, int y, int z);

    protected FastNoiseLite getNoise() {
        return ((TerraChunkGenerator) Objects.requireNonNull(world.getGenerator())).getNoiseGenerator();
    }

    public World getWorld() {
        return world;
    }
}
