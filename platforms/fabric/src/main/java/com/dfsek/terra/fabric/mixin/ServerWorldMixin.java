package com.dfsek.terra.fabric.mixin;

import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.world.TerraWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void injectConstructor(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> registryKey, DimensionType dimensionType, WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long l, List<Spawner> list, boolean bl, CallbackInfo ci) {
        if(chunkGenerator instanceof FabricChunkGeneratorWrapper) {
            TerraFabricPlugin.getInstance().getWorldMap().put(dimensionType, Pair.of((ServerWorld) (Object) this, new TerraWorld((com.dfsek.terra.api.platform.world.World) this, ((FabricChunkGeneratorWrapper) chunkGenerator).getPack(), TerraFabricPlugin.getInstance())));
            ((FabricChunkGeneratorWrapper) chunkGenerator).setDimensionType(dimensionType);
            TerraFabricPlugin.getInstance().logger().info("Registered world " + this + " to dimension type " + dimensionType);
        }
    }
}
