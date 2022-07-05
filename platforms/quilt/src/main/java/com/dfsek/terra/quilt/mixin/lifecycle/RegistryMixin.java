package com.dfsek.terra.quilt.mixin.lifecycle;


import com.dfsek.terra.quilt.QuiltEntryPoint;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


// Register Terra things to the builtin registries.
@Mixin(Registry.class)
public class RegistryMixin {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void registerTerraGenerators(CallbackInfo ci) {
        QuiltEntryPoint.register();
    }
}
