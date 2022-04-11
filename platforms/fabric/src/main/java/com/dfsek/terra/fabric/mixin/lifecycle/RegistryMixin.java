package com.dfsek.terra.fabric.mixin.lifecycle;


import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dfsek.terra.fabric.FabricEntryPoint;


// Register Terra things to the builtin registries.
@Mixin(Registry.class)
public class RegistryMixin {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void registerTerraGenerators(CallbackInfo ci) {
        FabricEntryPoint.register();
    }
}
