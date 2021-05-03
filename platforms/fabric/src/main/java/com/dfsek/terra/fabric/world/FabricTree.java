package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.profiler.ProfileFrame;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Locale;
import java.util.Random;

public class FabricTree implements Tree {
    private final ConfiguredFeature<?, ?> delegate;
    private final String id;

    public FabricTree(ConfiguredFeature<?, ?> delegate, String id) {
        this.delegate = delegate;
        this.id = id;
    }

    @Override
    @SuppressWarnings("try")
    public boolean plant(Location l, Random r) {
        try(ProfileFrame ignore = TerraFabricPlugin.getInstance().getProfiler().profile("fabric_tree:" + id.toLowerCase(Locale.ROOT))) {
            StructureWorldAccess fabricWorldAccess = ((StructureWorldAccess) l.getWorld());
            ChunkGenerator generatorWrapper = (ChunkGenerator) l.getWorld().getGenerator();
            return delegate.generate(fabricWorldAccess, generatorWrapper, r, new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        }
    }

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(TerraFabricPlugin.getInstance().getWorldHandle().createBlockData("minecraft:grass_block"),
                TerraFabricPlugin.getInstance().getWorldHandle().createBlockData("minecraft:podzol"),
                TerraFabricPlugin.getInstance().getWorldHandle().createBlockData("minecraft:mycelium"));
    }
}
