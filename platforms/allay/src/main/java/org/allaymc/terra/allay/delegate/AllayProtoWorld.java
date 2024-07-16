package org.allaymc.terra.allay.delegate;

import com.dfsek.terra.api.util.vector.Vector3;

import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.data.VanillaBlockPropertyTypes;
import org.allaymc.api.world.generator.context.OtherChunkAccessibleContext;
import org.allaymc.terra.allay.Mapping;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;

/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayProtoWorld(AllayServerWorld allayServerWorld, OtherChunkAccessibleContext context) implements ProtoWorld {

    private static final org.allaymc.api.block.type.BlockState WATER = BlockTypes.WATER.ofState(VanillaBlockPropertyTypes.LIQUID_DEPTH.createValue(15));

    @Override
    public int centerChunkX() {
        return context.getCurrentChunk().getX();
    }

    @Override
    public int centerChunkZ() {
        return context.getCurrentChunk().getZ();
    }

    @Override
    public ServerWorld getWorld() {
        return allayServerWorld;
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        var allayBlockState = (AllayBlockState)data;
        var containsWater = allayBlockState.containsWater();
        if (!containsWater) {
            var oldBlock = context.getBlockState(x, y, z).getBlockType();
            containsWater = oldBlock == BlockTypes.WATER;
        }
        context.setBlockState(x, y, z, allayBlockState.allayBlockState());
        if (containsWater) context.setBlockState(x, y, z, WATER, 1);
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        var blockState = context.getBlockState(x, y, z);
        return new AllayBlockState(blockState, Mapping.blockStateBeToJe(blockState));
    }

    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return new AllayFakeEntity(Vector3.of(x, y, z), allayServerWorld);
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        // TODO
        return null;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return allayServerWorld.getGenerator();
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return allayServerWorld.getBiomeProvider();
    }

    @Override
    public ConfigPack getPack() {
        return allayServerWorld.getPack();
    }

    @Override
    public long getSeed() {
        return allayServerWorld.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return allayServerWorld.getMaxHeight();
    }

    @Override
    public int getMinHeight() {
        return allayServerWorld.getMinHeight();
    }

    @Override
    public AllayServerWorld getHandle() {
        return allayServerWorld;
    }
}