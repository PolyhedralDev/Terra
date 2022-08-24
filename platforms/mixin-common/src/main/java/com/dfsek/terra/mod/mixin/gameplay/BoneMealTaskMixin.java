package com.dfsek.terra.mod.mixin.gameplay;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.task.BoneMealTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.random.RandomGenerator;

import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;
import com.dfsek.terra.mod.config.FertilizableConfig;
import com.dfsek.terra.mod.util.BiomeUtil;
import com.dfsek.terra.mod.util.MinecraftAdapter;
import com.dfsek.terra.mod.util.WritableWorldSeedRedirecter;


@Mixin(BoneMealTask.class)
public class BoneMealTaskMixin {
    
    @Inject(method = "canBoneMeal", at = @At("HEAD"), cancellable = true)
    public void injectCanBoneMeal(BlockPos pos, ServerWorld world, CallbackInfoReturnable<Boolean> cir) {
        Map<Identifier, FertilizableConfig> map = BiomeUtil.TERRA_BIOME_FERTILIZABLE_MAP.get(world.getBiome(pos));
        if(map != null) {
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            FertilizableConfig config = map.get(Registry.BLOCK.getId(block));
            if(config != null) {
                Boolean villagerFarmable = config.isVillagerFarmable();
                if(villagerFarmable != null) {
                    if(villagerFarmable) {
                        Structure canGrow = config.getCanGrow();
                        if(canGrow != null) {
                            RandomGenerator random = MinecraftAdapter.adapt(world.getRandom());
                            cir.setReturnValue(canGrow.generate(
                                    Vector3Int.of(pos.getX(), pos.getY(), pos.getZ()), new WritableWorldSeedRedirecter((WritableWorld) world, world.getSeed() + random.nextLong(Long.MAX_VALUE)), Rotation.NONE));
                            return;
                        }
                        cir.setReturnValue(true);
                        return;
                    }
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
