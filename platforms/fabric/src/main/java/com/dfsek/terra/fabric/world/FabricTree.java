package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.world.generator.FabricChunkGenerator;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldAccess;
import com.dfsek.terra.profiler.ProfileFrame;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Locale;
import java.util.Random;

public class FabricTree implements Tree {
    private final ConfiguredFeature<?, ?> delegate;

    public FabricTree(ConfiguredFeature<?, ?> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean plant(Location l, Random r) {
        try(ProfileFrame ignore = TerraFabricPlugin.getInstance().getProfiler().profile("fabric_tree:" + delegate.toString().toLowerCase(Locale.ROOT))) {
            FabricWorldAccess fabricWorldAccess = ((FabricWorldAccess) l.getWorld());
            ChunkGenerator generatorWrapper = ((FabricChunkGenerator) fabricWorldAccess.getGenerator()).getHandle();
            return delegate.generate((StructureWorldAccess) fabricWorldAccess.getHandle(), generatorWrapper, r, new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        }
    }

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(TerraFabricPlugin.getInstance().getWorldHandle().createBlockData("minecraft:grass_block"),
                TerraFabricPlugin.getInstance().getWorldHandle().createBlockData("minecraft:podzol"),
                TerraFabricPlugin.getInstance().getWorldHandle().createBlockData("minecraft:mycelium"));
    }
}
