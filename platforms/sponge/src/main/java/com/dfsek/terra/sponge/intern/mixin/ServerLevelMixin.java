package com.dfsek.terra.sponge.intern.mixin;

import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.sponge.TerraSpongePlugin;
import com.dfsek.terra.sponge.intern.SpongeChunkGeneratorWrapper;
import com.dfsek.terra.world.TerraWorld;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.api.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Shadow
    @Final
    private ServerChunkCache chunkSource;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;updateSkyBrightness()V"))
    public void injectConstructor(Level level) {
        if(chunkSource.getGenerator() instanceof SpongeChunkGeneratorWrapper) {
            SpongeChunkGeneratorWrapper chunkGeneratorWrapper = (SpongeChunkGeneratorWrapper) chunkSource.getGenerator();
            DimensionType dimensionType = ((Level) (Object) this).dimensionType();
            TerraSpongePlugin.getInstance().getWorldMap().put(dimensionType, Pair.of((ServerWorld) this, new TerraWorld((com.dfsek.terra.api.platform.world.World) this, chunkGeneratorWrapper.getPack(), TerraSpongePlugin.getInstance())));
            chunkGeneratorWrapper.setDimensionType(dimensionType);
            TerraSpongePlugin.getInstance().logger().info("Registered world " + this + " to dimension type " + dimensionType);
        }
        ((Level) (Object) this).updateSkyBrightness();
    }
}
