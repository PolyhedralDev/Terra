package com.dfsek.terra.forge.mixin;

import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.forge.TerraForgePlugin;
import com.dfsek.terra.forge.generation.ForgeChunkGeneratorWrapper;
import com.dfsek.terra.world.TerraWorld;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow
    @Final
    private ServerChunkProvider chunkSource;

    @Shadow
    protected abstract void initCapabilities();

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/server/ServerWorld;initCapabilities()V"))
    public void injectConstructor(ServerWorld serverWorld) {
        if(chunkSource.getGenerator() instanceof ForgeChunkGeneratorWrapper) {
            ForgeChunkGeneratorWrapper chunkGeneratorWrapper = (ForgeChunkGeneratorWrapper) chunkSource.getGenerator();
            DimensionType dimensionType = ((World) (Object) this).dimensionType();
            TerraForgePlugin.getInstance().getWorldMap().put(dimensionType, Pair.of((ServerWorld) (Object) this, new TerraWorld((com.dfsek.terra.api.platform.world.World) this, chunkGeneratorWrapper.getPack(), TerraForgePlugin.getInstance())));
            chunkGeneratorWrapper.setDimensionType(dimensionType);
            TerraForgePlugin.getInstance().logger().info("Registered world " + this + " to dimension type " + dimensionType);
        }
        initCapabilities();
    }
}
