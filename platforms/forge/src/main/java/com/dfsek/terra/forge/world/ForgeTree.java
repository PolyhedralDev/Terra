package com.dfsek.terra.forge.world;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.forge.world.generator.ForgeChunkGenerator;
import com.dfsek.terra.forge.world.handles.world.ForgeWorldAccess;
import com.dfsek.terra.profiler.ProfileFrame;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Locale;
import java.util.Random;

public class ForgeTree implements Tree {
    private final ConfiguredFeature<?, ?> delegate;
    private final String id;
    private final TerraPlugin main;

    public ForgeTree(ConfiguredFeature<?, ?> delegate, String id, TerraPlugin main) {
        this.delegate = delegate;
        this.id = id;
        this.main = main;
    }

    @Override
    public boolean plant(Location l, Random r) {
        try(ProfileFrame ignore = main.getProfiler().profile("fabric_tree:" + id.toLowerCase(Locale.ROOT))) {
            ForgeWorldAccess fabricWorldAccess = ((ForgeWorldAccess) l.getWorld());
            ChunkGenerator generatorWrapper = ((ForgeChunkGenerator) fabricWorldAccess.getGenerator()).getHandle();
            return delegate.place((ISeedReader) fabricWorldAccess.getHandle(), generatorWrapper, r, new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        }
    }

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createBlockData("minecraft:grass_block"),
                main.getWorldHandle().createBlockData("minecraft:podzol"),
                main.getWorldHandle().createBlockData("minecraft:mycelium"));
    }
}
