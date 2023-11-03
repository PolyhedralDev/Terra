package com.dfsek.terra.bukkit.generator;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.world.BukkitProtoWorld;


public class BukkitBlockPopulator extends BlockPopulator {
    private final BlockState air;
    private ConfigPack pack;
    
    public BukkitBlockPopulator(ConfigPack pack, BlockState air) {
        this.pack = pack;
        this.air = air;
    }
    
    public void setPack(ConfigPack pack) {
        this.pack = pack;
    }
    
    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ,
                         @NotNull LimitedRegion limitedRegion) {
        pack.getStages().forEach(
                generationStage -> generationStage.populate(new BukkitProtoWorld(limitedRegion, air, pack.getBiomeProvider())));
    }
}
