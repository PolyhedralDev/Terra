package com.dfsek.terra.fabric.mixin.fix;

import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.NetherFossilGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.NetherFossilStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structure.Context;
import net.minecraft.world.gen.structure.Structure.StructurePosition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;


/**
 * Disable fossil generation in Terra worlds, as they are very expensive due to consistently triggering cache misses.
 *
 * Currently, on Fabric, Terra cannot be specified as a Nether generator. TODO: logic to turn fossils back on if chunk generator is in nether.
 */
@Mixin(NetherFossilStructure.class)
public class NetherFossilOptimization {
    @Inject(method = "getStructurePosition", at = @At("HEAD"), cancellable = true)
    public void injectFossilPositions(Context context, CallbackInfoReturnable<Optional<StructurePosition>> cir) {
        if(context.chunkGenerator() instanceof FabricChunkGeneratorWrapper) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
