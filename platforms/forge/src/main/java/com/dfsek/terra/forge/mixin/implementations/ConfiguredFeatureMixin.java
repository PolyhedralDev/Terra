package com.dfsek.terra.forge.mixin.implementations;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.forge.TerraForgePlugin;
import com.dfsek.terra.profiler.ProfileFrame;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;


@Mixin(ConfiguredFeature.class)
@Implements(@Interface(iface = Tree.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ConfiguredFeatureMixin {
    
    @Shadow
    public abstract boolean place(ISeedReader p_242765_1_, ChunkGenerator p_242765_2_, Random p_242765_3_, BlockPos p_242765_4_);
    
    @SuppressWarnings({ "try" })
    public boolean terra$plant(Location l, Random r) {
        try(ProfileFrame ignore = TerraForgePlugin.getInstance().getProfiler().profile("forge_tree")) {
            ISeedReader world = ((ISeedReader) l.getWorld());
            ChunkGenerator generatorWrapper = (ChunkGenerator) l.getWorld().getGenerator();
            return place(world, generatorWrapper, r, new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        }
    }
    
    public MaterialSet terra$getSpawnable() {
        return MaterialSet.get(TerraForgePlugin.getInstance().getWorldHandle().createBlockData("minecraft:grass_block"),
                               TerraForgePlugin.getInstance().getWorldHandle().createBlockData("minecraft:podzol"),
                               TerraForgePlugin.getInstance().getWorldHandle().createBlockData("minecraft:mycelium"));
    }
}
