package com.dfsek.terra.bukkit.generator;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.world.BukkitProtoWorld;
<<<<<<< HEAD
=======
import com.dfsek.terra.bukkit.lootfix.LootFixService;
>>>>>>> 86e1828d0 (Initial fork: Terra 7.0.3 (lootfix + 1.21.10 compat + Java 21-25))


public class BukkitBlockPopulator extends BlockPopulator {
    private final BlockState air;
<<<<<<< HEAD
    private ConfigPack pack;

    public BukkitBlockPopulator(ConfigPack pack, BlockState air) {
        this.pack = pack;
        this.air = air;
=======
    private final LootFixService lootFixService;
    private ConfigPack pack;

    public BukkitBlockPopulator(ConfigPack pack, BlockState air, LootFixService lootFixService) {
        this.pack = pack;
        this.air = air;
        this.lootFixService = lootFixService;
>>>>>>> 86e1828d0 (Initial fork: Terra 7.0.3 (lootfix + 1.21.10 compat + Java 21-25))
    }

    public void setPack(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ,
                         @NotNull LimitedRegion limitedRegion) {
        pack.getStages().forEach(
            generationStage -> generationStage.populate(new BukkitProtoWorld(limitedRegion, air, pack.getBiomeProvider())));
<<<<<<< HEAD
=======

        if(lootFixService != null) {
            lootFixService.applyDuringWorldgen(worldInfo, random, chunkX, chunkZ, limitedRegion);
        }
>>>>>>> 86e1828d0 (Initial fork: Terra 7.0.3 (lootfix + 1.21.10 compat + Java 21-25))
    }
}
