package com.dfsek.terra.fabric.mixin.implementations.world;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.fabric.block.FabricBlockState;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import com.dfsek.terra.fabric.util.FabricUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
@Implements(@Interface(iface = World.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ServerWorldMixin {
    private WorldConfig config;
    @Shadow
    @Final
    private ServerChunkManager chunkManager;

    @Shadow
    public abstract long getSeed();

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/level/ServerWorldProperties;Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/world/dimension/DimensionType;Lnet/minecraft/server/WorldGenerationProgressListener;Lnet/minecraft/world/gen/chunk/ChunkGenerator;ZJLjava/util/List;Z)V")
    public void injectConstructor(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<net.minecraft.world.World> worldKey, DimensionType dimensionType, WorldGenerationProgressListener worldGenerationProgressListener, net.minecraft.world.gen.chunk.ChunkGenerator chunkGenerator, boolean debugWorld, long seed, List<Spawner> spawners, boolean shouldTickTime, CallbackInfo ci) {
        if(chunkGenerator instanceof FabricChunkGeneratorWrapper) {
            config = ((FabricChunkGeneratorWrapper) chunkGenerator).getPack().toWorldConfig((World) this);
        }
    }

    public int terra$getMaxHeight() {
        return (((ServerWorld) (Object) this).getBottomY()) + ((ServerWorld) (Object) this).getHeight();
    }

    public Chunk terra$getChunkAt(int x, int z) {
        return (Chunk) ((ServerWorld) (Object) this).getChunk(x, z);
    }

    public BlockEntity terra$getBlockState(int x, int y, int z) {
        return FabricUtil.createState((WorldAccess) this, new BlockPos(x, y, z));
    }

    public BlockState terra$getBlockData(int x, int y, int z) {
        return new FabricBlockState(((ServerWorld) (Object) this).getBlockState(new BlockPos(x, y, z)));
    }

    public void terra$setBlockData(int x, int y, int z, BlockState data, boolean physics) {
        BlockPos pos = new BlockPos(x, y, z);
        ((ServerWorld) (Object) this).setBlockState(pos, ((FabricBlockState) data).getHandle(), physics ? 3 : 1042);
    }

    public Entity terra$spawnEntity(Vector3 location, EntityType entityType) {
        net.minecraft.entity.Entity entity = ((net.minecraft.entity.EntityType<?>) entityType).create(((ServerWorld) (Object) this));
        entity.setPos(location.getX(), location.getY(), location.getZ());
        ((ServerWorld) (Object) this).spawnEntity(entity);
        return (Entity) entity;
    }

    @Intrinsic
    public long terra$getSeed() {
        return getSeed();
    }

    public int terra$getMinHeight() {
        return ((ServerWorld) (Object) this).getBottomY();
    }

    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }

    public ChunkGenerator terra$getGenerator() {
        return ((FabricChunkGeneratorWrapper) chunkManager.getChunkGenerator()).getHandle();
    }

    public BiomeProvider terra$getBiomeProvider() {
        return ((TerraBiomeSource) ((ServerWorld) (Object) this).getChunkManager().getChunkGenerator().getBiomeSource()).getProvider();
    }

    public WorldConfig terra$getConfig() {
        return config;
    }

    /**
     * Overridden in the same manner as {@link ChunkRegionMixin#hashCode()}
     *
     * @param other Another object
     * @return Whether this world is the same as other.
     * @see ChunkRegionMixin#hashCode()
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof ServerWorldAccess)) return false;
        return (ServerWorldAccess) this == (((ServerWorldAccess) other).toServerWorld());
    }
}
