package com.dfsek.terra.world;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.world.TerraWorldLoadEvent;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.generator.Sampler;
import com.dfsek.terra.config.pack.WorldConfigImpl;
import net.jafama.FastMath;

public class TerraWorldImpl implements TerraWorld {
    private final BiomeProvider provider;
    private final WorldConfigImpl config;
    private final boolean safe;
    private final World world;
    private final BlockData air;


    public TerraWorldImpl(World w, ConfigPack c, TerraPlugin main) {
        if(!w.isTerraWorld()) throw new IllegalArgumentException("World " + w + " is not a Terra World!");
        this.world = w;
        config = (WorldConfigImpl) c.toWorldConfig(this);
        this.provider = config.getProvider();
        air = main.getWorldHandle().createBlockData("minecraft:air");
        main.getEventManager().callEvent(new TerraWorldLoadEvent(this, c));
        safe = true;
    }


    @Override
    public World getWorld() {
        return world;
    }


    @Override
    public BiomeProvider getBiomeProvider() {
        return provider;
    }

    @Override
    public WorldConfigImpl getConfig() {
        return config;
    }

    @Override
    public boolean isSafe() {
        return safe;
    }


    @Override
    public BlockData getUngeneratedBlock(int x, int y, int z) {
        UserDefinedBiome biome = (UserDefinedBiome) provider.getBiome(x, z);
        Palette palette = biome.getGenerator(world).getPalette(y);
        Sampler sampler = config.getSamplerCache().get(x, z);
        int fdX = FastMath.floorMod(x, 16);
        int fdZ = FastMath.floorMod(z, 16);
        double noise = sampler.sample(fdX, y, fdZ);
        if(noise > 0) {
            int level = 0;
            for(int yi = world.getMaxHeight() - 1; yi > y; yi--) {
                if(sampler.sample(fdX, yi, fdZ) > 0) level++;
                else level = 0;
            }
            return palette.get(level, x, y, z);
        } else if(y <= biome.getConfig().getSeaLevel()) {
            return biome.getConfig().getOceanPalette().get(biome.getConfig().getSeaLevel() - y, x, y, z);
        } else return air;
    }

    @Override
    public BlockData getUngeneratedBlock(Vector3 v) {
        return getUngeneratedBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }
}
