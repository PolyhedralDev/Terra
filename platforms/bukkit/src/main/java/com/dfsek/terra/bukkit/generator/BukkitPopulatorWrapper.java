package com.dfsek.terra.bukkit.generator;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitPopulatorWrapper extends BlockPopulator {
    private final ChunkGenerator delegate;
    
    public BukkitPopulatorWrapper(ChunkGenerator delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {
        delegate.getGenerationStages().forEach(populator -> {
            if(populator instanceof Chunkified) {
                populator.populate(BukkitAdapter.adapt(world), BukkitAdapter.adapt(source));
            }
        });
    }
}
