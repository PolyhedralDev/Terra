package com.dfsek.terra.mod.mixin.fix;

import net.minecraft.world.gen.structure.NetherFossilStructure;
import net.minecraft.world.gen.structure.Structure.Context;
import net.minecraft.world.gen.structure.Structure.StructurePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;


/**
 * Disable fossil generation in Terra worlds, as they are very expensive due to consistently triggering cache misses.
 * <p>
 * Currently, on Fabric, Terra cannot be specified as a Nether generator. TODO: logic to turn fossils back on if chunk generator is in
 * nether.
 */
@Mixin(NetherFossilStructure.class)
public class NetherFossilOptimization {
    @Inject(method = "getStructurePosition", at = @At("HEAD"), cancellable = true)
    public void injectFossilPositions(Context context, CallbackInfoReturnable<Optional<StructurePosition>> cir) {
        if(context.chunkGenerator() instanceof MinecraftChunkGeneratorWrapper) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
