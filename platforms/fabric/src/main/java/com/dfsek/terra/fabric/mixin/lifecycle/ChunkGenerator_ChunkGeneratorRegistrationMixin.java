package com.dfsek.terra.fabric.mixin.lifecycle;


import com.dfsek.terra.fabric.FabricEntryPoint;

import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ChunkGenerator.class)
public class ChunkGenerator_ChunkGeneratorRegistrationMixin {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void registerTerraGenerators(CallbackInfo ci) {
        FabricEntryPoint.register();
    }
}
