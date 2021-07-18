package com.dfsek.terra.world;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.world.TerraWorldLoadEvent;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.generator.Sampler;
import com.dfsek.terra.config.pack.WorldConfigImpl;
import net.jafama.FastMath;

public class TerraWorldImpl implements TerraWorld {
    private final BiomeProvider provider;
    private final WorldConfigImpl config;
    private final World world;

    public TerraWorldImpl(World w, ConfigPack c, TerraPlugin main) {
        if(!w.isTerraWorld()) throw new IllegalArgumentException("World " + w + " is not a Terra World!");
        this.world = w;
        config = (WorldConfigImpl) c.toWorldConfig(this);
        this.provider = config.getProvider();
        main.getEventManager().callEvent(new TerraWorldLoadEvent(this, c));
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
    public BlockState getUngeneratedBlock(int x, int y, int z) {
        return world.getTerraGenerator().getBlock(world, x, y, z);
    }

    @Override
    public BlockState getUngeneratedBlock(Vector3 v) {
        return getUngeneratedBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }
}
