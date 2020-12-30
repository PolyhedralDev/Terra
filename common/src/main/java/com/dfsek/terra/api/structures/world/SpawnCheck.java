package com.dfsek.terra.api.structures.world;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.generation.math.Sampler;
import net.jafama.FastMath;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SpawnCheck {
    protected final World world;
    protected final TerraPlugin main;
    private final Map<Long, Sampler> cache;

    protected SpawnCheck(World world, TerraPlugin main) {
        this.world = world;
        this.main = main;
        cache = new LinkedHashMap<Long, Sampler>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Sampler> eldest) {
                return size() > main.getTerraConfig().getCheckCache();
            }
        };
    }

    public abstract boolean check(int x, int y, int z);

    protected double sample(int x, int y, int z, TerraBiomeGrid grid) {
        int cx = FastMath.floorDiv(x, 16);
        int cz = FastMath.floorDiv(z, 16);
        long key = (((long) cx) << 32) | (cz & 0xffffffffL);
        return cache.computeIfAbsent(key, k -> new Sampler(cx, cz, grid, world, 4, 8)).sample(x - (cx << 4), y, z - (cz << 4));
    }
}
