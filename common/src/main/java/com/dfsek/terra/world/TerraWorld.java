package com.dfsek.terra.world;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.profiler.WorldProfiler;
import com.dfsek.terra.world.generation.math.Sampler;
import net.jafama.FastMath;

public class TerraWorld {
    private final BiomeProvider provider;
    private final ConfigPack config;
    private final boolean safe;
    private final WorldProfiler profiler;
    private final World world;
    private final BlockData air;


    public TerraWorld(World w, ConfigPack c, TerraPlugin main) {
        config = c;
        profiler = new WorldProfiler(w);
        this.provider = config.getTemplate().getProviderBuilder().build(w.getSeed());
        this.world = w;
        air = main.getWorldHandle().createBlockData("minecraft:air");
        safe = true;
    }

    public World getWorld() {
        return world;
    }

    public static boolean isTerraWorld(World w) {
        return w.getGenerator().getHandle() instanceof GeneratorWrapper;
    }

    public BiomeProvider getBiomeProvider() {
        return provider;
    }

    public ConfigPack getConfig() {
        return config;
    }

    public boolean isSafe() {
        return safe;
    }

    public WorldProfiler getProfiler() {
        return profiler;
    }

    /**
     * Get a block at an ungenerated location
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return BlockData
     */
    public BlockData getUngeneratedBlock(int x, int y, int z) {
        UserDefinedBiome biome = (UserDefinedBiome) provider.getBiome(x, z);
        Palette<BlockData> palette = biome.getGenerator(world).getPalette(y);
        Sampler sampler = config.getSamplerCache().get(world, x, z);
        int fdX = FastMath.floorMod(x, 16);
        int fdZ = FastMath.floorMod(z, 16);
        double noise = sampler.sample(fdX, y, fdZ);
        if(noise > 0) {
            int level = 0;
            for(int yi = world.getMaxHeight(); yi > y; yi--) {
                if(sampler.sample(fdX, yi, fdZ) > 0) level++;
                else level = 0;
            }
            return palette.get(level, x, y, z);
        } else if(y <= biome.getConfig().getSeaLevel()) {
            return biome.getConfig().getOceanPalette().get(biome.getConfig().getSeaLevel() - y, x, y, z);
        } else return air;
    }

    public BlockData getUngeneratedBlock(Location l) {
        return getUngeneratedBlock(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    public BlockData getUngeneratedBlock(Vector3 v) {
        return getUngeneratedBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }
}
