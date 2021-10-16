package com.dfsek.terra.fabric.mixin;

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

import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectConstructor(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session,
                                  ServerWorldProperties properties, RegistryKey<World> registryKey, DimensionType dimensionType,
                                  WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator,
                                  boolean debugWorld, long l, List<Spawner> list, boolean bl, CallbackInfo ci) {
        if(chunkGenerator instanceof FabricChunkGeneratorWrapper) {
            ((FabricChunkGeneratorWrapper) chunkGenerator).setWorld((ServerWorld) (Object) this);
            FabricEntryPoint.getPlatform().addWorld((ServerWorld) (Object) this);
            FabricEntryPoint.getPlatform().logger().info("Registered world " + this);
        }
    }
}
