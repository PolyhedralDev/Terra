package com.dfsek.terra.fabric.mixin.implementations;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Locale;
import java.util.Random;
import java.util.Set;

@Mixin(ConfiguredFeature.class)
@Implements(@Interface(iface = Tree.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ConfiguredFeatureMixin {
    @Shadow
    public abstract boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos);

    @SuppressWarnings({"ConstantConditions", "try"})
    public boolean terra$plant(Location l, Random r) {
        String id = BuiltinRegistries.CONFIGURED_FEATURE.getId((ConfiguredFeature<?, ?>) (Object) this).toString();
        try(ProfileFrame ignore = TerraFabricPlugin.getInstance().getProfiler().profile("fabric_tree:" + id.toLowerCase(Locale.ROOT))) {
            StructureWorldAccess fabricWorldAccess = ((StructureWorldAccess) l.getWorld());
            ChunkGenerator generatorWrapper = (ChunkGenerator) l.getWorld().getGenerator();
            return generate(fabricWorldAccess, generatorWrapper, r, new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        }
    }

    public Set<BlockType> terra$getSpawnable() {
        return MaterialSet.get(TerraFabricPlugin.getInstance().getWorldHandle().createBlockData("minecraft:grass_block"),
                TerraFabricPlugin.getInstance().getWorldHandle().createBlockData("minecraft:podzol"),
                TerraFabricPlugin.getInstance().getWorldHandle().createBlockData("minecraft:mycelium"));
    }
}
